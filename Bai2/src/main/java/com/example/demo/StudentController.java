package com.example.demo;

import com.example.demo.dao.IStudentDao;
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

    @Autowired
    private IStudentDao studentDao;

    // 1. GET ALL, SEARCH & SORT
    // Ví dụ: GET /students
    // GET /students?sortBy=name&direction=asc
    // GET /students?department=Cong nghe thong tin
    // GET /students?name=Nguyen
    @GetMapping
    public List<Student> getAllStudents(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        if (department != null && !department.trim().isEmpty()) {
            return studentRepository.findByDepartmentCustom(department.trim());
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
    // Ví dụ: GET /students/1
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentDao.findById(id);
        return student.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. POST (Thêm mới sinh viên - Sử dụng HibernateGenericDao.create)
    // Ví dụ: POST /students
    // Body: { "name": "Nguyen Van A", "dob": "2003-05-15", "department": "Cong nghe thong tin", "email": "a@gmail.com" }
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student savedStudent = studentDao.create(student);
        return ResponseEntity.ok(savedStudent);
    }

    // 4. PUT / UPDATE (Cập nhật sinh viên - Sử dụng HibernateGenericDao.update)
    // Ví dụ: PUT /students/1
    // Body: { "name": "Nguyen Van A Cap Nhat", "dob": "2003-05-15", "department": "Khoa hoc may tinh", "email": "a_new@gmail.com" }
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        Optional<Student> optionalStudent = studentDao.findById(id);
        if (optionalStudent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student student = optionalStudent.get();
        student.setName(studentDetails.getName());
        student.setDob(studentDetails.getDob());
        student.setDepartment(studentDetails.getDepartment());
        student.setEmail(studentDetails.getEmail());

        Student updatedStudent = studentDao.update(student);
        return ResponseEntity.ok(updatedStudent);
    }

    // 5. DELETE (Xóa sinh viên theo ID)
    // Ví dụ: DELETE /students/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        studentRepository.deleteById(id);
        return ResponseEntity.ok("Đã xóa thành công sinh viên có id: " + id);
    }

    // ==========================================================
    // BÀI 2: GỌI CÂU QUERY TÙY CHỈNH TRONG LỚP (CLASS) CONTROLLER
    // ==========================================================

    // 6. Gọi Query 1: Lấy danh sách sinh viên theo Khoa (dùng @Query tùy chỉnh)
    // Ví dụ: GET /students/custom/by-department?department=Cong nghe thong tin
    @GetMapping("/custom/by-department")
    public List<Student> getStudentsByDepartmentCustom(@RequestParam String department) {
        return studentRepository.findByDepartmentCustom(department);
    }

    // 7. Gọi Query 2: Tìm kiếm theo từ khóa tên hoặc khoa (dùng @Query tùy chỉnh)
    // Ví dụ: GET /students/custom/search?keyword=Cong
    @GetMapping("/custom/search")
    public List<Student> searchStudentsCustom(@RequestParam String keyword) {
        return studentRepository.searchByNameOrDepartmentCustom(keyword);
    }
}
