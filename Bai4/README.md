# BÀI 4: KIẾN TRÚC DOANH NGHIỆP TOÀN DIỆN (DAO/IMPL, SERVICE/IMPL, HIBERNATEDAO, REST, MODEL)

> **Môn học:** Phát triển ứng dụng Web  
> **Sinh viên:** Nguyễn Duy Trường  
> **Công nghệ:** Java 17+, Spring Boot, Spring Data JPA, Hibernate, MySQL, Maven, Postman

---

## 1. Cấu Trúc Mã Nguồn Chuẩn Doanh Nghiệp (Theo Yêu Cầu Giảng Viên)

Dự án đã được tái cấu trúc hoàn chỉnh theo chuẩn kiến trúc phân tầng chuyên nghiệp:

```text
Bai4/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java               # Khởi chạy Spring Boot
│   │   │   ├── StudentConsoleRunner.java          # In log, seed dữ liệu & kiểm tra hệ thống
│   │   │   │
│   │   │   ├── model/                             # [Thực thể CSDL / Entity]
│   │   │   │   ├── Department.java                # Bảng 'departments' (department_id, department_code, department_name, description, created_at, updated_at)
│   │   │   │   ├── Student.java                   # Bảng 'students' (student_id, student_name, dob, email, department_id, created_at, updated_at)
│   │   │   │   └── ClassInfo.java                 # Bảng 'class_info' (class_id, class_code, class_name, description, created_at, updated_at)
│   │   │   │
│   │   │   ├── dao/                               # [Tầng Interface DAO]
│   │   │   │   ├── IGenericDao.java               # Interface DAO đa năng
│   │   │   │   ├── IDepartmentDao.java            # Interface DAO cho Department
│   │   │   │   ├── IStudentDao.java               # Interface DAO cho Student
│   │   │   │   ├── IClassInfoDao.java             # Interface DAO cho ClassInfo
│   │   │   │   └── impl/                          # [Triển khai DAO kế thừa HibernateGenericDao]
│   │   │   │       ├── DepartmentDaoImpl.java
│   │   │   │       ├── StudentDaoImpl.java
│   │   │   │       └── ClassInfoDaoImpl.java
│   │   │   │
│   │   │   ├── hibernateDao/                      # [Tầng Hibernate Core dùng chung]
│   │   │   │   └── HibernateGenericDao.java       # Class cha triển khai SimpleJpaRepository & null-check, logger.warn
│   │   │   │
│   │   │   ├── service/                           # [Tầng Interface Service Nghiệp Vụ]
│   │   │   │   ├── DepartmentService.java
│   │   │   │   ├── StudentService.java
│   │   │   │   ├── ClassInfoService.java
│   │   │   │   └── impl/                          # [Triển khai Service]
│   │   │   │       ├── DepartmentServiceImpl.java
│   │   │   │       ├── StudentServiceImpl.java
│   │   │   │       └── ClassInfoServiceImpl.java
│   │   │   │
│   │   │   └── rest/                              # [Tầng Controller / RESTful API]
│   │   │       ├── DepartmentController.java      # Quản lý Khoa / Phòng ban (/departments)
│   │   │       ├── StudentController.java         # Quản lý Sinh viên (/students)
│   │   │       └── ClassInfoController.java       # Quản lý Lớp học (/classes)
│   │   │
│   │   └── resources/
│   │       ├── application.properties             # Kết nối MySQL (demo_db)
│   │       └── static/index.html                  # Giao diện Web hiển thị realtime
│   └── test/
├── pom.xml                                        # Cấu hình Maven
├── database.sql                                   # Script tạo 3 bảng MySQL theo đúng schema
└── README.md
```

---

## 2. Các Bảng Trong MySQL (Database Schema)

### 2.1. Bảng `departments`
- `department_id` BIGINT AUTO_INCREMENT PRIMARY KEY
- `department_code` VARCHAR(20)
- `department_name` VARCHAR(100)
- `description` VARCHAR(255)
- `created_at` DATETIME(6)
- `updated_at` DATETIME(6)

### 2.2. Bảng `students`
- `student_id` BIGINT AUTO_INCREMENT PRIMARY KEY
- `student_name` VARCHAR(100)
- `dob` DATE
- `email` VARCHAR(100)
- `department_id` BIGINT (Khóa ngoại trỏ về `departments`)
- `created_at` DATETIME(6)
- `updated_at` DATETIME(6)

### 2.3. Bảng `class_info`
- `class_id` BIGINT AUTO_INCREMENT PRIMARY KEY
- `class_code` VARCHAR(20)
- `class_name` VARCHAR(100)
- `description` VARCHAR(255)
- `created_at` DATETIME(6)
- `updated_at` DATETIME(6)

---

## 3. Khởi Chạy Ứng Dụng

Tại thư mục `Bai4`:
```powershell
$env:JAVA_HOME="C:\Users\Admin\.jdks\openjdk-26.0.2.1"
.\mvnw.cmd spring-boot:run
```

---

## 4. Danh Sách Endpoint RESTful API

1. **Quản lý Sinh viên:**
   - Lấy danh sách: `GET http://localhost:8080/students`
   - Tìm theo tên: `GET http://localhost:8080/students?name=Nguyen`
   - Tìm theo khoa: `GET http://localhost:8080/students?departmentId=1`
   - Thêm sinh viên: `POST http://localhost:8080/students`
   - Cập nhật sinh viên: `PUT http://localhost:8080/students/{id}`
   - Xóa sinh viên: `DELETE http://localhost:8080/students/{id}`

2. **Quản lý Khoa / Ngành:**
   - Lấy danh sách: `GET http://localhost:8080/departments`
   - Thêm khoa: `POST http://localhost:8080/departments`
   - Cập nhật khoa: `PUT http://localhost:8080/departments/{id}`
   - Xóa khoa: `DELETE http://localhost:8080/departments/{id}`

3. **Quản lý Lớp học:**
   - Lấy danh sách: `GET http://localhost:8080/classes`
   - Thêm lớp: `POST http://localhost:8080/classes`
