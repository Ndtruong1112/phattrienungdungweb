package com.example.demo.controller;

import com.example.demo.entity.Department;
import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private StudentService studentService;

    @PersistenceContext
    private EntityManager entityManager;

    // 1. GET ALL hoặc tìm kiếm theo khoa/tên
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

    // =========================================================================
    // 2. SẮP XẾP KẾT QUẢ (SORTING - Theo bài viết Baeldung 1: Spring Data Sorting)
    // Ví dụ: GET /students/sort?sortBy=studentName&direction=asc
    // =========================================================================
    @GetMapping("/sort")
    public List<Student> getStudentsSorted(
            @RequestParam(defaultValue = "studentId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        return studentService.getStudentsSorted(sort);
    }

    // =========================================================================
    // 3. PHÂN TRANG & SẮP XẾP (PAGINATION & SORTING - Theo bài viết Baeldung 2)
    // Ví dụ: GET /students/page?page=0&size=5&sortBy=studentName&direction=asc
    // =========================================================================
    @GetMapping("/page")
    public Page<Student> getStudentsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "studentId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return studentService.getStudentsPaged(pageable);
    }

    // 4. PHÂN TRANG THEO KHOA
    // Ví dụ: GET /students/page-by-department?department=Cong nghe thong tin&page=0&size=5
    @GetMapping("/page-by-department")
    public Page<Student> getStudentsByDepartmentPaged(
            @RequestParam String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return studentService.getStudentsByDepartmentPaged(department, pageable);
    }

    // 5. GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 6. POST (Tạo sinh viên)
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

    // 7. PUT (Cập nhật sinh viên)
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

    // 8. DELETE (Xóa sinh viên)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        Optional<Student> existing = studentService.getStudentById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Đã xóa thành công sinh viên có id: " + id);
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
