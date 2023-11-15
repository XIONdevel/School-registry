package com.example.demo.teacher;

import com.example.demo.exception.ExistsException;
import com.example.demo.group.Group;
import com.example.demo.group.GroupRepository;
import com.example.demo.subject.Subject;
import com.example.demo.subject.SubjectRepository;
import com.example.demo.utils.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {
    public static final Logger logger = LoggerFactory.getLogger(TeacherService.class);
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final GroupRepository groupRepository;
    private final ServiceUtil serviceUtil;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository,
                          SubjectRepository subjectRepository,
                          GroupRepository groupRepository,
                          ServiceUtil serviceUtil) {
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
        this.serviceUtil = serviceUtil;
        logger.info("Teacher service initialized");
    }

    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacher(Long id) {
        if (id == null) {
            logger.warn("Given id is null");
            throw new NullPointerException("given id is null");
        }

        Optional<Teacher> teacher = teacherRepository.findById(id);

        if (teacher.isEmpty()) {
            logger.warn("Teacher with given id does not exists");
            throw new ExistsException("Teacher does not exists");
        }
        return teacher.get();
    }

    public void addTeacher(Teacher teacher) {
        if (teacher == null) {
            logger.warn("Given teacher is null");
            throw new NullPointerException("teacher can not be null");
        }

        if (teacherRepository.existsTeacherByEmailAndIdNot(teacher.getEmail(), teacher.getId())) {
            logger.warn("Can not save teacher. Email is taken");
            throw new ExistsException("email is taken");
        }

        if (teacherRepository.existsTeacherByPhoneAndIdNot(teacher.getPhone(), teacher.getId())) {
            logger.warn("Can not save teacher. Phone is taken");
            throw new ExistsException("phone is taken");
        }

        teacherRepository.save(teacher);
        logger.info("Teacher successfully saved");
    }

    public void deleteTeacher(Long id) {
        if (id == null) {
            logger.warn("Given id is null");
            throw new NullPointerException("id can not be null");
        }

        Teacher teacher;
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);

        if (teacherOptional.isEmpty()) {
            logger.warn("Teacher with given id does not exists");
            throw new ExistsException("teacher with this id does not exists");
        }

        teacher = teacherOptional.get();

        for (Subject s : teacher.getSubjects()) {
            s.removeTeacher(teacher);
        }

        teacherRepository.deleteById(id);
        logger.info("Teacher deleted successfully");
    }

    public void editTeacher(Long id, Teacher teacher) {
        if (id == null || teacher == null) {
            logger.warn("Teacher or id is null");
            throw new NullPointerException("id or teach is null");
        }

        if (teacherRepository.existsTeacherByPhoneAndIdNot(teacher.getPhone(), id)) {
            logger.warn("Can not edit teacher. Phone taken");
            throw new ExistsException("phone is taken");
        }

        if (teacherRepository.existsTeacherByEmailAndIdNot(teacher.getEmail(), id)) {
            logger.warn("Can not edit teacher. Email taken");
            throw new ExistsException("email is taken");
        }

        teacherRepository.save(teacher);
        logger.info("Teacher successfully saved");
    }

    public void addSubject(Long teacherId, Long subjectId) {
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
        }

        if (teacher.getSubjects().contains(subject)) {
            logger.warn("This teacher already have this subject");
        }

        subject.addTeacher(teacher);
        teacher.addSubject(subject);
        subjectRepository.save(subject);
        logger.info("Subject successfully saved");
        teacherRepository.save(teacher);
        logger.info("Teacher successfully saved");
    }

    public void removeSubject(Long teacherId, Long subjectId) {
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
        subjectRepository.save(subject);
        logger.info("Subject successfully saved");
        teacherRepository.save(teacher);
        logger.info("Teacher successfully saved");
    }

    public void addGroup(Long groupId, Long teacherId) {
        if (groupId == null || teacherId == null) {
            logger.warn("Some value is null while adding group to teacher");
            throw new NullPointerException("group & teacher ids can not equals null");
        }

        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);

        if (optionalTeacher.isEmpty()) {
            logger.warn("Teacher with given id does not exists");
            throw new ExistsException("teacher with this id does not exists");
        }

        if (optionalGroup.isEmpty()) {
            logger.warn("Group with given id does not exists");
            throw new ExistsException("group with this id does not exists");
        }

        Teacher teacher = optionalTeacher.get();
        Group group = optionalGroup.get();

        if (group.isTaken()) {
            logger.info("Group already taken");
        }

        if (teacher.getGroup() != null) {
            logger.info("This teacher already have this group");
        }

        group.addTeacherLead(teacher);
        teacher.addGroup(group);
        groupRepository.save(group);
        logger.info("Group saved successfully");
        teacherRepository.save(teacher);
        logger.info("Teacher saved successfully");
    }

    public void removeGroup(Long groupId, Long teacherId) {
        if (groupId == null || teacherId == null) {
            throw new NullPointerException("group & teacher ids can not be null");
        }

        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);

        if (optionalTeacher.isEmpty()) {
            throw new ExistsException("teacher with this id does not exists");
        }

        if (optionalGroup.isEmpty()) {
            throw new ExistsException("group with this id does not exists");
        }

        Teacher teacher = optionalTeacher.get();
        Group group = optionalGroup.get();

        group.removeTeacher();
        teacher.removeGroup();

        groupRepository.save(group);
        logger.info("Group saved successfully");
        teacherRepository.save(teacher);
        logger.info("Teacher saved successfully");
    }
}