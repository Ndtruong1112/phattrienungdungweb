package com.example.demo.rest;

import com.example.demo.model.ClassInfo;
import com.example.demo.service.ClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classes")
@CrossOrigin(origins = "*")
public class ClassInfoController {

    @Autowired
    private ClassInfoService classInfoService;

    @GetMapping
    public List<ClassInfo> getAll() {
        return classInfoService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassInfo> getById(@PathVariable Long id) {
        return classInfoService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClassInfo> create(@RequestBody ClassInfo classInfo) {
        return ResponseEntity.ok(classInfoService.create(classInfo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassInfo> update(@PathVariable Long id, @RequestBody ClassInfo classInfo) {
        return ResponseEntity.ok(classInfoService.update(id, classInfo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        classInfoService.delete(id);
        return ResponseEntity.ok("Deleted class with id: " + id);
    }
}
