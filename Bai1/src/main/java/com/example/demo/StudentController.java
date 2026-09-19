package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // 1. GET ALL, SEARCH & SORT (Lấy danh sách, có sắp xếp và lọc)
    @GetMapping
    public List<Student> getAllStudents(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        if (department != null && !department.trim().isEmpty()) {
            return studentRepository.findByDepartmentContainingIgnoreCase(department.trim());
        }

        if (name != null && !name.trim().isEmpty()) {
            return studentRepository.findByNameContainingIgnoreCase(name.trim());
        }

        Sort sort = direction.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();

        return studentRepository.findAll(sort);
    }

    // 2. GET BY ID (Lấy 1 sinh viên theo ID)
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. POST (Thêm mới sinh viên qua JSON Postman)
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student savedStudent = studentRepository.save(student);
        return ResponseEntity.ok(savedStudent);
    }

    // 4. PUT (Cập nhật thông tin sinh viên qua JSON Postman)
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student student = optionalStudent.get();
        student.setName(studentDetails.getName());
        student.setDob(studentDetails.getDob());
        student.setDepartment(studentDetails.getDepartment());
        student.setEmail(studentDetails.getEmail());

        Student updatedStudent = studentRepository.save(student);
        return ResponseEntity.ok(updatedStudent);
    }

    // 5. DELETE (Xóa sinh viên theo ID)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        studentRepository.deleteById(id);
        return ResponseEntity.ok("Đã xóa thành công sinh viên có id: " + id);
    }
}
