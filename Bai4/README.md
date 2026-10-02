# BÀI 4: KIẾN TRÚC KẾT HỢP HIBERNATE GENERIC DAO VÀ SPRING DATA JPA REPOSITORY

> **Môn học:** Phát triển ứng dụng Web  
> **Sinh viên:** Nguyễn Duy Trường  
> **Công nghệ:** Java 17+, Spring Boot, Spring Data JPA, Hibernate, MySQL, Maven, Postman  

---

## 1. Cấu Trúc Dự Án Hoàn Chỉnh

Dự án được cấu trúc phân tầng chuẩn mực theo cấu trúc doanh nghiệp kết hợp 2 kỹ thuật truy cập dữ liệu trong cùng một ứng dụng:

```text
Bai4/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java               # Khởi chạy Spring Boot
│   │   │   ├── StudentConsoleRunner.java          # In bảng dữ liệu Console, khởi tạo mẫu
│   │   │   │
│   │   │   ├── controller/                        # [Tầng REST Controller]
│   │   │   │   ├── StudentController.java         # API sinh viên + hàm resolveDepartment()
│   │   │   │   └── UserController.java            # API người dùng (User)
│   │   │   │
│   │   │   ├── dao/                               # [Tầng DAO Interface & Impl]
│   │   │   │   ├── IGenericDao.java               # Generic DAO Interface
│   │   │   │   ├── IStudentDao.java               # DAO Interface cho Student
│   │   │   │   └── impl/
│   │   │   │       └── StudentDaoImpl.java        # Triển khai DAO kế thừa HibernateGenericDao
│   │   │   │
│   │   │   ├── entity/                            # [Tầng JPA Entities]
│   │   │   │   ├── Department.java                # Bảng 'departments'
│   │   │   │   ├── Student.java                   # Bảng 'students' (liên kết Department)
│   │   │   │   └── User.java                      # Bảng 'users'
│   │   │   │
│   │   │   ├── hibernateDao/                      # [Tầng Hibernate Core]
│   │   │   │   └── HibernateGenericDao.java       # Generic DAO kế thừa SimpleJpaRepository
│   │   │   │
│   │   │   ├── repository/                        # [Tầng Spring Data JPA Repository]
│   │   │   │   └── UserRepository.java            # Kế thừa JpaRepository<User, Long>
│   │   │   │
│   │   │   └── service/                           # [Tầng Business Service]
│   │   │       ├── StudentService.java            # Service Interface
│   │   │       └── impl/
│   │   │           └── StudentServiceImpl.java    # Service Impl gọi qua DAO
│   │   │
│   │   └── resources/
│   │       ├── application.properties             # Cấu hình kết nối MySQL (demo_db)
│   │       └── static/index.html                  # Giao diện Web hiển thị realtime
│   └── test/
├── pom.xml                                        # Cấu hình Maven
├── database.sql                                   # Script tạo các bảng CSDL
└── README.md
```

---

## 2. Điểm Nhấn Kiến Trúc Kỹ Thuật

### 2.1. Kết Hợp Song Song 2 Mô Hình Data Access
1. **Thực thể `Student` (Mô hình DAO & Hibernate DAO):**
   - Định nghĩa `IGenericDao` và `IStudentDao`.
   - `StudentDaoImpl` kế thừa `HibernateGenericDao` sử dụng `EntityManager` để thực thi truy vấn HQL/JPQL và kế thừa các hàm CRUD cơ bản.
   - Tầng Controller gọi qua `StudentService` -> `StudentServiceImpl` -> `StudentDaoImpl`.
2. **Thực thể `User` (Mô hình Spring Data JPA Repository):**
   - `UserRepository` kế thừa trực tiếp từ `JpaRepository<User, Long>`.
   - Minh họa cách tiếp cận hiện đại của Spring Boot khi không cần tự viết lớp DAO thủ công.

### 2.2. Kiểm Tra Ràng Buộc Khóa Ngoại (`resolveDepartment`)
Trong `StudentController.java`, hàm `resolveDepartment(Student student)` tự động kiểm tra khoa được gán cho sinh viên:
- Nếu truyền theo ID: Tra cứu trực tiếp bằng `entityManager.find`.
- Nếu truyền theo tên khoa: Tra cứu bằng JPQL `TypedQuery<Department>`.
- Nếu khoa không tồn tại, lập tức ném ra ngoại lệ và trả về mã lỗi 400 Bad Request:
  ```java
  if (departments.isEmpty()) {
      throw new IllegalArgumentException("Không tìm thấy khoa có tên: " + departmentName);
  }
  ```

---

## 3. Hướng Dẫn Chạy & Kiểm Thử

### Khởi chạy:
```powershell
$env:JAVA_HOME="C:\Users\Admin\.jdks\openjdk-26.0.2.1"
.\mvnw.cmd spring-boot:run
```

### Danh Sách API Endpoints (Postman / Browser):

#### 1. Quản lý Sinh viên (`/students`)
- `GET http://localhost:8080/students`: Lấy tất cả sinh viên
- `GET http://localhost:8080/students/1`: Lấy sinh viên theo ID
- `GET http://localhost:8080/students/by-department?department=Cong nghe thong tin`: Tìm theo khoa
- `GET http://localhost:8080/students/search?name=Nguyen`: Tìm kiếm theo tên
- `POST http://localhost:8080/students`: Thêm mới sinh viên
  ```json
  {
    "studentName": "Nguyen Van D",
    "dob": "2003-12-01",
    "email": "vand@gmail.com",
    "department": {
      "departmentName": "Cong nghe thong tin"
    }
  }
  ```
- `PUT http://localhost:8080/students/1`: Cập nhật sinh viên
- `DELETE http://localhost:8080/students/1`: Xóa sinh viên

#### 2. Quản lý Người dùng (`/users`)
- `GET http://localhost:8080/users`: Lấy tất cả người dùng
- `GET http://localhost:8080/users/1`: Lấy người dùng theo ID
- `POST http://localhost:8080/users`: Thêm người dùng mới
  ```json
  {
    "username": "user_moi",
    "password": "secretpassword",
    "email": "moi@gmail.com",
    "fullName": "Tran Van Moi",
    "role": "USER"
  }
  ```
- `PUT http://localhost:8080/users/1`: Cập nhật người dùng
- `DELETE http://localhost:8080/users/1`: Xóa người dùng
