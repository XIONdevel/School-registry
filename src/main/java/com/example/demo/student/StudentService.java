package com.example.demo.student;

import com.example.demo.exception.ExistsException;
import com.example.demo.parent.Parent;
import com.example.demo.parent.ParentRepository;
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

    public void editStudent(Long id, Student updatedStudent) {
        if (id == null || updatedStudent == null) {
            throw new NullPointerException("id:" + id + ", updatedStudent: " + updatedStudent);
        }

        Optional<Student> optionalStudent = studentRepository.findById(id);
        Student student;

        if (optionalStudent.isPresent()) {
            student = optionalStudent.get();
        } else {
            throw new ExistsException("student with this id does not exists");
        }

        if (isPhoneTaken(updatedStudent.getPhone(), id)) {
            throw new ExistsException("phone is taken");
        }

        if (isEmailTaken(updatedStudent.getEmail(), id)) {
            throw new ExistsException("email is taken");
        }

        student.setName(updatedStudent.getName());
        student.setSurname(updatedStudent.getSurname());
        student.setPhone(updatedStudent.getPhone());
        student.setEmail(updatedStudent.getEmail());

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
                p.removeChild(student.get());
                parentRepository.save(p);
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
        parent.removeChild(student);
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

        parent.addChild(student);
        studentRepository.save(student);
        parentRepository.save(parent);
    }

    public Student findByName(String name) {
        if (name == null) {
            throw new NullPointerException("name can not be null");
        }
        return studentRepository.findStudentByName(name);
    }

    public Student findByEmail(String email) {
        if (email == null) {
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
