package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<List<Student>> getAll() {
        List<Student> list = service.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = service.getStudent(id);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        service.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addStudent(Student student) {
        service.addStudent(student);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<Void> editStudent(
            @PathVariable Long id,
            @RequestBody Student student) {
        service.editStudent(id, student);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{studentId}/parents/remove/{parentId}")
    public ResponseEntity<Void> removeParent(
            @PathVariable Long parentId,
            @PathVariable Long studentId) {
        service.deleteParent(parentId, studentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{studentId}/parents/add/{parentId}")
    public ResponseEntity<Void> addParent(
            @PathVariable Long parentId,
            @PathVariable Long studentId) {
        service.addParent(parentId, studentId);
        return ResponseEntity.noContent().build();
    }
}
