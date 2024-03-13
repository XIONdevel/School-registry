package com.example.school.entity.student;

import com.example.school.exception.notfound.DataNotFoundException;
import com.example.school.entity.parent.Parent;
import com.example.school.entity.parent.ParentRepository;
import com.example.school.entity.teacher.TeacherRepository;
import com.example.school.entity.user.UserRepository;
import com.example.school.utils.ServiceUtil;
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
    private final UserRepository userRepository;
    private final ServiceUtil serviceUtil;

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          ParentRepository parentRepository,
                          TeacherRepository teacherRepository,
                          UserRepository userRepository, ServiceUtil serviceUtil) {
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.serviceUtil = serviceUtil;
        logger.info("Student service initialization finished");
    }


    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public void saveStudent(Student student) {
        if (student == null) {
            logger.error("Given student is null. Termination of operation.");
            throw new NullPointerException("Given student is null.");
        }

        logger.info("Student saved, id: {}", studentRepository.save(student).getId());
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
            throw new DataNotFoundException("Parent with given id not found.");
        }


        if (st.isEmpty()) {
            logger.error("Student by given id not found. Termination of operation.");
            throw new DataNotFoundException("Student with given id not found.");
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
            throw new DataNotFoundException("Parent with given id not found.");
        }

        if (st.isEmpty()) {
            logger.error("Student with given id not found. Termination of operation.");
            throw new DataNotFoundException("Student with given id not found.");
        }

        parent = pr.get();
        student = st.get();

        student.addParent(parent);
        parent.addChild(student);

        studentRepository.save(student);
        logger.info("Student successfully saved.");
        parentRepository.save(parent);
        logger.info("Parent successfully saved.");
    }

    protected boolean isPhoneTaken(String phone, Long currentId) {
        return teacherRepository.existsByPhone(phone)
                || parentRepository.existsByPhone(phone)
                || studentRepository.existsStudentByPhoneAndIdNot(phone, currentId);
    }

//    protected boolean isEmailTaken(String email, Long currentId) {
//        return teacherRepository.existsByEmail(email)
//                || parentRepository.existsByEmail(email)
//                || studentRepository.existsStudentByPhoneAndIdNot(email, currentId);
//    }

}
