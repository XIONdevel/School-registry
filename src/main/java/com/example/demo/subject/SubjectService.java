package com.example.demo.subject;


import com.example.demo.exception.ExistsException;
import com.example.demo.teacher.Teacher;
import com.example.demo.teacher.TeacherRepository;
import com.example.demo.utils.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/subjects")
public class SubjectService {
    public static final Logger logger = LoggerFactory.getLogger(SubjectService.class);
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final ServiceUtil serviceUtil;

    public SubjectService(SubjectRepository subjectRepository,
                          TeacherRepository teacherRepository,
                          ServiceUtil serviceUtil) {
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.serviceUtil = serviceUtil;
        logger.info("Subject service initialized");
    }


    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    public Subject getSubject(Long id) {
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isEmpty()) {
            logger.warn("Subject with given id does not exists");
            throw new ExistsException("Subject does not exists");
        }
        return subject.get();
    }

    public void addSubject(Subject subject) {
        if (subject == null) {
            logger.warn("Given subject is null");
            throw new NullPointerException("subject:" + subject);
        }
        subjectRepository.save(subject);
        logger.info("Subject successfully saved");
    }

    public void deleteSubject(Long id) {
        if (id == null) {
            logger.warn("Given id is null");
            throw new NullPointerException("id can not be null");
        }

        Subject subject;
        Optional<Subject> subjectOptional = subjectRepository.findById(id);

        if (subjectOptional.isEmpty()) {
            logger.warn("Subject with given id does not exists");
            throw new ExistsException("subject with this id does not exists");
        }

        subject = subjectOptional.get();

        for (Teacher teacher : subjectOptional.get().getTeachers()) {
            teacher.removeSubject(subject);
        }

        subjectRepository.deleteById(id);
        logger.info("Subject successfully deleted");
    }

    public void editSubject(Long id, Subject subject) {
        if (id == null || subject == null) {
            logger.info("Given subject or id is null");
            throw new NullPointerException("id:" + id + ", subject: " + subject);
        }

        if (subjectRepository.existsSubjectByNameAndIdNot(subject.getName(), id)) {
            logger.warn("Subject with same name already exists");
            throw new ExistsException("name is taken");
        }

        subjectRepository.save(subject);
        logger.info("Subject successfully saved");
    }

    public void addTeacher(Long teacherId, Long subjectId) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalTeacher.isEmpty()) {
            logger.warn("Teacher with given id does not exists");
            throw new ExistsException("teacher with this id does not exists");
        }

        if (optionalSubject.isEmpty()) {
            logger.warn("Subject with given id does not exists");
            throw new ExistsException("subject with this id does not exists");
        }

        Subject subject = optionalSubject.get();
        Teacher teacher = optionalTeacher.get();

        if (subject.getTeachers().contains(teacher)) {
            logger.warn("Subject already taken by this teacher");
        } else {
            subject.addTeacher(teacher);
        }

        if (teacher.getSubjects().contains(subject)) {
            logger.warn("Teacher already have this subject");
        } else {
            teacher.addSubject(subject);
        }

        subjectRepository.save(subject);
        logger.info("Subject saved");
        teacherRepository.save(teacher);
        logger.info("Teacher saved");
    }

    public void removeTeacher(Long teacherId, Long subjectId) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalTeacher.isEmpty()) {
            logger.warn("Teacher with given id does not exists");
            throw new ExistsException("teacher with this id does not exists");
        }

        if (optionalSubject.isEmpty()) {
            logger.warn("Subject with given id does not exists");
            throw new ExistsException("subject with this id does not exists");
        }

        Subject subject = optionalSubject.get();
        Teacher teacher = optionalTeacher.get();

        teacher.removeSubject(subject);
        subject.removeTeacher(teacher);

        teacherRepository.save(teacher);
        logger.info("Teacher successfully saved");
        subjectRepository.save(subject);
        logger.info("Subject successfully saved");
    }



}
