package com.example.school.subject;


import com.example.school.exception.NameTakenException;
import com.example.school.exception.notfound.DataNotFoundException;
import com.example.school.teacher.Teacher;
import com.example.school.teacher.TeacherRepository;
import com.example.school.utils.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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
        logger.info("Subject service initialized.");
    }


    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    public Subject getSubject(Long id) {
        if (id == null) {
            logger.error("Given id is null. Termination of operation.");
            throw new NullPointerException("Given id is null.");
        }

        Optional<Subject> subject = subjectRepository.findById(id);

        if (subject.isEmpty()) {
            logger.error("Subject with given id not found. Termination of operation.");
            throw new DataNotFoundException("Subject with given id not found.");
        }
        return subject.get();
    }

    public void addSubject(Subject subject) {
        if (subject == null) {
            logger.error("Given subject is null. Termination of operation.");
            throw new NullPointerException("Subject is null.");
        }

        if (subjectRepository.existsSubjectByNameAndIdNot(subject.getName(), subject.getId())) {
            logger.error("Name is taken. Termination of operation.");
            throw new NameTakenException("Name is taken.");
        }

        subjectRepository.save(subject);
        logger.info("Subject successfully saved.");
    }

    public void deleteSubject(Long id) {
        if (id == null) {
            logger.error("Given id is null. Termination of operation.");
            throw new NullPointerException("Given id is null.");
        }

        Optional<Subject> subjectOptional = subjectRepository.findById(id);

        if (subjectOptional.isEmpty()) {
            logger.error("Subject with given id not found. Termination of operation.");
            throw new DataNotFoundException("Subject with given id not found.");
        }

        Subject subject = subjectOptional.get();

        for (Teacher teacher : subject.getTeachers()) {
            teacher.removeSubject(subject);
            teacherRepository.save(teacher);
        }

        subjectRepository.deleteById(id);
        logger.info("Subject successfully deleted.");
    }

    public void editSubject(Long id, Subject subject) {
        if (id == null || subject == null) {
            logger.warn("Given subject or id is null. Termination of operation.");
            throw new NullPointerException("Subject or id is null.");
        }

        if (subjectRepository.existsSubjectByNameAndIdNot(subject.getName(), id)) {
            logger.error("Name is taken. Termination of operation.");
            throw new NameTakenException("Name is taken.");
        }

        subjectRepository.save(subject);
        logger.info("Subject successfully saved.");
    }

    public void addTeacher(Long teacherId, Long subjectId) {
        if (teacherId == null || subjectId == null) {
            logger.error("Teacher or subject id is null. Termination of operation.");
            throw new NullPointerException("Given teacher or subject id is null.");
        }

        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalTeacher.isEmpty()) {
            logger.error("Teacher with given id not found. Termination of operation.");
            throw new DataNotFoundException("Teacher with this id not found.");
        }

        if (optionalSubject.isEmpty()) {
            logger.error("Subject with given id not found. Termination of operation.");
            throw new DataNotFoundException("Subject with given id not found.");
        }

        Subject subject = optionalSubject.get();
        Teacher teacher = optionalTeacher.get();

        subject.addTeacher(teacher);
        teacher.addSubject(subject);

        subjectRepository.save(subject);
        logger.info("Subject successfully saved.");
        teacherRepository.save(teacher);
        logger.info("Teacher successfully saved.");
    }

    public void removeTeacher(Long teacherId, Long subjectId) {
        if (teacherId == null || subjectId == null) {
            logger.error("Teacher or subject id is null. Termination of operation.");
            throw new NullPointerException("Given teacher or subject id is null.");
        }

        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalTeacher.isEmpty()) {
            logger.error("Teacher with given id not found. Termination of operation.");
            throw new DataNotFoundException("Teacher with this id not found.");
        }

        if (optionalSubject.isEmpty()) {
            logger.error("Subject with given id not found. Termination of operation.");
            throw new DataNotFoundException("Subject with given id not found.");
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

    public Subject getByName(String name) {
        if (name == null) {
            logger.error("Given name is null. Termination of operation.");
            throw new NullPointerException("Given name is null.");
        }

        Optional<Subject> optionalSubject = subjectRepository.findByName(name);

        if (optionalSubject.isEmpty()) {
            logger.error("Subject with given name not found. Termination of operation.");
            throw new DataNotFoundException("Subject with given name not found.");
        }
        return optionalSubject.get();
    }
}
