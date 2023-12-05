package com.example.demo.subject;

import com.example.demo.teacher.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/subjects")
public class SubjectController {
    private final SubjectService service;

    @Autowired
    public SubjectController(SubjectService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Subject>> getAll() {
        List<Subject> list = service.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subject> getSubject(@PathVariable Long id) {
        Subject subject = service.getSubject(id);
        return ResponseEntity.ok(subject);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addSubject(Subject subject) {
        service.addSubject(subject);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<Void> updateSubject(
            @RequestBody Subject subject,
            @PathVariable Long id) {
        service.editSubject(id, subject);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        service.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{subjectId}/subjects/add/{teacherId}")
    public ResponseEntity<Void> addTeacher(
            @PathVariable Long subjectId,
            @PathVariable Long teacherId) {
        service.addTeacher(teacherId, subjectId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{subjectId}/subjects/remove/{teacherId}")
    public ResponseEntity<Void> removeTeacher(
            @PathVariable Long subjectId,
            @PathVariable Long teacherId) {
        service.removeTeacher(teacherId, subjectId);
        return ResponseEntity.noContent().build();
    }
}
