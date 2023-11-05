package com.example.demo.teacher;

import com.example.demo.exception.ExistsException;
import com.example.demo.parents.Parent;
import com.example.demo.student.Student;
import com.example.demo.subject.Subject;
import com.example.demo.subject.SubjectRepository;
import com.example.demo.utils.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final ServiceUtil serviceUtil;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository,
                          SubjectRepository subjectRepository,
                          ServiceUtil serviceUtil) {
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.serviceUtil = serviceUtil;
    }

    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacher(Long id) {
        Optional<Teacher> teacher = teacherRepository.findById(id);
        if (teacher.isEmpty()) {
            throw new ExistsException("Teacher does not exists");
        }
        return teacher.get();
    }

    public void addTeacher(Teacher teacher) {
        if (teacher == null) {
            throw new NullPointerException("teacher:" + teacher);
        }

        if (serviceUtil.isEmailTaken(teacher.getEmail())) {
            throw new ExistsException("email is taken");
        }

        if (serviceUtil.isPhoneTaken(teacher.getPhone())) {
            throw new ExistsException("phone is taken");
        }

        teacherRepository.save(teacher);
    }

    public void deleteTeacher(Long id) {
        if (id == null) {
            throw new NullPointerException("id can not be null");
        }

        Teacher teacher;
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);

        if (teacherOptional.isEmpty()) {
            throw new ExistsException("teacher with this id does not exists");
        }

        teacher = teacherOptional.get();

        for (Subject s : teacher.getSubjects()) {
            s.removeTeacher(teacher);
        }

        teacherRepository.deleteById(id);
    }

    public void editTeacher(Long id, Teacher teacher) {
        if (id == null || teacher == null) {
            throw new NullPointerException("id:" + id + ", teacher: " + teacher);
        }

        if (teacherRepository.existsById(id)) {
            teacherRepository.deleteById(id);
        }

        if (serviceUtil.isEmailTaken(teacher.getEmail())) {
            throw new ExistsException("email already taken");
        }
        if (serviceUtil.isPhoneTaken(teacher.getPhone())) {
            throw new ExistsException("phone already taken");
        }

        teacherRepository.save(teacher);
    }

    public void addSubject(Long teacherId, Long subjectId) {
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

    public void removeSubject(Long teacherId, Long subjectId) {
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
