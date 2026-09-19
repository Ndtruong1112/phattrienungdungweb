package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class StudentConsoleRunner implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println();
        System.out.println("==========================================================================================");
        System.out.println("   [BÀI 1: JAVA SPRING BOOT] HỆ THỐNG QUẢN LÝ SINH VIÊN (MAVEN + MYSQL)");
        System.out.println("   CÁC TRƯỜNG DB: ID | STUDENT NAME | DOB (DATE OF BIRTH) | DEPARTMENT | EMAIL");
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
        System.out.println("==========================================================================================\n");
    }
}
