package com.example.demo.parents;

import com.example.demo.exception.ExistsException;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.StudentService;
import com.example.demo.teacher.TeacherRepository;
import com.example.demo.utils.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParentService {
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
            throw new NullPointerException("parent can not be null");
        }

        String email = parent.getEmail();
        String phone = parent.getPhone();

        if (serviceUtil.isEmailTaken(email)) {
            throw new ExistsException("email already taken");
        }

        if (serviceUtil.isPhoneTaken(phone)) {
            throw new ExistsException("phone already taken");
        }

        parentRepository.save(parent);
    }

    public void deleteParent(Long id) {
        if (parentRepository.existsById(id)) {
            parentRepository.deleteById(id);
        } else {
            throw new ExistsException("parent does not exists");
        }
    }

    public void editParent(Long id, Parent updatedParent) {
        if (id == null || updatedParent == null) {
            throw new NullPointerException("id:" + id + ", updated parent: " + updatedParent);
        }

        if (parentRepository.existsById(id)) {
            parentRepository.deleteById(id);
        }

        if (serviceUtil.isEmailTaken(updatedParent.getEmail())) {
            throw new ExistsException("email already taken");
        }
        if (serviceUtil.isPhoneTaken(updatedParent.getPhone())) {
            throw new ExistsException("phone already taken");
        }

        parentRepository.save(updatedParent);
    }

    public void addChild(Long studentId, Long parentId) {
        Optional<Student> studentT = studentRepository.findById(studentId);
        Optional<Parent> parentT = parentRepository.findById(parentId);

        if (studentT.isEmpty()) {
            throw new ExistsException("student with this id does not exists");
        }

        if (parentT.isEmpty()) {
            throw new ExistsException("parent with this id does not exists");
        }

        Parent parent = parentT.get();
        Student student = studentT.get();

        if (parent.getChildren().contains(student)) {
            throw new ExistsException("this student is already marked as a child of this person");
        }

        if (student.getParents().contains(parent)) {
            throw new ExistsException("this parent is already marked as a parent of this person");
        }

        student.getParents().add(parent);
        parent.getChildren().add(student);
        studentRepository.save(student);
        parentRepository.save(parent);

    }

    public void removeChild(Long studentId, Long parentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        Optional<Parent> parent = parentRepository.findById(parentId);

        if (student.isEmpty()) {
            throw new ExistsException("student with this id does not exists");
        }

        if (parent.isEmpty()) {
            throw new ExistsException("parent with this id does not exists");
        }

        if (parent.get().getChildren().contains(student.get())) {
            parent.get().getChildren().remove(student.get());
        } else {
            throw new ExistsException("This students is not this parent child");
        }

        if (student.get().getParents().contains(parent.get())) {
            student.get().getParents().remove(parent.get());
            studentRepository.save(student.get());
            parentRepository.save(parent.get());
        } else {
            throw new ExistsException("This parent is not this child parent");
        }
    }
}












