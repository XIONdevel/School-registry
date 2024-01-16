package com.example.demo.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeacherController {
    private final TeacherService service;

    @Autowired
    public TeacherController(TeacherService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAll() {
        List<Teacher> list = service.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacher(@PathVariable Long id) {
        Teacher teacher = service.getTeacher(id);
        return ResponseEntity.ok(teacher);
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<Void> addTeacher(@RequestBody Teacher teacher,
                                           @PathVariable Long userId) {
        service.createTeacher(teacher, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<Void> updateTeacher(@RequestBody Teacher teacher,
                                              @PathVariable Long id) {
        service.editTeacher(id, teacher);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        service.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{teacherId}/subjects/add/{subjectId}")
    public ResponseEntity<Void> addSubject(
            @PathVariable Long subjectId,
            @PathVariable Long teacherId) {
        service.addSubject(teacherId, subjectId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{teacherId}/subjects/remove/{subjectId}")
    public ResponseEntity<Void> removeSubject(
            @PathVariable Long subjectId,
            @PathVariable Long teacherId) {
        service.removeSubject(teacherId, subjectId);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{teacherId}/add/group/{groupId}")
    public ResponseEntity<Void> addMainGroup(
            @PathVariable Long groupId,
            @PathVariable Long teacherId) {
        service.addGroup(groupId, teacherId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{teacherId}/remove/group/{groupId}")
    public ResponseEntity<Void> removeMainGroup(
            @PathVariable Long groupId,
            @PathVariable Long teacherId) {
        service.removeGroup(groupId, teacherId);
        return ResponseEntity.noContent().build();
    }


}
