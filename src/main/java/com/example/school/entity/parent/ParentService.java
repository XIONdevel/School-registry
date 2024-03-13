package com.example.school.entity.parent;

import com.example.school.exception.ExistsException;
import com.example.school.exception.InvalidRoleException;
import com.example.school.exception.notfound.UserNotFoundException;
import com.example.school.exception.notfound.DataNotFoundException;
import com.example.school.entity.student.Student;
import com.example.school.entity.student.StudentRepository;
import com.example.school.entity.teacher.TeacherRepository;
import com.example.school.entity.user.permission.Role;
import com.example.school.entity.user.User;
import com.example.school.entity.user.UserRepository;
import com.example.school.utils.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParentService {
    public static final Logger logger = LoggerFactory.getLogger(ParentService.class);
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final ServiceUtil serviceUtil;

    @Autowired
    public ParentService(ParentRepository parentRepository,
                         StudentRepository studentRepository,
                         TeacherRepository teacherRepository,
                         UserRepository userRepository, ServiceUtil serviceUtil) {
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.serviceUtil = serviceUtil;
        logger.info("Parent service initialized");
    }

    public Parent getParent(Long id) {
        if (id == null) {
            logger.error("Given id is null. Termination of operation.");
            throw new NullPointerException("Given id is null.");
        }

        Optional<Parent> parent = parentRepository.findById(id);

        if (parent.isEmpty()) {
            logger.error("Parent with given id not found. Termination of operation.");
            throw new DataNotFoundException("Parent do not exists exception");
        }
        return parent.get();

    }

    public List<Parent> getAll() {
        return parentRepository.findAll();
    }

    public void deleteParent(Long id) {
        if (id == null) {
            logger.warn("Given id is null");
            throw new NullPointerException("given id is null");
        }

        Optional<Parent> optionalParent = parentRepository.findById(id);
        if (optionalParent.isPresent()) {
            Parent parent = optionalParent.get();
            for (Student s : parent.getChildren()) {
                s.removeParent(parent);
                studentRepository.save(s);
            }
        }
        parentRepository.deleteById(id);
    }

    public void addChild(Long studentId, Long parentId) {
        Optional<Student> studentT = studentRepository.findById(studentId);
        Optional<Parent> parentT = parentRepository.findById(parentId);

        if (studentT.isEmpty()) {
            logger.warn("Student with given id does not exists");
            throw new ExistsException("student with given id does not exists");
        }

        if (parentT.isEmpty()) {
            logger.warn("Parent with given id does not exists");
            throw new ExistsException("parent with given id does not exists");
        }

        Parent parent = parentT.get();
        Student student = studentT.get();

        if (parent.getChildren().contains(student)) {
            logger.warn("This student is already marked as a child of this person");
        }

        if (student.getParents().contains(parent)) {
            logger.warn("This parent is already marked as a parent of this person");
        }

        student.addParent(parent);
        parent.addChild(student);

        studentRepository.save(student);
        logger.info("Student successfully saved");
        parentRepository.save(parent);
        logger.info("Parent successfully saved");
    }

    public void removeChild(Long studentId, Long parentId) {
        if (studentId == null ||
                parentId == null) {
            logger.warn("Some of given ids is null");
            throw new NullPointerException("Some of given ids is null");
        }

        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        Optional<Parent> optionalParent = parentRepository.findById(parentId);

        if (optionalStudent.isEmpty()) {
            logger.warn("Student with given id does not exists");
            throw new ExistsException("student with this id does not exists");
        }

        if (optionalParent.isEmpty()) {
            logger.warn("Parent with given id does not exists");
            throw new ExistsException("parent with this id does not exists");
        }

        Student student = optionalStudent.get();
        Parent parent = optionalParent.get();

        student.getParents().remove(parent);
        parent.getChildren().remove(student);

        studentRepository.save(optionalStudent.get());
        logger.info("Student successfully saved");
        parentRepository.save(optionalParent.get());
        logger.info("Parent successfully saved");
    }

    public void saveParent(Parent parent) {
        if (parent == null) {
            logger.error("Given parent is null. Termination of operation.");
            throw new NullPointerException("Given parent is null.");
        }

        logger.info("Parent saved, id: {}", parentRepository.save(parent).getId());
    }
}












