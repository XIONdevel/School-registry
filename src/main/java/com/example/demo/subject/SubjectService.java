package com.example.demo.subject;


import com.example.demo.exception.ExistsException;
import com.example.demo.teacher.Teacher;
import com.example.demo.teacher.TeacherRepository;
import com.example.demo.utils.ServiceUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/subjects")
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final ServiceUtil serviceUtil;

    public SubjectService(SubjectRepository subjectRepository,
                          TeacherRepository teacherRepository,
                          ServiceUtil serviceUtil) {
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.serviceUtil = serviceUtil;
    }


    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    public Subject getSubject(Long id) {
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isEmpty()) {
            throw new ExistsException("Teacher does not exists");
        }
        return subject.get();
    }

    public void addSubject(Subject subject) {
        if (subject == null) {
            throw new NullPointerException("subject:" + subject);
        }
        subjectRepository.save(subject);
    }

    public void deleteSubject(Long id) {
        if (id == null) {
            throw new NullPointerException("id can not be null");
        }

        Subject subject;
        Optional<Subject> subjectOptional = subjectRepository.findById(id);

        if (subjectOptional.isEmpty()) {
            throw new ExistsException("subject with this id does not exists");
        }

        subject = subjectOptional.get();

        for (Teacher teacher : subjectOptional.get().getTeachers()) {
            teacher.removeSubject(subject);
        }

        subjectRepository.deleteById(id);
    }

    public void editSubject(Long id, Subject subject) {
        if (id == null || subject == null) {
            throw new NullPointerException("id:" + id + ", subject: " + subject);
        }

        if (subjectRepository.existsById(id)) {
            subjectRepository.deleteById(id);
        }

        subjectRepository.save(subject);
    }

    public void addTeacher(Long teacherId, Long subjectId) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalTeacher.isEmpty()) {
            throw new ExistsException("teacher with this id does not exists");
        }

        if (optionalSubject.isEmpty()) {
            throw new ExistsException("subject with this id does not exists");
        }

        Subject subject = optionalSubject.get();
        Teacher teacher = optionalTeacher.get();

        if (subject.getTeachers().contains(teacher)) {
            throw new ExistsException("this subject already taken by this teacher");
        }

        if (teacher.getSubjects().contains(subject)) {
            throw new ExistsException("this teacher already have this subject");
        }

        subject.addTeacher(teacher);
        teacher.addSubject(subject);
        subjectRepository.save(subject);
        teacherRepository.save(teacher);
    }

    public void removeTeacher(Long teacherId, Long subjectId) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalTeacher.isEmpty()) {
            throw new ExistsException("teacher with this id does not exists");
        }

        if (optionalSubject.isEmpty()) {
            throw new ExistsException("subject with this id does not exists");
        }

        Subject subject = optionalSubject.get();
        Teacher teacher = optionalTeacher.get();

        teacher.removeSubject(subject);
        subject.removeTeacher(teacher);
        teacherRepository.save(teacher);
        subjectRepository.save(subject);
    }



}
