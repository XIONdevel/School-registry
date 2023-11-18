package com.example.demo.group;


import com.example.demo.exception.ExistsException;
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
        logger.info("Group service initialized");
    }

    public Group getGroup(Long id) {
        if (id == null) {
            logger.warn("Given id null");
            throw new NullPointerException("id can not be null");
        }

        Optional<Group> group = groupRepository.findById(id);
        if (group.isEmpty()) {
            logger.warn("Group with given id does not exists");
            throw new ExistsException("group with this id does not exists");
        }
        return group.get();
    }

    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    public void addNewGroup(Group group) {
        if (group == null) {
            logger.warn("Given group is null");
            throw new NullPointerException("group can not be null");
        }

        String name = group.getName();

        if (name == null || name.isEmpty() || name.isBlank()) {
            logger.warn("Group name is empty");
            throw new NullPointerException("group name can not be empty");
        }

        if (groupRepository.existsByName(group.getName())) {
            logger.warn("Group name already taken");
            throw new ExistsException("group name is taken");
        }

        groupRepository.save(group);
        logger.info("Group successfully saved");
    }

    public void removeTeacher(Long groupId, Long teacherId) {
        if (groupId == null || teacherId == null) {
            logger.warn("Group id or teacher id is null");
            throw new NullPointerException("group & teacher ids can not be null");
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

        group.removeTeacher();
        teacher.removeGroup();
        groupRepository.save(group);
        logger.info("Group successfully saved");
        teacherRepository.save(teacher);
        logger.info("Teacher successfully saved");
    }

    public void addTeacher(Long groupId, Long teacherId) {
        if (groupId == null || teacherId == null) {
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

        group.addTeacherLead(teacher);
        teacher.addGroup(group);

        groupRepository.save(group);
        logger.info("Group successfully saved");
        teacherRepository.save(teacher);
        logger.info("Teacher successfully saved");
    }

    public void deleteGroup(Long id) {
        if (id == null) {
            logger.warn("Given id is null");
            throw new NullPointerException("id can not be null");
        }

        if (groupRepository.existsById(id)) {
            groupRepository.deleteById(id);
            logger.info("Group successfully deleted");
        } else {
            logger.info("Group with given id does not exists");
        }
    }

    public void editGroup(Long id, Group group) {
        if (group == null || id == null) {
            logger.warn("Group or id is null");
            throw new NullPointerException("id or group is null");
        }

        if (!groupRepository.existsById(id)) {
            logger.warn("Group with given id does not exists");
            throw new ExistsException("group with this id does not exists");
        }

        if (groupRepository
                .existsGroupByNameAndIdNot(group.getName(), group.getId())) {
            logger.warn("Name is taken");
            throw new ExistsException("this name is taken");
        }

        group.setId(id);

        groupRepository.save(group);
        logger.warn("Group successfully saved");
    }

    public List<Student> getAllStudents(Long groupId) {
        return studentRepository.findAllFromGroup(groupId);
    }
}
