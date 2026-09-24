# BÀI 2: TÙY BIẾN TRUY VẤN (@QUERY JPQL) & MÔ HÌNH HIBERNATE GENERIC DAO

> **Môn học:** Phát triển ứng dụng Web  
> **Sinh viên:** Nguyễn Duy Trường  
> **Công nghệ:** Java 17+, Spring Boot, Spring Data JPA, Hibernate, MySQL, Maven, Postman

---

## 1. Yêu Cầu & Nội Dung Bài 2

Bài 2 nâng cấp dự án từ Bài 1 với 2 nội dung chuyên sâu:

### 1.1. Viết Custom Query bằng JPQL trong `StudentRepository`
- **Query 1:** Tìm sinh viên theo Khoa (*Department*) không phân biệt chữ hoa / chữ thường:
  ```java
  @Query("SELECT s FROM Student s WHERE LOWER(s.department) = LOWER(:department)")
  List<Student> findByDepartmentCustom(@Param("department") String department);
  ```
- **Query 2:** Tìm kiếm đa năng theo từ khóa xuất hiện ở Tên (*name*) hoặc Khoa (*department*):
  ```java
  @Query("SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.department) LIKE LOWER(CONCAT('%', :keyword, '%'))")
  List<Student> searchByNameOrDepartmentCustom(@Param("keyword") String keyword);
  ```

### 1.2. Gọi câu Query trong các lớp (Class)
- **Gọi trong lớp `StudentConsoleRunner.java`:** Tự động gọi query và in kết quả tìm kiếm ra màn hình Console ngay khi ứng dụng khởi động.
- **Gọi trong lớp `StudentController.java`:** Cung cấp 2 REST API endpoint:
  - `GET /students/custom/by-department?department={tên_khoa}`
  - `GET /students/custom/search?keyword={từ_khóa}`
- **Gọi trên giao diện Web (`index.html`):** Tích hợp khung tìm kiếm tương tác trực quan, lọc dữ liệu trực tiếp trên bảng.

### 1.3. Áp dụng mô hình Generic DAO Pattern (`dao` & `hibernatedao`)
Tách rời Giao diện (Interface) và Triển khai (Implementation):
- **Package `dao`:**
  - `IGenericDao<Pk, Entity>`: Interface định nghĩa chuẩn các hàm CRUD dùng chung (`create`, `findById`, `findAll`, `update`, `deleteById`).
  - `IStudentDao`: Interface DAO mở rộng dành riêng cho bảng `Student`.
- **Package `hibernatedao`:**
  - `HibernateGenericDao<Pk, Entity>`: Class cha dùng chung kế thừa `SimpleJpaRepository<Entity, Pk>` và triển khai `IGenericDao`. Chứa hàm `create()` có kiểm tra dữ liệu an toàn (`null check`) và ghi log hệ thống (`logger.warn`).
  - `HibernateStudentDao`: Class DAO triển khai thực tế cho bảng `Student`, được Spring quản lý với annotation `@Repository`.

---

## 2. Cấu Trúc Mã Nguồn Bài 2

```text
Bai2/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java               # Khởi chạy Spring Boot
│   │   │   ├── Student.java                       # Entity mô hình sinh viên
│   │   │   ├── StudentRepository.java             # JPA Repository chứa các Custom @Query
│   │   │   ├── StudentController.java             # REST Controller (kết nối cả DAO và Repository)
│   │   │   ├── StudentConsoleRunner.java          # CommandLineRunner in console & demo query
│   │   │   │
│   │   │   ├── dao/                               # [Mục 1: Tầng Interface DAO]
│   │   │   │   ├── IGenericDao.java               # Interface định nghĩa CRUD generic
│   │   │   │   └── IStudentDao.java               # Interface DAO cho Student
│   │   │   │
│   │   │   └── hibernatedao/                      # [Mục 2: Tầng triển khai Hibernate DAO]
│   │   │       ├── HibernateGenericDao.java       # Class cha triển khai dùng chung
│   │   │       └── HibernateStudentDao.java       # Class DAO cụ thể của Student
│   │   │
│   │   └── resources/
│   │       ├── application.properties             # Cấu hình kết nối MySQL
│   │       └── static/index.html                  # Web UI có khu vực test Bài 2
│   └── test/
├── pom.xml                                        # Cấu hình Maven
├── database.sql                                   # Script tạo CSDL & dữ liệu mẫu
├── postman_collection.json                        # File import test API Postman (gồm cả Bài 1 & 2)
└── README.md                                      # Tài liệu hướng dẫn Bài 2
```

---

## 3. Cách Khởi Chạy & Kiểm Thử

### 3.1. Khởi chạy ứng dụng
Mở terminal tại thư mục `Bai2` và chạy:
```powershell
$env:JAVA_HOME="C:\Users\Admin\.jdks\openjdk-26.0.2.1"
.\mvnw.cmd spring-boot:run
```

### 3.2. Kiểm tra trên Console Terminal
Khi chạy, console sẽ in ra demo kết quả gọi câu query tùy chỉnh:
```text
------------------------------------------------------------------------------------------
   [BÀI 2 DEMO] GỌI QUERY TÙY CHỈNH: Tìm kiếm sinh viên theo Khoa 'Cong nghe thong tin'
   -> Câu lệnh JPQL: SELECT s FROM Student s WHERE LOWER(s.department) = LOWER(:department)
------------------------------------------------------------------------------------------
   [KẾT QUẢ TÌM THẤY] ID: 4  | Tên: Nguyen Van An        | Khoa: Cong nghe thong tin  | Ngày sinh: 2003-05-15
   [KẾT QUẢ TÌM THẤY] ID: 5  | Tên: Nguyễn Duy Trường    | Khoa: công nghệ thông tin  | Ngày sinh: 2006-07-21
------------------------------------------------------------------------------------------
```

### 3.3. Kiểm tra trên Giao diện Web
Truy cập: 👉 `http://localhost:8080`
- Sử dụng khung **"[BÀI 2] Khu Vực Thử Nghiệm Câu Query Tự Viết"**:
  - Gõ tên khoa (ví dụ `Cong nghe thong tin`) -> Bấm **"Lọc"**.
  - Gõ từ khóa bất kỳ (ví dụ `Trường`, `Cuong`, `An`) -> Bấm **"Tìm"**.
  - Xem câu lệnh JPQL tương ứng hiển thị trực tiếp.

### 3.4. Kiểm tra qua Postman
Import file `postman_collection.json` vào Postman và chạy các request:
- **Request 8:** `GET http://localhost:8080/students/custom/by-department?department=Cong nghe thong tin`
- **Request 9:** `GET http://localhost:8080/students/custom/search?keyword=Cong`
