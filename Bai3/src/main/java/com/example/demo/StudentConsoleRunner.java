package com.example.demo;

import com.example.demo.dao.IStudentDao;
import com.example.demo.model.Department;
import com.example.demo.model.Student;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class StudentConsoleRunner implements CommandLineRunner {

    @Autowired
    private StudentService studentService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private IStudentDao studentDao;

    @Override
    public void run(String... args) throws Exception {
        System.out.println();
        System.out.println("==========================================================================================");
        System.out.println("   [JAVA SPRING BOOT] HỆ THỐNG QUẢN LÝ ĐÀO TẠO & SINH VIÊN");
        System.out.println("   KIẾN TRÚC DOANH NGHIỆP: Controller (rest) -> Service -> DAO (dao/impl) -> Hibernate/MySQL");
        System.out.println("   CÁC BẢNG DB: departments | students | class_info");
        System.out.println("==========================================================================================");

        try {
            // Khởi tạo phòng ban/khoa nếu trống
            if (departmentService.getAll().isEmpty()) {
                departmentService.create(new Department("CNTT", "Cong nghe thong tin", "Khoa Cong nghe thong tin"));
                departmentService.create(new Department("KHMT", "Khoa hoc may tinh", "Khoa Khoa hoc may tinh"));
                departmentService.create(new Department("QTKD", "Quan tri kinh doanh", "Khoa Quan tri kinh doanh"));
            }

            // In danh sách sinh viên hiện có ra Console dưới dạng bảng
            List<Student> students = studentService.getAll();
            System.out.println("\n[CONSOLE - DATABASE REALTIME] DANH SÁCH SINH VIÊN TRONG MYSQL:");
            System.out.println("+------+-------------------------+------------+-------------------------+-------------------------+");
            System.out.printf("| %-4s | %-23s | %-10s | %-23s | %-23s |\n", "ID", "STUDENT NAME", "DOB", "DEPARTMENT", "EMAIL");
            System.out.println("+------+-------------------------+------------+-------------------------+-------------------------+");
            for (Student s : students) {
                String deptName = s.getDepartment() != null ? s.getDepartment().getDepartmentName() : "Khoa #" + s.getDepartmentId();
                System.out.printf("| %-4d | %-23s | %-10s | %-23s | %-23s |\n",
                        s.getStudentId(),
                        s.getStudentName() != null ? s.getStudentName() : "",
                        s.getDob() != null ? s.getDob().toString() : "N/A",
                        deptName,
                        s.getEmail() != null ? s.getEmail() : "");
            }
            System.out.println("+------+-------------------------+------------+-------------------------+-------------------------+");

            System.out.println("\n[DAO & SERVICE CHECK] Kiểm tra truy vấn qua tầng StudentDaoImpl:");
            List<Student> itStudents = studentDao.findByDepartmentId(1L);
            System.out.printf("   -> Tìm thấy %d sinh viên thuộc department_id = 1 (CNTT)\n", itStudents.size());
            for (Student s : itStudents) {
                System.out.printf("      [ID: %d] %s (%s)\n", s.getStudentId(), s.getStudentName(), s.getEmail());
            }

        } catch (Exception e) {
            System.err.println("[CONSOLE WARNING] Chưa thể kết nối tới MySQL hoặc Database chưa sẵn sàng: " + e.getMessage());
        }

        System.out.println("\n[CONSOLE HƯỚNG DẪN TEST API]:");
        System.out.println("1. Sinh viên:       GET/POST/PUT/DELETE http://localhost:8080/students");
        System.out.println("2. Khoa/Ngành:      GET/POST/PUT/DELETE http://localhost:8080/departments");
        System.out.println("3. Lớp học:         GET/POST/PUT/DELETE http://localhost:8080/classes");
        System.out.println("==========================================================================================\n");
    }
}
