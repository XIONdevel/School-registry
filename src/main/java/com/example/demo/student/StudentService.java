package com.example.demo.student;

import com.example.demo.exception.ExistsException;
import com.example.demo.parents.Parent;
import com.example.demo.parents.ParentRepository;
import com.example.demo.parents.ParentService;
import com.example.demo.teacher.TeacherRepository;
import com.example.demo.utils.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StudentService {
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
    }


    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public Student getStudent(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            return student.get();
        } else {
            throw new ExistsException("Student does not exists");
        }
    }

    public void addStudent(Student student) {
        if (student == null) {
            throw new NullPointerException("student can not be null");
        }

        if (serviceUtil.isPhoneTaken(student.getPhone())) {
            throw new ExistsException("phone is taken");
        }

        if (serviceUtil.isEmailTaken(student.getEmail())) {
            throw new ExistsException("email is taken");
        }

        studentRepository.save(student);
    }

    public void editStudent(Long id, Student student) {
        if (id == null || student == null) {
            throw new NullPointerException("id:" + id + ", student: " + student);
        }

        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        }

        if (serviceUtil.isEmailTaken(student.getEmail())) {
            throw new ExistsException("email already taken");
        }
        if (serviceUtil.isPhoneTaken(student.getPhone())) {
            throw new ExistsException("phone already taken");
        }

        studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        if (id == null) {
            throw new NullPointerException("id can not be null");
        }

        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            Set<Parent> parents = student.get().getParents();

            for (Parent p : parents) {
                p.getChildren().remove(student);
            }

            studentRepository.deleteById(id);
        }
    }

    public void deleteParent(Long parentId, Long studentId) {
        if (parentId == null || studentId == null) {
            throw new NullPointerException("Parent: " + parentId + ", student: " + studentId);
        }

        Optional<Parent> pr = parentRepository.findById(parentId);
        Optional<Student> st = studentRepository.findById(studentId);
        Student student;
        Parent parent;

        if (pr.isPresent()) {
            parent = pr.get();
        } else {
            throw new ExistsException("parent does not exists");
        }

        if (st.isPresent()) {
            student = st.get();
        } else {
            throw new ExistsException("student does not exists");
        }

        student.removeParent(parent);
        studentRepository.save(student);
        parentRepository.save(parent);
    }

    public void addParent(Long parentId, Long studentId) {
        if (parentId == null || studentId == null) {
            throw new NullPointerException("Parent: " + parentId + ", student: " + studentId);
        }

        Optional<Parent> pr = parentRepository.findById(parentId);
        Optional<Student> st = studentRepository.findById(studentId);

        Student student;
        Parent parent;

        if (pr.isPresent()) {
            parent = pr.get();
        } else {
            throw new ExistsException("parent does not exists");
        }

        if (st.isPresent()) {
            student = st.get();
        } else {
            throw new ExistsException("student does not exists");
        }

        if (student.getParents().contains(parent)) {
            throw new ExistsException("this person already this kid parent");
        }

        student.addParents(parent);

        if (parent.getChildren().contains(student)) {
            throw new ExistsException("this person already this kid parent");
        }

        parent.addChildren(student);
        studentRepository.save(student);
        parentRepository.save(parent);
    }


}
