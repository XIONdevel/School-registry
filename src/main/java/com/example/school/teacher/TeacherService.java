package com.example.school.teacher;

import com.example.school.exception.*;
import com.example.school.exception.notfound.DataNotFoundException;
import com.example.school.exception.notfound.UserNotFoundException;
import com.example.school.group.Group;
import com.example.school.group.GroupRepository;
import com.example.school.parent.ParentRepository;
import com.example.school.student.StudentRepository;
import com.example.school.subject.Subject;
import com.example.school.subject.SubjectRepository;
import com.example.school.user.permission.Role;
import com.example.school.user.User;
import com.example.school.user.UserRepository;
import com.example.school.utils.ServiceUtil;
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
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final SubjectRepository subjectRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ServiceUtil serviceUtil;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository,
                          StudentRepository studentRepository,
                          ParentRepository parentRepository,
                          SubjectRepository subjectRepository,
                          GroupRepository groupRepository,
                          UserRepository userRepository, ServiceUtil serviceUtil) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.serviceUtil = serviceUtil;
        logger.info("Teacher service initialized");
    }

    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacher(Long id) {
        if (id == null) {
            logger.error("Given id is null. Termination of operation.");
            throw new NullPointerException("Given id is null.");
        }

        Optional<Teacher> teacher = teacherRepository.findById(id);

        if (teacher.isEmpty()) {
            logger.error("Teacher with given id not found. Termination of operation.");
            throw new DataNotFoundException("Teacher with given id not found.");
        }
        return teacher.get();
    }

    public void createTeacher(Teacher teacher, Long userId) {
        if (teacher == null) {
            logger.error("Given teacher is null. Termination of operation.");
            throw new NullPointerException("Given teacher is null.");
        }

        if (serviceUtil.existsById(userId)) {
            logger.error("User data with given id already exists. Termination of operation.");
            throw new ExistsException("User data with given id already exists.");
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            logger.error("User with given id not found. Termination of operation.");
            throw new UserNotFoundException("User with given id not found.");
        }

        User user = optionalUser.get();
        if (!user.getRole().equals(Role.TEACHER)) {
            logger.error("You don`t have permission to create teacher data. Termination of operation.");
            throw new InvalidRoleException("You don`t have permission to create teacher data.");
        }

        if (serviceUtil.isEmailTaken(teacher.getEmail())) {
            logger.error("Given email is taken. Termination of operation.");
            throw new EmailTakenException("Given email is taken.");
        }

        if (serviceUtil.isPhoneTaken(teacher.getPhone())) {
            logger.error("Given phone is taken. Termination of operation.");
            throw new PhoneTakenException("Phone is taken.");
        }

        teacherRepository.save(teacher);
        logger.info("Teacher successfully saved.");
    }

    public void deleteTeacher(Long id) {
        if (id == null) {
            logger.error("Given id is null. Termination of operation.");
            throw new NullPointerException("Given id is null.");
        }

        Teacher teacher;
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);

        if (teacherOptional.isPresent()) {
            teacher = teacherOptional.get();

            for (Subject s : teacher.getSubjects()) {
                s.removeTeacher(teacher);
                subjectRepository.save(s);
            }
        }

        teacherRepository.deleteById(id);
        logger.info("Teacher successfully deleted.");
    }

    public void editTeacher(Long id, Teacher teacher) {
        if (id == null || teacher == null) {
            logger.error("Given teacher or id is null. Termination of operation.");
            throw new NullPointerException("Given teacher or id is null.");
        }

        if (isPhoneTaken(teacher.getPhone(), id)) {
            logger.error("Given phone is taken. Termination of operation.");
            throw new PhoneTakenException("Phone is taken.");
        }

        if (isEmailTaken(teacher.getEmail(), id)) {
            logger.error("Given email is taken. Termination of operation.");
            throw new EmailTakenException("Email is taken.");
        }

        teacher.setId(id);
        teacherRepository.save(teacher);
        logger.info("Teacher successfully saved.");
    }

    public void addSubject(Long teacherId, Long subjectId) {
        if (teacherId == null || subjectId == null) {
            logger.error("Given teacher or subject id is null. Termination of operation.");
            throw new NullPointerException("Given teacher or subject id is null.");
        }

        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalTeacher.isEmpty()) {
            logger.error("Teacher with given id not found. Termination of operation.");
            throw new DataNotFoundException("Teacher with given id not found.");
        }

        if (optionalSubject.isEmpty()) {
            logger.warn("Subject with given id not found. Termination of operation.");
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

    public void removeSubject(Long teacherId, Long subjectId) {
        if (teacherId == null || subjectId == null) {
            logger.error("Given teacher or subject id is null. Termination of operation.");
            throw new NullPointerException("Given teacher or subject id is null.");
        }

        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalTeacher.isEmpty()) {
            logger.error("Teacher with given id not found. Termination of operation.");
            throw new DataNotFoundException("Teacher with given id not found.");
        }

        if (optionalSubject.isEmpty()) {
            logger.warn("Subject with given id not found. Termination of operation.");
            throw new DataNotFoundException("Subject with given id not found.");
        }
        Subject subject = optionalSubject.get();
        Teacher teacher = optionalTeacher.get();

        teacher.removeSubject(subject);
        subject.removeTeacher(teacher);

        subjectRepository.save(subject);
        logger.info("Subject successfully saved.");
        teacherRepository.save(teacher);
        logger.info("Teacher successfully saved.");
    }

    public void addGroup(Long groupId, Long teacherId) {
        if (groupId == null || teacherId == null) {
            logger.error("Given group or teacher id is null. Termination of operation.");
            throw new NullPointerException("Given group or teacher id is null.");
        }

        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);

        if (optionalTeacher.isEmpty()) {
            logger.error("Teacher with given id not found. Termination of operation.");
            throw new DataNotFoundException("Teacher with this id not found.");
        }

        if (optionalGroup.isEmpty()) {
            logger.error("Group with given id not found. Termination of operation.");
            throw new DataNotFoundException("Group with given id not found.");
        }

        Teacher teacher = optionalTeacher.get();
        Group group = optionalGroup.get();

        group.addTeacherLead(teacher);
        teacher.setMainGroup(group);

        groupRepository.save(group);
        logger.info("Group saved successfully.");
        teacherRepository.save(teacher);
        logger.info("Teacher saved successfully.");
    }

    public void removeGroup(Long groupId, Long teacherId) {
        if (groupId == null || teacherId == null) {
            logger.error("Given group or teacher id is null. Termination of operation.");
            throw new NullPointerException("Given group or teacher id is null.");
        }

        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);

        if (optionalTeacher.isEmpty()) {
            logger.error("Teacher with given id not found. Termination of operation.");
            throw new DataNotFoundException("Teacher with this id not found.");
        }

        if (optionalGroup.isEmpty()) {
            logger.error("Group with given id not found. Termination of operation.");
            throw new DataNotFoundException("Group wi th given id not found.");
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

    protected boolean isEmailTaken(String email, Long teacherId) {
        return studentRepository.existsByEmail(email) ||
                teacherRepository.existsTeacherByEmailAndIdNot(email, teacherId) ||
                parentRepository.existsByEmail(email);
    }

    protected boolean isPhoneTaken(String phone, Long teacherId) {
        return studentRepository.existsByPhone(phone) ||
                teacherRepository.existsTeacherByPhoneAndIdNot(phone, teacherId) ||
                parentRepository.existsByPhone(phone);
    }
}