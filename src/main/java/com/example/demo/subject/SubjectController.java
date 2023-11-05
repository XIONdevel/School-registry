package com.example.demo.subject;

import com.example.demo.teacher.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public List<Subject> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Subject getSubject(@PathVariable Long id) {
        return service.getSubject(id);
    }

    @PostMapping("/add")
    public void addSubject(Subject subject) {
        service.addSubject(subject);
    }

    @PutMapping("/{id}/edit")
    public void updateSubject(@RequestBody Subject subject,
                              @PathVariable Long id) {
        service.editSubject(id, subject);
    }

    @DeleteMapping("/{id}/delete")
    public void deleteSubject(@PathVariable Long id) {
        service.deleteSubject(id);
    }

    @PutMapping("/{subjectId}/subjects/add/{teacherId}")
    public void addTeacher(@PathVariable Long subjectId,
                           @PathVariable Long teacherId) {
        service.addTeacher(teacherId, subjectId);
    }

    @PutMapping("/{subjectId}/subjects/remove/{teacherId}")
    public void removeTeacher(@PathVariable Long subjectId,
                              @PathVariable Long teacherId) {
        service.removeTeacher(teacherId, subjectId);
    }
}
