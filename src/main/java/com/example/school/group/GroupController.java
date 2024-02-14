package com.example.school.group;

import com.example.school.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<List<Group>> getAll() {
        List<Group> list = service.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroup(@PathVariable Long id) {
        Group group = service.getGroup(id);
        return ResponseEntity.ok(group);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        service.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/new")
    public ResponseEntity<Void> newGroup(@RequestBody Group group) {
        service.addNewGroup(group);
        Long id = service.getByName(group.getName()).getId();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<Void> editGroup(
            @PathVariable Long id,
            @RequestBody Group group) {
        service.editGroup(id, group);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{groupId}/add/teacher/{teacherId}")
    public ResponseEntity<Void> addTeacher(
            @PathVariable Long groupId,
            @PathVariable Long teacherId) {
        service.addTeacher(groupId, teacherId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{groupId}/remove/teacher/{teacherId}")
    public ResponseEntity<Void> removeTeacher(
            @PathVariable Long groupId,
            @PathVariable Long teacherId) {
        service.removeTeacher(groupId, teacherId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getStudents(@PathVariable Long id) {
        List<Student> list = service.getAllStudents(id);
        return ResponseEntity.ok(list);
    }

}
