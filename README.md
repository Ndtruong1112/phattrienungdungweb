# Môn Học: Phát Triển Ứng Dụng Web

Repository tổng hợp các bài tập, bài thực hành và dự án môn **Phát triển ứng dụng Web**.

- **Sinh viên thực hiện:** Nguyễn Duy Trường
- **Tài khoản GitHub:** [@Ndtruong1112](https://github.com/Ndtruong1112)
- **Công nghệ sử dụng:** Java, Spring Boot, Spring Data JPA, Hibernate, Maven, MySQL, RESTful API, Postman, Bootstrap

---

## 📑 Danh Mục Các Bài Tập

| Thư mục | Tên Bài | Nội Dung Tóm Tắt | Trạng Thái |
| :---: | :--- | :--- | :---: |
| [**`Bai1/`**](./Bai1) | **Bài 1: Quản Lý Sinh Viên Cơ Bản** | Ứng dụng Java Spring Boot kết nối MySQL, quản lý thông tin sinh viên (`id`, `name`, `dob`, `department`, `email`). Hiển thị bảng dữ liệu ra màn hình Console khi khởi động và cung cấp REST API đầy đủ CRUD (Postman/Web UI). | ✅ **Hoàn thành** |
| [**`Bai2/`**](./Bai2) | **Bài 2: Custom Query (@Query JPQL)** | Tùy biến truy vấn thực thể JPQL với annotation `@Query` trong `StudentRepository` (tìm theo khoa chính xác, tìm kiếm theo từ khóa tên hoặc khoa). Gọi thực thi trực tiếp trong `StudentConsoleRunner` và cung cấp API trong `StudentController`. | ✅ **Hoàn thành** |
| [**`Bai3/`**](./Bai3) | **Bài 3: Mô Hình DAO & Hibernate DAO** | Triển khai mô hình Generic DAO Pattern kinh điển: `IGenericDao` & `HibernateGenericDao` kế thừa `SimpleJpaRepository` dùng `EntityManager`. Giao diện `IStudentDao` và triển khai `HibernateStudentDao` phục vụ quản lý sinh viên. | ✅ **Hoàn thành** |
| [**`Bai4/`**](./Bai4) | **Bài 4: Kiến Trúc Doanh Nghiệp (DAO & Spring Data JPA)** | Cấu trúc phân tầng chuẩn mực kết hợp 2 kỹ thuật truy xuất dữ liệu: `Student` dùng Hibernate Generic DAO (`dao/impl/StudentDaoImpl` kế thừa `HibernateGenericDao`) và `User` dùng Spring Data JPA `UserRepository`. Tích hợp xử lý toàn vẹn khóa ngoại `resolveDepartment()` trong `StudentController`. | ✅ **Hoàn thành** |

---

## 🛠 Hướng Dẫn Chung Khi Mở Dự Án

1. Yêu cầu môi trường: **JDK 17 trở lên** và dịch vụ **MySQL Server** (cổng 3306, user `root`, password `123456`, database `demo_db`).
2. Di chuyển vào thư mục bài tập cần chạy (ví dụ `cd Bai1`, `cd Bai2`, `cd Bai3`, hoặc `cd Bai4`).
3. Khởi chạy ứng dụng:
   ```powershell
   $env:JAVA_HOME="C:\Users\Admin\.jdks\openjdk-26.0.2.1"
   .\mvnw.cmd spring-boot:run
   ```
4. Truy cập giao diện trực tiếp trên trình duyệt: `http://localhost:8080`.
