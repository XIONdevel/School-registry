package com.example.demo.group;


import com.example.demo.exception.ExistsException;
import com.example.demo.exception.GroupNotFoundException;
import com.example.demo.exception.TeacherNotFoundException;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.example.demo.teacher.Teacher;
import com.example.demo.teacher.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);
    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository,
                        TeacherRepository teacherRepository,
                        StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        logger.info("Group service initialized.");
    }

    public Group getGroup(Long id) {
        if (id == null) {
            logger.error("Given id is null. Termination of operation.");
            throw new NullPointerException("Given id is null.");
        }

        Optional<Group> group = groupRepository.findById(id);

        if (group.isEmpty()) {
            logger.error("Group with given id not found. Termination of operation.");
            throw new GroupNotFoundException("Group with given id not found.");
        }

        return group.get();
    }

    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    public void addNewGroup(Group group) {
        if (group == null) {
            logger.error("Given group is null. Termination of operation.");
            throw new NullPointerException("Group can not be null.");
        }

        String name = group.getName();

        if (name == null || name.isEmpty() || name.isBlank()) {
            logger.error("Group name is empty. Termination of operation.");
            throw new NullPointerException("Group name can not be empty.");
        }

        if (groupRepository.existsByName(group.getName())) {
            logger.error("Given group name is taken. Termination of operation.");
            throw new GroupNotFoundException("Given group name is taken.");
        }

        groupRepository.save(group);
        logger.info("Group successfully saved.");
    }

    public void removeTeacher(Long groupId, Long teacherId) {
        if (groupId == null || teacherId == null) {
            logger.error("Given group or teacher id is null. Termination of operation.");
            throw new NullPointerException("Given group or teacher id is null.");
        }

        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);

        if (optionalTeacher.isEmpty()) {
            logger.error("Teacher with given id not found. Termination of operation.");
            throw new TeacherNotFoundException("Teacher with this id not found.");
        }

        if (optionalGroup.isEmpty()) {
            logger.error("Group with given id not found. Termination of operation.");
            throw new GroupNotFoundException("Group with given id not found.");
        }

        Teacher teacher = optionalTeacher.get();
        Group group = optionalGroup.get();

        group.removeTeacher();
        teacher.removeGroup();

        groupRepository.save(group);
        logger.info("Group successfully saved.");
        teacherRepository.save(teacher);
        logger.info("Teacher successfully saved.");
    }

    public void addTeacher(Long groupId, Long teacherId) {
        if (groupId == null || teacherId == null) {
            logger.error("Given group or teacher id is null. Termination of operation.");
            throw new NullPointerException("Given group or teacher id is null.");
        }

        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);

        if (optionalTeacher.isEmpty()) {
            logger.error("Teacher with given id not found. Termination of operation.");
            throw new TeacherNotFoundException("Teacher with this id not found.");
        }

        if (optionalGroup.isEmpty()) {
            logger.error("Group with given id not found. Termination of operation.");
            throw new GroupNotFoundException("Group with given id not found.");
        }

        Teacher teacher = optionalTeacher.get();
        Group group = optionalGroup.get();

        group.addTeacherLead(teacher);
        teacher.setMainGroup(group);

        groupRepository.save(group);
        logger.info("Group successfully saved");
        teacherRepository.save(teacher);
        logger.info("Teacher successfully saved");
    }

    public void deleteGroup(Long groupId) {
        if (groupId == null) {
            logger.error("Given group id is null. Termination of operation.");
            throw new NullPointerException("Given group id is null.");
        }

        Optional<Group> optionalGroup = groupRepository.findById(groupId);

        if (optionalGroup.isEmpty()) {
            logger.error("Group with given id not found. Termination of operation.");
            throw new GroupNotFoundException("Group with given id not found.");
        }

        Teacher teacher = optionalGroup.get().getTeacherLead();

        if (teacher != null) {
            teacher.removeGroup();
            teacherRepository.save(teacher);
        }

        groupRepository.deleteById(groupId);
        logger.info("Group successfully deleted.");
    }

    public void editGroup(Long id, Group group) {
        if (group == null || id == null) {
            logger.error("Group or id is null. Termination of operation.");
            throw new NullPointerException("Id or group is null.");
        }

        if (!groupRepository.existsById(id)) {
            logger.error("Group with given id not found. Termination of operation.");
            throw new GroupNotFoundException("Group with this id not found.");
        }

        if (groupRepository.existsGroupByNameAndIdNot(group.getName(), group.getId())) {
            logger.error("Name is taken. Termination of operation.");
            throw new ExistsException("Name is taken.");
        }

        group.setId(id);

        groupRepository.save(group);
        logger.info("Group successfully saved.");
    }

    public List<Student> getAllStudents(Long groupId) {
        if (groupId == null) {
            logger.error("Given group id is null. Termination of operation.");
            throw new NullPointerException("Given group id is null.");
        }

        Optional<Group> optionalGroup = groupRepository.findById(groupId);

        if (optionalGroup.isEmpty()) {
            logger.error("Group with given id not found. Termination of operation.");
            throw new GroupNotFoundException("Group with given id not found.");
        }

        return studentRepository.findAllByGroup(optionalGroup.get());
    }

    public Group getByName(String name) {
        if (name == null) {
            logger.error("Given name is null. Termination of operation.");
            throw new NullPointerException("Given name is null");
        }

        Optional<Group> optionalGroup =  groupRepository.findByName(name);

        if (optionalGroup.isEmpty()) {
            logger.error("Group with given id not found. Termination of operation.");
            throw new GroupNotFoundException("Group with given id not found.");
        }
        return optionalGroup.get();
    }
}
