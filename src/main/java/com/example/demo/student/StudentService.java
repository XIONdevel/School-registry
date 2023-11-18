package com.example.demo.student;

import com.example.demo.exception.ExistsException;
import com.example.demo.parent.Parent;
import com.example.demo.parent.ParentRepository;
import com.example.demo.teacher.TeacherRepository;
import com.example.demo.utils.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final TeacherRepository teacherRepository;
    private final ServiceUtil serviceUtil;

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          ParentRepository parentRepository,
                          TeacherRepository teacherRepository,
                          ServiceUtil serviceUtil) {
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.teacherRepository = teacherRepository;
        this.serviceUtil = serviceUtil;
        logger.info("Student service initialization finished");
    }


    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public Student getStudent(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            return student.get();
        } else {
            logger.warn("Student with given id does not exists");
            throw new ExistsException("Student does not exists");
        }
    }

    public void addStudent(Student student) {
        if (student == null) {
            logger.warn("Attempted to add a null student. Aborting operation.");
            throw new NullPointerException("student can not be null");
        }

        if (serviceUtil.isPhoneTaken(student.getPhone())) {
            logger.warn("Attempted to add a taken phone. Aborting operation.");
            throw new ExistsException("phone is taken");
        }

        if (serviceUtil.isEmailTaken(student.getEmail())) {
            logger.warn("Attempted to add a taken email. Aborting operation.");
            throw new ExistsException("email is taken");
        }

        studentRepository.save(student);
        logger.info("Student saved: {}", student);
    }

    public void editStudent(Long id, Student updatedStudent) {
        if (id == null || updatedStudent == null) {
            logger.warn("Some value is null while editing student.");
            throw new NullPointerException("id or updated student is null");
        }

        Optional<Student> optionalStudent = studentRepository.findById(id);
        Student student;

        if (optionalStudent.isPresent()) {
            student = optionalStudent.get();
        } else {
            logger.warn("Attempted edit do not existing student. Aborting operation.");
            throw new ExistsException("student with this id does not exists");
        }

        if (isPhoneTaken(updatedStudent.getPhone(), id)) {
            logger.warn("Attempted edit student with taken phone. Aborting operation.");
            throw new ExistsException("phone is taken");
        }

        if (isEmailTaken(updatedStudent.getEmail(), id)) {
            logger.warn("Attempted edit student with taken email. Aborting operation.");
            throw new ExistsException("email is taken");
        }

        student.setName(updatedStudent.getName());
        student.setSurname(updatedStudent.getSurname());
        student.setPhone(updatedStudent.getPhone());
        student.setEmail(updatedStudent.getEmail());

        studentRepository.save(student);
        logger.info("Student updated: {}", student);
    }

    public void deleteStudent(Long id) {
        if (id == null) {
            logger.warn("Attempted delete student by null id");
            throw new NullPointerException("id can not be null");
        }

        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            Set<Parent> parents = student.get().getParents();

            for (Parent p : parents) {
                p.removeChild(student.get());
                parentRepository.save(p);
            }

            studentRepository.deleteById(id);
            logger.info("Student successfully deleted");
        } else {
            logger.warn("Student with id={} does not exists", id);
        }
    }

    public void deleteParent(Long parentId, Long studentId) {
        if (parentId == null || studentId == null) {
            logger.warn("Attempting delete parent from student with null value");
            throw new NullPointerException("Parent: " + parentId + ", student: " + studentId);
        }

        Optional<Parent> pr = parentRepository.findById(parentId);
        Optional<Student> st = studentRepository.findById(studentId);
        Student student;
        Parent parent;

        if (pr.isPresent()) {
            parent = pr.get();
        } else {
            logger.warn("Parent by given id does not exists");
            throw new ExistsException("parent does not exists");
        }

        if (st.isPresent()) {
            student = st.get();
        } else {
            logger.warn("Student by given id does not exists");
            throw new ExistsException("student does not exists");
        }

        student.removeParent(parent);
        parent.removeChild(student);
        studentRepository.save(student);
        logger.info("Student saved");
        parentRepository.save(parent);
        logger.info("Parent saved");
        logger.info("Parent removed");
    }

    public void addParent(Long parentId, Long studentId) {
        if (parentId == null || studentId == null) {
            logger.warn("Some of values is null while adding parent");
            throw new NullPointerException("Parent: " + parentId + ", student: " + studentId);
        }

        Optional<Parent> pr = parentRepository.findById(parentId);
        Optional<Student> st = studentRepository.findById(studentId);

        Student student;
        Parent parent;

        if (pr.isPresent()) {
            parent = pr.get();
        } else {
            logger.warn("Parent with given id does not exists");
            throw new ExistsException("parent does not exists");
        }

        if (st.isPresent()) {
            student = st.get();
        } else {
            logger.warn("Student with given id does not exists");
            throw new ExistsException("student does not exists");
        }

        if (student.getParents().contains(parent)) {
            logger.warn("This student already have this parent");
        } else {
            student.addParents(parent);
        }

        if (parent.getChildren().contains(student)) {
            logger.warn("This parent already have this child");
        } else {
            parent.addChild(student);
        }

        studentRepository.save(student);
        logger.info("Student successfully saved");
        parentRepository.save(parent);
        logger.info("Parent successfully saved");
    }

    public Student findByName(String name) {
        if (name == null) {
            logger.warn("Given name is null");
            throw new NullPointerException("name can not be null");
        }
        return studentRepository.findStudentByName(name);
    }

    public Student findByEmail(String email) {
        if (email == null) {
            logger.warn("Given email is null");
            throw new NullPointerException("email can not be null");
        }
        return studentRepository.findStudentByEmail(email);
    }

    protected boolean isPhoneTaken(String phone, Long currentId) {
        if (teacherRepository.existsByPhone(phone)
                || parentRepository.existsByPhone(phone)
                || studentRepository.existsStudentByPhoneAndIdNot(phone, currentId)) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean isEmailTaken(String email, Long currentId) {
        if (teacherRepository.existsByEmail(email)
                || parentRepository.existsByEmail(email)
                || studentRepository.existsStudentByPhoneAndIdNot(email, currentId)) {
            return true;
        } else {
            return false;
        }
    }
}
