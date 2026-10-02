package com.example.demo;

import com.example.demo.entity.Department;
import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.StudentService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class StudentConsoleRunner implements CommandLineRunner {

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println();
        System.out.println("==========================================================================================");
        System.out.println("   [BÀI 4 - SPRING BOOT] KIẾN TRÚC DOANH NGHIỆP: DAO + REPOSITORY + SERVICE + CONTROLLER");
        System.out.println("   THỰC THỂ: Department, Student, User | CƠ CHẾ: HibernateGenericDao & JpaRepository");
        System.out.println("==========================================================================================");

        try {
            // 1. Khởi tạo dữ liệu mẫu cho Khoa (Department) nếu chưa có
            List<Department> depts = entityManager.createQuery("SELECT d FROM Department d", Department.class).getResultList();
            Department cntt;
            Department khmt;
            if (depts.isEmpty()) {
                System.out.println("[CONSOLE] Khởi tạo dữ liệu mẫu cho bảng 'departments'...");
                cntt = new Department("CNTT", "Cong nghe thong tin", "Khoa Cong nghe thong tin");
                khmt = new Department("KHMT", "Khoa hoc may tinh", "Khoa Khoa hoc may tinh");
                Department qtkd = new Department("QTKD", "Quan tri kinh doanh", "Khoa Quan tri kinh doanh");
                entityManager.persist(cntt);
                entityManager.persist(khmt);
                entityManager.persist(qtkd);
                entityManager.flush();
            } else {
                cntt = depts.get(0);
                khmt = depts.size() > 1 ? depts.get(1) : cntt;
            }

            // 2. Khởi tạo dữ liệu mẫu cho User (Spring Data JPA)
            if (userRepository.count() == 0) {
                System.out.println("[CONSOLE] Khởi tạo dữ liệu mẫu cho bảng 'users'...");
                userRepository.save(new User("admin", "123456", "admin@school.edu.vn", "Quan Tri Vien", "ADMIN"));
                userRepository.save(new User("gv_nam", "123456", "nam@school.edu.vn", "Nguyen Van Nam", "USER"));
            }

            // 3. Khởi tạo dữ liệu mẫu cho Student (Hibernate Generic DAO)
            if (studentService.getAllStudents().isEmpty()) {
                System.out.println("[CONSOLE] Khởi tạo dữ liệu mẫu cho bảng 'students'...");
                studentService.createStudent(new Student("Nguyen Van A", LocalDate.of(2003, 5, 15), "vana@gmail.com", cntt));
                studentService.createStudent(new Student("Tran Thi B", LocalDate.of(2004, 8, 20), "thib@gmail.com", khmt));
                studentService.createStudent(new Student("Le Van C", LocalDate.of(2002, 11, 10), "vanc@gmail.com", cntt));
            }

            // 4. In bảng danh sách sinh viên
            List<Student> students = studentService.getAllStudents();
            System.out.println("\n[DATABASE REALTIME] DANH SÁCH SINH VIÊN (QUA DAO & SERVICE):");
            System.out.println("+------+-------------------------+------------+-------------------------+-------------------------+");
            System.out.printf("| %-4s | %-23s | %-10s | %-23s | %-23s |\n", "ID", "STUDENT NAME", "DOB", "DEPARTMENT", "EMAIL");
            System.out.println("+------+-------------------------+------------+-------------------------+-------------------------+");
            for (Student s : students) {
                String deptName = s.getDepartment() != null ? s.getDepartment().getDepartmentName() : "N/A";
                System.out.printf("| %-4d | %-23s | %-10s | %-23s | %-23s |\n",
                        s.getStudentId(),
                        s.getStudentName() != null ? s.getStudentName() : "",
                        s.getDob() != null ? s.getDob().toString() : "N/A",
                        deptName,
                        s.getEmail() != null ? s.getEmail() : "");
            }
            System.out.println("+------+-------------------------+------------+-------------------------+-------------------------+");

            // 5. In danh sách Users
            List<User> users = userRepository.findAll();
            System.out.println("\n[DATABASE REALTIME] DANH SÁCH USERS (QUA SPRING DATA JPA REPOSITORY):");
            System.out.println("+------+--------------------+--------------------+--------------------+");
            System.out.printf("| %-4s | %-18s | %-18s | %-18s |\n", "ID", "USERNAME", "FULL NAME", "ROLE");
            System.out.println("+------+--------------------+--------------------+--------------------+");
            for (User u : users) {
                System.out.printf("| %-4d | %-18s | %-18s | %-18s |\n",
                        u.getUserId(), u.getUsername(), u.getFullName(), u.getRole());
            }
            System.out.println("+------+--------------------+--------------------+--------------------+");

        } catch (Exception e) {
            System.err.println("[CONSOLE WARNING] Chưa thể kết nối tới MySQL: " + e.getMessage());
        }

        System.out.println("\n[HƯỚNG DẪN TEST API POSTMAN / BROWSER]:");
        System.out.println("1. Web UI:           http://localhost:8080");
        System.out.println("2. Sinh viên:        GET    http://localhost:8080/students");
        System.out.println("                     GET    http://localhost:8080/students/1");
        System.out.println("                     POST   http://localhost:8080/students");
        System.out.println("                     PUT    http://localhost:8080/students/1");
        System.out.println("                     DELETE http://localhost:8080/students/1");
        System.out.println("3. Tra cứu Khoa:     GET    http://localhost:8080/students/by-department?department=Cong nghe thong tin");
        System.out.println("4. Tìm kiếm Tên:     GET    http://localhost:8080/students/search?name=Nguyen");
        System.out.println("5. Quản lý Users:    GET    http://localhost:8080/users");
        System.out.println("                     POST   http://localhost:8080/users");
        System.out.println("                     DELETE http://localhost:8080/users/1");
        System.out.println("==========================================================================================\n");
    }
}
