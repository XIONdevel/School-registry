package com.example.demo.group;


import com.example.demo.exception.ExistsException;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.example.demo.teacher.Teacher;
import com.example.demo.teacher.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
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
    }

    public Group getGroup(Long id) {
        if (id == null) {
            throw new NullPointerException("id can not be null");
        }

        Optional<Group> group = groupRepository.findById(id);
        if (group.isEmpty()) {
            throw new ExistsException("group with this id does not exists");
        }

        return group.get();
    }

    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    public void addNewGroup(Group group) {
        if (group == null) {
            throw new NullPointerException("group can not be null");
        }

        String name = group.getName();

        if (name == null || name.isEmpty() || name.isBlank()) {
            throw new NullPointerException("group name can not be empty");
        }

        if (groupRepository.existsByName(group.getName())) {
            throw new ExistsException("group name is taken");
        }

        groupRepository.save(group);
    }

    public void removeTeacher(Long groupId, Long teacherId) {
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

        if (!group.isTaken()) {
            throw new ExistsException("this group is not taken");
        }

        if (teacher.getGroup() == null) {
            throw new ExistsException("this teacher does not have a group");
        }

        group.removeTeacher();
        teacher.removeGroup();
        groupRepository.save(group);
        teacherRepository.save(teacher);
    }

    public void addTeacher(Long groupId, Long teacherId) {
        if (groupId == null || teacherId == null) {
            throw new NullPointerException("group & teacher ids can not equals null");
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

        if (group.isTaken()) {
            throw new ExistsException("this group already taken");
        }

        if (teacher.getGroup() != null) {
            throw new ExistsException("this teacher already have a group");
        }

        group.addTeacherLead(teacher);
        teacher.addGroup(group);
        groupRepository.save(group);
        teacherRepository.save(teacher);
    }

    public void deleteGroup(Long id) {
        if (id == null) {
            throw new NullPointerException("id can not be null");
        }

        if (groupRepository.existsById(id)) {
            groupRepository.deleteById(id);
        }
    }

    public void editGroup(Long id, Group group) {
        if (group == null || id == null) {
            throw new NullPointerException("id: " + id + ", group: " + group);
        }

        if (!groupRepository.existsById(id)) {
            throw new ExistsException("group with this id does not exists");
        }

        Group saved = groupRepository.findById(id).get();
        groupRepository.deleteById(id);

        if (groupRepository.existsByName(group.getName())) {
            groupRepository.save(saved);
            throw new ExistsException("this name is taken");
        }
        groupRepository.save(group);
    }

    public List<Student> getAllStudents(Long groupId) {
        return studentRepository.findAllFromGroup(groupId);
    }
}
