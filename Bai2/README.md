# BÀI TẬP 2: TÙY CHỈNH TRUY VẤN DỮ LIỆU BẰNG @QUERY (JPQL) TRONG SPRING DATA JPA

Dự án này là phần mở rộng từ **Bài 1**, tập trung vào việc tạo các câu truy vấn tùy chỉnh (**Custom Queries**) bằng ngôn ngữ truy vấn thực thể JPQL thông qua annotation `@Query` trong Repository, đồng thời gọi thực thi các câu truy vấn đó trong cả **Console Application (`CommandLineRunner`)** và **REST Controller**.

---

## 1. Yêu Cầu Của Bài Tập 2
1. Viết câu truy vấn tùy chỉnh trong `StudentRepository` bằng `@Query` (JPQL).
2. Gọi thực thi câu truy vấn trong lớp (Class):
   - **Cách 1: Trong `StudentConsoleRunner` (`CommandLineRunner`)**: Chạy và in kết quả trực tiếp ra màn hình Console khi khởi động ứng dụng.
   - **Cách 2: Trong `StudentController` (`@RestController`)**: Cung cấp API endpoint để gọi kiểm tra qua trình duyệt hoặc Postman.

---

## 2. Chi Tiết Thực Hiện

### 2.1. Định nghĩa `@Query` trong `StudentRepository.java`
```java
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // 1. Query tìm sinh viên theo đúng tên khoa (không phân biệt hoa thường)
    @Query("SELECT s FROM Student s WHERE LOWER(s.department) = LOWER(:department)")
    List<Student> findByDepartmentCustom(@Param("department") String department);

    // 2. Query tìm kiếm theo từ khóa xuất hiện trong Tên hoặc Khoa
    @Query("SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.department) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Student> searchByNameOrDepartmentCustom(@Param("keyword") String keyword);
}
```

### 2.2. Gọi Query trong `StudentConsoleRunner.java`
Khi chạy `mvnw spring-boot:run`, console sẽ tự động thực hiện truy vấn và in kết quả:
```java
List<Student> itStudents = studentRepository.findByDepartmentCustom("Cong nghe thong tin");
for (Student s : itStudents) {
    System.out.printf("   [KẾT QUẢ TÌM THẤY] ID: %-2d | Tên: %-20s | Khoa: %-20s | Ngày sinh: %s\n",
            s.getId(), s.getName(), s.getDepartment(), s.getDob());
}
```

### 2.3. Gọi Query trong `StudentController.java`
```java
@GetMapping("/custom/by-department")
public List<Student> getStudentsByDepartmentCustom(@RequestParam String department) {
    return studentRepository.findByDepartmentCustom(department);
}

@GetMapping("/custom/search")
public List<Student> searchStudentsCustom(@RequestParam String keyword) {
    return studentRepository.searchByNameOrDepartmentCustom(keyword);
}
```

---

## 3. Hướng Dẫn Chạy & Kiểm Thử

### Khởi động ứng dụng:
```bash
$env:JAVA_HOME="C:\Users\Admin\.jdks\openjdk-26.0.2.1"
.\mvnw.cmd spring-boot:run
```

### Test API Postman / Trình duyệt:
1. **Tìm sinh viên theo khoa**:
   - `GET http://localhost:8080/students/custom/by-department?department=Cong nghe thong tin`
2. **Tìm kiếm theo từ khóa tên hoặc khoa**:
   - `GET http://localhost:8080/students/custom/search?keyword=Cong`
3. **Các API CRUD cơ bản**:
   - `GET http://localhost:8080/students`
   - `POST http://localhost:8080/students`
   - `PUT http://localhost:8080/students/1`
   - `DELETE http://localhost:8080/students/1`
