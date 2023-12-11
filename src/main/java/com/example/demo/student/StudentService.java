package com.example.demo.student;

import com.example.demo.exception.*;
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
        if (student.isEmpty()) {
            logger.error("Student with given id not found. Termination of operation");
            throw new StudentNotFoundException("Student with given id not found.");
        }
        return student.get();
    }

    public void addStudent(Student student) {
        if (student == null) {
            logger.error("Attempted to add a null student. Termination of operation.");
            throw new NullPointerException("Student can not be null.");
        }

        if (serviceUtil.isPhoneTaken(student.getPhone())) {
            logger.error("Phone is taken. Termination of operation.");
            throw new PhoneTakenException("Phone is taken.");
        }

        if (serviceUtil.isEmailTaken(student.getEmail())) {
            logger.error("Email is taken. Termination of operation.");
            throw new EmailTakenException("Email is taken.");
        }

        studentRepository.save(student);
        logger.info("Student saved, id: {}", student.getId());
    }

    public void editStudent(Long id, Student updatedStudent) {
        if (id == null || updatedStudent == null) {
            logger.error("Some value is null while editing student. Termination of operation.");
            throw new NullPointerException("id or updated student is null");
        }

        Optional<Student> optionalStudent = studentRepository.findById(id);
        Student student;

        if (optionalStudent.isEmpty()) {
            logger.error("Attempted edit do not existing student. Termination of operation.");
            throw new StudentNotFoundException("Student with this id does not exists");
        }

        student = optionalStudent.get();

        if (isPhoneTaken(updatedStudent.getPhone(), id)) {
            logger.error("Phone is taken. Termination of operation.");
            throw new PhoneTakenException("Phone is taken.");
        }

        if (isEmailTaken(updatedStudent.getEmail(), id)) {
            logger.error("Email taken. Termination of operation.");
            throw new EmailTakenException("Email is taken.");
        }

        student.setName(updatedStudent.getName());
        student.setSurname(updatedStudent.getSurname());
        student.setPhone(updatedStudent.getPhone());
        student.setEmail(updatedStudent.getEmail());

        studentRepository.save(student);
        logger.info("Student updated, id: {}", student.getId());
    }

    public void deleteStudent(Long id) {
        if (id == null) {
            logger.error("Attempted delete student by null id. Termination of operation.");
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
            logger.error("Student with given id does not exists. Termination of operation.");
            throw new StudentNotFoundException("Student with given id does not exists.");
        }
    }

    public void deleteParent(Long parentId, Long studentId) {
        if (parentId == null || studentId == null) {
            logger.error("Some of given id is null. Termination of operation.");
            throw new NullPointerException("Some of given id is null.");
        }

        Optional<Parent> pr = parentRepository.findById(parentId);
        Optional<Student> st = studentRepository.findById(studentId);
        Student student;
        Parent parent;

        if (pr.isEmpty()) {
            logger.error("Parent by given id not found. Termination of operation.");
            throw new ParentNotFoundException("Parent with given id not found.");
        }


        if (st.isEmpty()) {
            logger.error("Student by given id not found. Termination of operation.");
            throw new StudentNotFoundException("Student with given id not found.");
        }

        parent = pr.get();
        student = st.get();

        student.removeParent(parent);
        parent.removeChild(student);

        studentRepository.save(student);
        logger.info("Student saved, id:{}", studentId);
        parentRepository.save(parent);
        logger.info("Parent saved, id:{}", parentId);
    }

    public void addParent(Long parentId, Long studentId) {
        if (parentId == null || studentId == null) {
            logger.error("Some of given id is null. Termination of operation.");
            throw new NullPointerException("Some of given id is null.");
        }

        Optional<Parent> pr = parentRepository.findById(parentId);
        Optional<Student> st = studentRepository.findById(studentId);

        Student student;
        Parent parent;

        if (pr.isEmpty()) {
            logger.error("Parent with given id not found. Termination of operation.");
            throw new ParentNotFoundException("Parent with given id not found.");
        }

        if (st.isEmpty()) {
            logger.error("Student with given id not found. Termination of operation.");
            throw new StudentNotFoundException("Student with given id not found.");
        }

        parent = pr.get();
        student = st.get();

        student.addParents(parent);
        parent.addChild(student);

        studentRepository.save(student);
        logger.info("Student successfully saved.");
        parentRepository.save(parent);
        logger.info("Parent successfully saved.");
    }

    public Student findByName(String name) {
        if (name == null) {
            logger.error("Given name is null. Termination of operation.");
            throw new NullPointerException("Name can not be null.");
        }

        Optional<Student> optionalStudent = studentRepository.findStudentByName(name);

        if (optionalStudent.isEmpty()) {
            logger.error("Student with given name not found. Termination of operation.");
            throw new StudentNotFoundException("Student with given id not found.");
        }

        return optionalStudent.get();
    }

    public Student findByEmail(String email) {
        if (email == null) {
            logger.error("Given email is null. Termination of operation.");
            throw new NullPointerException("Given email is null.");
        }

        Optional<Student> optionalStudent = studentRepository.findStudentByEmail(email);

        if (optionalStudent.isEmpty()) {
            logger.error("Student with given name not found. Termination of operation.");
            throw new StudentNotFoundException("Student with given id not found.");
        }

        return optionalStudent.get();
    }

    protected boolean isPhoneTaken(String phone, Long currentId) {
        return teacherRepository.existsByPhone(phone)
                || parentRepository.existsByPhone(phone)
                || studentRepository.existsStudentByPhoneAndIdNot(phone, currentId);
    }

    protected boolean isEmailTaken(String email, Long currentId) {
        return teacherRepository.existsByEmail(email)
                || parentRepository.existsByEmail(email)
                || studentRepository.existsStudentByPhoneAndIdNot(email, currentId);
    }

    public Student getByEmail(String email) {
        if (email == null) {
            logger.error("Given email is null. Termination of operation.");
            throw new NullPointerException("Given email is null.");
        }

        Optional<Student> optionalStudent = studentRepository.findStudentByEmail(email);

        if (optionalStudent.isEmpty()) {
            logger.error("Student with given email not found. Termination of operation.");
            throw new StudentNotFoundException("Student with given email not found.");
        }
        return optionalStudent.get();
    }
}
