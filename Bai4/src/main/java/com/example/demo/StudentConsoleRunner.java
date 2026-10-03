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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        System.out.println("   [BÀI 4 - SPRING BOOT] KIẾN TRÚC HIBERNATE DAO: PHÂN TRANG (PAGINATION) & SẮP XẾP (SORTING)");
        System.out.println("   CÁC CLASS TRONG hibernateDao: HibernateGenericDao, HibernateStudentDao, HibernateDepartmentDao, HibernateUserDao");
        System.out.println("==========================================================================================");

        try {
            // 1. Khởi tạo dữ liệu mẫu cho Khoa nếu trống
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

            // 2. Khởi tạo dữ liệu mẫu cho User nếu trống
            if (userRepository.count() == 0) {
                System.out.println("[CONSOLE] Khởi tạo dữ liệu mẫu cho bảng 'users'...");
                userRepository.save(new User("admin", "123456", "admin@school.edu.vn", "Quan Tri Vien", "ADMIN"));
                userRepository.save(new User("gv_nam", "123456", "nam@school.edu.vn", "Nguyen Van Nam", "USER"));
            }

            // 3. Khởi tạo dữ liệu mẫu cho Student nếu trống
            if (studentService.getAllStudents().isEmpty()) {
                System.out.println("[CONSOLE] Khởi tạo dữ liệu mẫu cho bảng 'students'...");
                studentService.createStudent(new Student("Nguyen Van A", LocalDate.of(2003, 5, 15), "vana@gmail.com", cntt));
                studentService.createStudent(new Student("Tran Thi B", LocalDate.of(2004, 8, 20), "thib@gmail.com", khmt));
                studentService.createStudent(new Student("Le Van C", LocalDate.of(2002, 11, 10), "vanc@gmail.com", cntt));
                studentService.createStudent(new Student("Pham Van D", LocalDate.of(2003, 1, 25), "vand@gmail.com", khmt));
                studentService.createStudent(new Student("Hoang Thi E", LocalDate.of(2004, 3, 18), "thie@gmail.com", cntt));
            }

            // 4. DEMO SORTING (Theo bài viết Baeldung 1: Spring Data Sorting)
            System.out.println("\n[DEMO BAELDUNG 1 - SORTING] DANH SÁCH SINH VIÊN SẮP XẾP THEO TÊN (A -> Z):");
            List<Student> sortedList = studentService.getStudentsSorted(Sort.by("studentName").ascending());
            for (Student s : sortedList) {
                System.out.printf("   ID: %-2d | Tên: %-20s | Khoa: %-20s | Ngày sinh: %s\n",
                        s.getStudentId(), s.getStudentName(),
                        s.getDepartment() != null ? s.getDepartment().getDepartmentName() : "N/A",
                        s.getDob());
            }

            // 5. DEMO PAGINATION (Theo bài viết Baeldung 2: Pagination and Sorting)
            System.out.println("\n[DEMO BAELDUNG 2 - PAGINATION] PHÂN TRANG (Trang 0, Kích thước 2 sinh viên/trang):");
            Page<Student> page0 = studentService.getStudentsPaged(PageRequest.of(0, 2, Sort.by("studentId").ascending()));
            System.out.printf("   Tổng số sinh viên: %d | Tổng số trang: %d | Trang hiện tại: %d\n",
                    page0.getTotalElements(), page0.getTotalPages(), page0.getNumber());
            for (Student s : page0.getContent()) {
                System.out.printf("   -> [Trang 0] ID: %-2d | Tên: %-20s | Email: %s\n",
                        s.getStudentId(), s.getStudentName(), s.getEmail());
            }

        } catch (Exception e) {
            System.err.println("[CONSOLE WARNING] Chưa thể kết nối tới MySQL: " + e.getMessage());
        }

        System.out.println("\n[CÁC API TEST POSTMAN / BROWSER THEO CHUẨN BAELDUNG]:");
        System.out.println("1. Web UI:                       http://localhost:8080");
        System.out.println("2. SẮP XẾP (Sorting):            GET http://localhost:8080/students/sort?sortBy=studentName&direction=asc");
        System.out.println("3. PHÂN TRANG (Pagination):      GET http://localhost:8080/students/page?page=0&size=2&sortBy=studentId&direction=asc");
        System.out.println("4. PHÂN TRANG THEO KHOA:         GET http://localhost:8080/students/page-by-department?department=Cong nghe thong tin&page=0&size=2");
        System.out.println("5. CRUD Sinh viên:               GET/POST/PUT/DELETE http://localhost:8080/students");
        System.out.println("6. Quản lý Users:                GET/POST/PUT/DELETE http://localhost:8080/users");
        System.out.println("==========================================================================================\n");
    }
}
