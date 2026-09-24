package com.example.demo;

import com.example.demo.dao.IStudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class StudentConsoleRunner implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private IStudentDao studentDao;

    @Override
    public void run(String... args) throws Exception {
        System.out.println();
        System.out.println("==========================================================================================");
        System.out.println("   [JAVA SPRING BOOT] HỆ THỐNG QUẢN LÝ SINH VIÊN (MAVEN + MYSQL)");
        System.out.println("   CÁC TRƯỜNG DB: ID | STUDENT NAME | DOB (DATE OF BIRTH) | DEPARTMENT | EMAIL");
        System.out.println("   KIẾN TRÚC: Spring Data JPA + Hibernate Generic DAO Pattern (dao & hibernatedao)");
        System.out.println("==========================================================================================");

        try {
            // Khởi tạo dữ liệu mẫu nếu DB trống
            if (studentRepository.count() == 0) {
                System.out.println("[CONSOLE] Database đang trống. Đang tự động thêm dữ liệu mẫu...");
                studentRepository.save(new Student("Nguyen Van A", LocalDate.of(2003, 5, 15), "Cong nghe thong tin", "vana@gmail.com"));
                studentRepository.save(new Student("Tran Thi B", LocalDate.of(2004, 8, 20), "Khoa hoc may tinh", "thib@gmail.com"));
                studentRepository.save(new Student("Le Van C", LocalDate.of(2002, 11, 10), "Quan tri kinh doanh", "vanc@gmail.com"));
                System.out.println("[CONSOLE] Đã thêm thành công 3 sinh viên mẫu vào MySQL!");
            }

            // In danh sách sinh viên hiện có ra Console dưới dạng bảng
            List<Student> students = studentRepository.findAll();
            System.out.println("\n[CONSOLE - DATABASE REALTIME] DANH SÁCH SINH VIÊN TRONG MYSQL:");
            System.out.println("+------+-------------------------+------------+-------------------------+-------------------------+");
            System.out.printf("| %-4s | %-23s | %-10s | %-23s | %-23s |\n", "ID", "STUDENT NAME", "DOB", "DEPARTMENT", "EMAIL");
            System.out.println("+------+-------------------------+------------+-------------------------+-------------------------+");
            for (Student s : students) {
                System.out.printf("| %-4d | %-23s | %-10s | %-23s | %-23s |\n",
                        s.getId(),
                        s.getName() != null ? s.getName() : "",
                        s.getDob() != null ? s.getDob().toString() : "N/A",
                        s.getDepartment() != null ? s.getDepartment() : "",
                        s.getEmail() != null ? s.getEmail() : "");
            }
            System.out.println("+------+-------------------------+------------+-------------------------+-------------------------+");

            // =====================================================================
            // BÀI 2: GỌI CÂU QUERY TÙY CHỈNH (CUSTOM QUERY) TRONG LỚP CONSOLE RUNNER
            // =====================================================================
            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println("   [BÀI 2 DEMO] GỌI QUERY TÙY CHỈNH: Tìm kiếm sinh viên theo Khoa 'Cong nghe thong tin'");
            System.out.println("   -> Câu lệnh JPQL: SELECT s FROM Student s WHERE LOWER(s.department) = LOWER(:department)");
            System.out.println("------------------------------------------------------------------------------------------");
            List<Student> itStudents = studentRepository.findByDepartmentCustom("Cong nghe thong tin");
            if (itStudents.isEmpty()) {
                System.out.println("   (Chưa có sinh viên nào thuộc khoa này)");
            } else {
                for (Student s : itStudents) {
                    System.out.printf("   [KẾT QUẢ TÌM THẤY] ID: %-2d | Tên: %-20s | Khoa: %-20s | Ngày sinh: %s\n",
                            s.getId(), s.getName(), s.getDepartment(), s.getDob());
                }
            }
            System.out.println("------------------------------------------------------------------------------------------");

        } catch (Exception e) {
            System.err.println("[CONSOLE WARNING] Chưa thể kết nối tới MySQL hoặc Database chưa sẵn sàng: " + e.getMessage());
            System.err.println("[CONSOLE HƯỚNG DẪN] Hãy kiểm tra MySQL (XAMPP/MySQL Server) đã chạy và cấu hình đúng trong application.properties!");
        }

        System.out.println("\n[CONSOLE HƯỚNG DẪN KIỂM TRA & TEST POSTMAN]:");
        System.out.println("1. Web UI:         http://localhost:8080");
        System.out.println("2. GET ALL:        GET    http://localhost:8080/students");
        System.out.println("3. GET BY ID:      GET    http://localhost:8080/students/1");
        System.out.println("4. POST (JSON):    POST   http://localhost:8080/students");
        System.out.println("   Body JSON mẫu:  {\"name\":\"Pham Van D\",\"dob\":\"2003-12-01\",\"department\":\"Ke toan\",\"email\":\"vand@gmail.com\"}");
        System.out.println("5. PUT (JSON):     PUT    http://localhost:8080/students/1");
        System.out.println("6. DELETE:         DELETE http://localhost:8080/students/1");
        System.out.println("7. BÀI 2 QUERY 1:  GET    http://localhost:8080/students/custom/by-department?department=Cong nghe thong tin");
        System.out.println("8. BÀI 2 QUERY 2:  GET    http://localhost:8080/students/custom/search?keyword=Cong");
        System.out.println("==========================================================================================\n");
    }
}
