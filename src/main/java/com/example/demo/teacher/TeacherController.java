package com.example.demo.teacher;

import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Teacher> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Teacher getTeacher(@PathVariable Long id) {
        return service.getTeacher(id);
    }

    @PostMapping("/add")
    public void addTeacher(Teacher teacher) {
        service.addTeacher(teacher);
    }

    @PutMapping("/{id}/edit")
    public void updateTeacher(@RequestBody Teacher teacher,
                              @PathVariable Long id) {
        service.editTeacher(id, teacher);
    }

    @DeleteMapping("/{id}/delete")
    public void deleteTeacher(@PathVariable Long id) {
        service.deleteTeacher(id);
    }

    @PutMapping("/{teacherId}/subjects/add/{subjectId}")
    public void addSubject(@PathVariable Long subjectId,
                           @PathVariable Long teacherId) {
        service.addSubject(teacherId, subjectId);
    }

    @PutMapping("/{teacherId}/subjects/remove/{subjectId}")
    public void removeSubject(@PathVariable Long subjectId,
                              @PathVariable Long teacherId) {
        service.removeSubject(teacherId, subjectId);
    }


}
