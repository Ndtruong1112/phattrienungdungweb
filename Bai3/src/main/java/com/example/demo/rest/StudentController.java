package com.example.demo.rest;

import com.example.demo.model.Student;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // 1. GET ALL & SEARCH
    @GetMapping
    public List<Student> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long departmentId) {
        if (name != null && !name.trim().isEmpty()) {
            return studentService.getByName(name.trim());
        }
        if (departmentId != null) {
            return studentService.getByDepartmentId(departmentId);
        }
        return studentService.getAll();
    }

    // 2. GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        return studentService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. POST - Thêm mới qua Service & DAO
    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student) {
        Student savedStudent = studentService.create(student);
        return ResponseEntity.ok(savedStudent);
    }

    // 4. PUT - Cập nhật qua Service & DAO
    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student student) {
        return ResponseEntity.ok(studentService.update(id, student));
    }

    // 5. DELETE - Xóa qua Service & DAO
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.ok("Deleted student with id: " + id);
    }
}
