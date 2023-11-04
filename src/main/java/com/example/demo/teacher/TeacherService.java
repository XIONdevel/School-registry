package com.example.demo.teacher;

import com.example.demo.exception.ExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {
    private final TeacherRepository repository;

    @Autowired
    public TeacherService(TeacherRepository repository) {
        this.repository = repository;
    }

    public List<Teacher> getAll() {
        return repository.findAll();
    }

    public Teacher getTeacher(Long id) {
        Optional<Teacher> teacher = repository.findById(id);
        if (teacher.isPresent()) {
            return teacher.get();
        } else {
            throw new ExistsException("Teacher does not exists");
        }
    }

    public void addTeacher(Teacher teacher) {
//        if (repository.existsTeacherByPhone(teacher.getPhone())) {
//            throw new ExistsException("Phone is taken");
//        }
//
//        if (repository.existsTeacherByEmail(teacher.getEmail())) {
//            throw new ExistsException("Email is taken");
//        }
//
//        if (repository.existsById(teacher.getId())) {
//            throw new ExistsException("Teacher with same id already exists");
//        }
//
//        repository.save(teacher);
    }

    //TODO: remove
    public void editTeacher(Teacher teacher) {
        if (repository.existsById(teacher.getId())) {
            repository.save(teacher);
        } else {
            throw new ExistsException("Teacher does not exists");
        }
    }

    public void deleteTeacher(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new ExistsException("Teacher with this id does not exists");
        }
    }

    
}
