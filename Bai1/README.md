# BÀI 1: HỆ THỐNG QUẢN LÝ SINH VIÊN (JAVA SPRING BOOT + MYSQL + MAVEN)

> **Môn học:** Phát triển ứng dụng Web  
> **Sinh viên:** Nguyễn Duy Trường  
> **Công nghệ:** Java 17+, Spring Boot, Spring Data JPA, MySQL, Maven, Postman

---

## 1. Yêu Cầu Đề Bài (Bài 1)
- Xây dựng một ứng dụng Java chạy bằng Spring Boot với trình quản lý Maven.
- Kết nối cơ sở dữ liệu MySQL (không dùng PostgreSQL).
- Bảng sinh viên gồm các trường:
  - `id`: Mã sinh viên (Khóa chính, tự động tăng).
  - `name`: Tên sinh viên (*Student Name*).
  - `dob`: Ngày sinh (*Date of Birth*, định dạng `yyyy-MM-dd`).
  - `department`: Khoa / Ngành (*Department*).
  - `email`: Địa chỉ email.
- Chế độ Console: Sử dụng `CommandLineRunner` in ra bảng danh sách sinh viên trực quan trên màn hình terminal khi khởi động.
- Hỗ trợ REST API CRUD (`GET`, `POST`, `PUT`, `DELETE`) giao tiếp dữ liệu dạng JSON qua Postman.
- Giao diện Web HTML/Bootstrap kiểm tra trực tiếp.

---

## 2. Cấu Trúc Thư Mục Bài 1

```text
Bai1/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java         # Điểm khởi chạy Spring Boot
│   │   │   ├── Student.java                 # Entity mô hình sinh viên
│   │   │   ├── StudentRepository.java       # Tầng JPA tương tác CSDL MySQL
│   │   │   ├── StudentController.java       # REST Controller xử lý JSON API
│   │   │   └── StudentConsoleRunner.java    # CommandLineRunner in bảng Console
│   │   └── resources/
│   │       ├── application.properties       # Cấu hình kết nối MySQL
│   │       └── static/index.html            # Giao diện Web hiển thị realtime
│   └── test/
├── pom.xml                                  # File cấu hình thư viện Maven
├── database.sql                             # File script tạo database và bảng
├── postman_collection.json                  # File import kiểm thử API trên Postman
└── README.md                                # Hướng dẫn chi tiết Bài 1
```

---

## 3. Cấu Hình MySQL (`application.properties`)

```properties
spring.application.name=demo

# Cấu hình MySQL (tự động tạo database demo_db nếu chưa có)
spring.datasource.url=jdbc:mysql://localhost:3306/demo_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=Asia/Ho_Chi_Minh&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=123456

# Cấu hình Hibernate JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Cổng khởi chạy
server.port=8080
```

---

## 4. Cách Chạy Ứng Dụng

Mở PowerShell tại thư mục `Bai1` và chạy lệnh:

```powershell
$env:JAVA_HOME="C:\Users\Admin\.jdks\openjdk-26.0.2.1"
.\mvnw.cmd spring-boot:run
```

Khi chạy, Console sẽ in ra bảng danh sách sinh viên:
```text
==========================================================================================
   [BÀI 1: JAVA SPRING BOOT] HỆ THỐNG QUẢN LÝ SINH VIÊN (MAVEN + MYSQL)
   CÁC TRƯỜNG DB: ID | STUDENT NAME | DOB (DATE OF BIRTH) | DEPARTMENT | EMAIL
==========================================================================================
[CONSOLE - DATABASE REALTIME] DANH SÁCH SINH VIÊN TRONG MYSQL:
+------+-------------------------+------------+-------------------------+-------------------------+
| ID   | STUDENT NAME            | DOB        | DEPARTMENT              | EMAIL                   |
+------+-------------------------+------------+-------------------------+-------------------------+
| 1    | Nguyen Van A            | 2003-05-15 | Cong nghe thong tin     | vana@gmail.com          |
| 2    | Tran Thi B              | 2004-08-20 | Khoa hoc may tinh       | thib@gmail.com          |
| 3    | Le Van C                | 2002-11-10 | Quan tri kinh doanh     | vanc@gmail.com          |
+------+-------------------------+------------+-------------------------+-------------------------+
```

---

## 5. Danh Sách Endpoint REST API (Test qua Postman)

*(Import file `postman_collection.json` vào Postman để kiểm thử nhanh)*

1. **GET All Students:** `GET http://localhost:8080/students`
   - Sắp xếp theo tên: `GET http://localhost:8080/students?sortBy=name&direction=asc`
   - Lọc theo khoa: `GET http://localhost:8080/students?department=Cong nghe thong tin`
2. **GET Student by ID:** `GET http://localhost:8080/students/{id}`
3. **POST Create Student (JSON):** `POST http://localhost:8080/students`
   ```json
   {
     "name": "Nguyen Van A",
     "dob": "2003-05-15",
     "department": "Cong nghe thong tin",
     "email": "vana@gmail.com"
   }
   ```
4. **PUT Update Student (JSON):** `PUT http://localhost:8080/students/{id}`
5. **DELETE Student:** `DELETE http://localhost:8080/students/{id}`
