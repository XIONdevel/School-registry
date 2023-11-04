package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/students")
public class StudentController {
    private final StudentService service;

    @Autowired
    StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Student> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return service.getStudent(id);
    }

    @DeleteMapping("/{id}/delete")
    public void deleteStudent(@PathVariable Long id) {
        service.deleteStudent(id);
    }

    @PostMapping("/add")
    public void addStudent(Student student) {
        service.addStudent(student);
    }

    @PutMapping("/{id}/edit")
    public void editStudent(@PathVariable Long id,
                     @RequestBody Student student) {
        service.editStudent(id, student);
    }

    @PutMapping("/{studentId}/parent/remove/{parentId}")
    public void removeParent(@PathVariable Long parentId,
                      @PathVariable Long studentId) {
        service.deleteParent(parentId, studentId);
    }

    @PutMapping("/{studentId}/parent/add/{parentId}")
    public void addParent(@PathVariable Long parentId,
                      @PathVariable Long studentId) {
        service.addParent(parentId, studentId);
    }


}
