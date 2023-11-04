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

    @PutMapping("/update/{id}")
    public void update(@RequestBody Teacher teacher,
                       @PathVariable Long id) {
    }


}
