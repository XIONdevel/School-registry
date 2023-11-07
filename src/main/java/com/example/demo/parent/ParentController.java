package com.example.demo.parent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/parents")
public class ParentController {
    private final ParentService service;

    @Autowired
    public ParentController(ParentService service) {
        this.service = service;
    }

    @GetMapping()
    public List<Parent> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Parent getParent(@PathVariable Long id) {
        return service.getParent(id);
    }

    @DeleteMapping("/{id}")
    public void deleteParent(@PathVariable Long id) {
        service.deleteParent(id);
    }

    @PutMapping("/{id}")
    public void editParent(@PathVariable Long id,
                           @RequestBody Parent parent) {
        service.editParent(id, parent);
    }

    @PostMapping("/new")
    public void addParent(@RequestBody Parent parent) {
        service.addParent(parent);
    }

    @PutMapping("/{parentId}/child/add/{studentId}")
    public void addChild(@PathVariable Long parentId,
                         @PathVariable Long studentId) {
        service.addChild(studentId, parentId);
    }

    @PutMapping("/{parentId}/child/remove/{studentId}")
    public void removeChild(@PathVariable Long parentId,
                         @PathVariable Long studentId) {
        service.removeChild(studentId, parentId);
    }


}










