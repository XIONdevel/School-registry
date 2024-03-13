package com.example.school.entity.visitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
public class VisitorController {

    private final VisitorService visitorService;

    @Autowired
    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping
    public ResponseEntity<List<Visitor>> getAll() {
        List<Visitor> list = visitorService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Visitor> getById(@PathVariable Long id) {
        Visitor visitor = visitorService.getById(id);
        return ResponseEntity.ok(visitor);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        visitorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/new")
    public ResponseEntity<Void> create(@RequestBody Visitor visitor) {
        visitorService.create(visitor);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<Void> edit(
            @PathVariable Long id,
            @RequestBody Visitor visitor
    ) {
        visitorService.edit(id, visitor);
        return ResponseEntity.noContent().build();
    }

}
