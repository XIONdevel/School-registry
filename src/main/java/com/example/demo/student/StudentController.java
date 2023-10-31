package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/students")
public class StudentController {

    private final StudentService service;

    @Autowired
    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping()
    public List<Student> fullList() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Student getStudents(@PathVariable Long id) {
        return service.getStudent(id);
    }

    @PostMapping("/add")
    public void insertStudent(@RequestBody Student student) {
        service.addStudent(student);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteStudent(@PathVariable Long id) {
        service.deleteStudent(id);
    }

    @PutMapping("/edit")
    public void editStudent(@RequestBody Student student) {
        service.updateStudent(student);
    }



}
