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

    //TODO: implement multiple search
    @GetMapping("/{ids}")
    public List<Student> getStudents(@PathVariable Long[] ids) {
        return service.getStudents(ids);
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
