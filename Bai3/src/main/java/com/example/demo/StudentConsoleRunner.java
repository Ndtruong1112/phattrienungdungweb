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
    private IStudentDao studentDao;

    @Override
    public void run(String... args) throws Exception {
        System.out.println();
        System.out.println("==========================================================================================");
        System.out.println("   [JAVA SPRING BOOT] HỆ THỐNG QUẢN LÝ SINH VIÊN (MAVEN + MYSQL)");
        System.out.println("   BÀI TẬP 3: MÔ HÌNH DAO & HIBERNATE DAO (GENERIC DAO PATTERN)");
        System.out.println("   CẤU TRÚC: IGenericDao -> HibernateGenericDao | IStudentDao -> HibernateStudentDao");
        System.out.println("==========================================================================================");

        try {
            // Khởi tạo dữ liệu mẫu qua DAO nếu DB trống
            if (studentDao.findAll().isEmpty()) {
                System.out.println("[CONSOLE DAO] Database đang trống. Đang tự động thêm dữ liệu mẫu qua studentDao.create()...");
                studentDao.create(new Student("Nguyen Van A", LocalDate.of(2003, 5, 15), "Cong nghe thong tin", "vana@gmail.com"));
                studentDao.create(new Student("Tran Thi B", LocalDate.of(2004, 8, 20), "Khoa hoc may tinh", "thib@gmail.com"));
                studentDao.create(new Student("Le Van C", LocalDate.of(2002, 11, 10), "Quan tri kinh doanh", "vanc@gmail.com"));
                System.out.println("[CONSOLE DAO] Đã thêm thành công 3 sinh viên mẫu qua HibernateGenericDao!");
            }

            // In danh sách sinh viên hiện có bằng studentDao.findAll()
            List<Student> students = studentDao.findAll();
            System.out.println("\n[CONSOLE - DATABASE REALTIME] DANH SÁCH SINH VIÊN (LẤY TỪ studentDao.findAll()):");
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
            // BÀI 3: DEMO CÁC PHƯƠNG THỨC TRUY VẤN TỪ HibernateStudentDao
            // =====================================================================
            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println("   [BÀI 3 DEMO] GỌI studentDao.findByDepartment('Cong nghe thong tin'):");
            List<Student> itStudents = studentDao.findByDepartment("Cong nghe thong tin");
            if (itStudents.isEmpty()) {
                System.out.println("   (Chưa có sinh viên nào thuộc khoa này)");
            } else {
                for (Student s : itStudents) {
                    System.out.printf("   [TÌM THẤY] ID: %-2d | Tên: %-20s | Khoa: %-20s | Email: %s\n",
                            s.getId(), s.getName(), s.getDepartment(), s.getEmail());
                }
            }
            System.out.println("------------------------------------------------------------------------------------------");

        } catch (Exception e) {
            System.err.println("[CONSOLE WARNING] Chưa thể kết nối tới MySQL hoặc Database chưa sẵn sàng: " + e.getMessage());
            System.err.println("[CONSOLE HƯỚNG DẪN] Hãy kiểm tra MySQL (XAMPP/MySQL Server) đã chạy và cấu hình đúng trong application.properties!");
        }

        System.out.println("\n[CONSOLE HƯỚNG DẪN KIỂM TRA & TEST POSTMAN - BÀI 3 DAO]:");
        System.out.println("1. Web UI:         http://localhost:8080");
        System.out.println("2. GET ALL (DAO):  GET    http://localhost:8080/students");
        System.out.println("3. GET BY ID:      GET    http://localhost:8080/students/1");
        System.out.println("4. POST (create):  POST   http://localhost:8080/students");
        System.out.println("   Body JSON mẫu:  {\"name\":\"Pham Van D\",\"dob\":\"2003-12-01\",\"department\":\"Ke toan\",\"email\":\"vand@gmail.com\"}");
        System.out.println("5. PUT (update):   PUT    http://localhost:8080/students/1");
        System.out.println("6. DELETE:         DELETE http://localhost:8080/students/1");
        System.out.println("7. DAO BY DEPT:    GET    http://localhost:8080/students/dao/by-department?department=Cong nghe thong tin");
        System.out.println("8. DAO BY NAME:    GET    http://localhost:8080/students/dao/by-name?name=Nguyen");
        System.out.println("==========================================================================================\n");
    }
}
