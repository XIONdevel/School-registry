package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = service.getStudent(id);
        return ResponseEntity.ok(student);
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

    @PutMapping("/{studentId}/parents/remove/{parentId}")
    public void removeParent(
            @PathVariable Long parentId,
            @PathVariable Long studentId) {
        service.deleteParent(parentId, studentId);
    }

    @PutMapping("/{studentId}/parents/add/{parentId}")
    public void addParent(
            @PathVariable Long parentId,
            @PathVariable Long studentId) {
        service.addParent(parentId, studentId);
    }


}
