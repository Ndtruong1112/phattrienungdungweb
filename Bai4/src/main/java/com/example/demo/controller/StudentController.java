package com.example.demo.controller;

import com.example.demo.entity.Department;
import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping
    public List<Student> getAllStudents(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String name) {
        if (department != null && !department.trim().isEmpty()) {
            return studentService.findByDepartment(department.trim());
        }
        if (name != null && !name.trim().isEmpty()) {
            return studentService.findByName(name.trim());
        }
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody Student student) {
        try {
            resolveDepartment(student);
            Student created = studentService.createStudent(student);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        try {
            resolveDepartment(studentDetails);
            Student updated = studentService.updateStudent(id, studentDetails);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        Optional<Student> existing = studentService.getStudentById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Đã xóa thành công sinh viên có id: " + id);
    }

    @GetMapping("/by-department")
    public List<Student> getStudentsByDepartment(@RequestParam String department) {
        return studentService.findByDepartment(department);
    }

    @GetMapping("/search")
    public List<Student> searchStudents(@RequestParam String name) {
        return studentService.findByName(name);
    }

    private void resolveDepartment(Student student) {
        if (student.getDepartment() == null) {
            return;
        }

        if (student.getDepartment().getDepartmentId() != null) {
            Department dept = entityManager.find(Department.class, student.getDepartment().getDepartmentId());
            if (dept != null) {
                student.setDepartment(dept);
                return;
            }
        }

        String departmentName = student.getDepartment().getDepartmentName();
        if (departmentName != null && !departmentName.trim().isEmpty()) {
            TypedQuery<Department> query = entityManager.createQuery(
                    "SELECT d FROM Department d WHERE LOWER(d.departmentName) = LOWER(:name) OR LOWER(d.departmentCode) = LOWER(:name)",
                    Department.class
            );
            query.setParameter("name", departmentName.trim());
            List<Department> departments = query.getResultList();

            if (departments.isEmpty()) {
                throw new IllegalArgumentException("Không tìm thấy khoa có tên: " + departmentName);
            }

            student.setDepartment(departments.get(0));
        }
    }
}
