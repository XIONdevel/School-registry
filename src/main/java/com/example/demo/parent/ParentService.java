package com.example.demo.parent;

import com.example.demo.exception.ExistsException;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.example.demo.teacher.TeacherRepository;
import com.example.demo.utils.ServiceUtil;
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
    private final ServiceUtil serviceUtil;

    @Autowired
    public ParentService(ParentRepository parentRepository,
                         StudentRepository studentRepository,
                         TeacherRepository teacherRepository,
                         ServiceUtil serviceUtil) {
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.serviceUtil = serviceUtil;
        logger.info("Parent service initialized");
    }

    public Parent getParent(Long id) {
        Optional<Parent> parent = parentRepository.findById(id);
        if (parent.isPresent()) {
            return parent.get();
        } else {
            throw new ExistsException("Parent do not exists exception");
        }
    }

    public List<Parent> getAll() {
        return parentRepository.findAll();
    }

    public void addParent(Parent parent) {
        if (parent == null) {
            logger.warn("Given parent is null");
            throw new NullPointerException("parent can not be null");
        }

        String email = parent.getEmail();
        String phone = parent.getPhone();
        parent.setId(null);

        if (serviceUtil.isEmailTaken(email)) {
            logger.warn("Email is taken");
            throw new ExistsException("email already taken");
        }

        if (serviceUtil.isPhoneTaken(phone)) {
            logger.warn("Phone is taken");
            throw new ExistsException("phone already taken");
        }

        parentRepository.save(parent);
        logger.info("Parent successfully saved");
    }

    public void deleteParent(Long id) {
        if (id == null) {
            logger.warn("Given id is null");
            throw new NullPointerException("given id is null");
        }
        if (parentRepository.existsById(id)) {
            parentRepository.deleteById(id);
        } else {
            logger.warn("Parent with given id does not exist");
        }
    }

    public void editParent(Long id, Parent updatedParent) {
        if (id == null || updatedParent == null) {
            logger.warn("Id or given paren is null");
            throw new NullPointerException("id or updated parent is null");
        }

        Optional<Parent> existingParent =
                parentRepository.findById(id);

        if (existingParent.isEmpty()) {
            logger.warn("Parent with given id does not exists");
            throw new ExistsException("Parent with given id does not exists");
        }

        if (isEmailTaken(updatedParent.getEmail(), id)) {
            logger.warn("Email is taken");
            throw new ExistsException("email is taken");
        }

        if (isPhoneTaken(updatedParent.getPhone(), id)) {
            logger.warn("Phone is taken");
            throw new ExistsException("phone is taken");
        }

        Parent parent = existingParent.get();
        parent.setName(updatedParent.getName());
        parent.setSurname(updatedParent.getSurname());
        parent.setPhone(updatedParent.getPhone());
        parent.setEmail(updatedParent.getEmail());

        parentRepository.save(parent);
        logger.info("Parent successfully saved");
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

        student.addParents(parent);
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

    protected boolean isEmailTaken(String email, Long parentId) {
        if (email == null || parentId == null) {
            return false;
        }

        return studentRepository.existsByEmail(email) ||
                teacherRepository.existsByEmail(email) ||
                parentRepository.existsParentByEmailAndIdNot(email, parentId);
    }

    protected boolean isPhoneTaken(String phone, Long parentId) {
        if (phone == null || parentId == null) {
            return false;
        }

        return studentRepository.existsByPhone(phone) ||
                teacherRepository.existsByPhone(phone) ||
                parentRepository.existsParentByPhoneAndIdNot(phone, parentId);
    }
}












