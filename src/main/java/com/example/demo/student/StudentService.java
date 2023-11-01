package com.example.demo.student;

import com.example.demo.exception.ExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**
 * TODO:refactor
 */
@Service
public class StudentService {
    private final StudentRepository repository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.repository = studentRepository;
    }

    public List<Student> getAll() {
        return repository.findAll();
    }

    public Student getStudent(Long id) {
        Optional<Student> student = repository.findById(id);
        if (student.isPresent()) {
            return student.get();
        } else {
            throw new ExistsException("Student does not exists");
        }
    }

    public void addStudent(Student student) {
        Student checkStudent =
                repository.findByPhone_number(student.getPhone());

        if (checkStudent != null) {
            throw new ExistsException("Phone number is taken");
        }

        repository.save(student);
    }

    //TODO: implement checking by phone
    public void updateStudent(Student student) {
        if (repository.findById(student.getId()).isPresent()) {
            repository.save(student);
        } else {
            throw new ExistsException("Student does not exists");
        }
    }

    public void deleteStudent(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new ExistsException("Student does not exists");
        }
    }

}
