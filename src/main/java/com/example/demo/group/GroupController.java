package com.example.demo.group;

import com.example.demo.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {
    private final GroupService service;

    @Autowired
    public GroupController(GroupService service) {
        this.service = service;
    }

    @GetMapping
    public List<Group> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Group getGroup(@PathVariable Long id) {
        return service.getGroup(id);
    }

    @DeleteMapping("/{id}/delete")
    public void deleteGroup(@PathVariable Long id) {
        service.deleteGroup(id);
    }

    @PostMapping("/new")
    public void newGroup(@RequestBody Group group) {
        service.addNewGroup(group);
    }

    @PutMapping("/{id}/edit")
    public void editGroup(@PathVariable Long id,
                          @RequestBody Group group) {
        service.editGroup(id, group);
    }

    @PutMapping("/{groupId}/add/teacher/{teacherId}")
    public void addTeacher(@PathVariable Long groupId,
                           @PathVariable Long teacherId) {
        service.addTeacher(groupId, teacherId);
    }

    @PutMapping("/{groupId}/remove/teacher/{teacherId}")
    public void removeTeacher(@PathVariable Long groupId,
                           @PathVariable Long teacherId) {
        service.removeTeacher(groupId, teacherId);
    }

    @GetMapping("/{id}/students")
    public List<Student> getStudents(@PathVariable Long id) {
        return service.getAllStudents(id);
    }

}
