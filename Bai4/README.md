# BÀI 4: KIẾN TRÚC HIBERNATE DAO KẾT HỢP SPRING DATA JPA - TÍCH HỢP PHÂN TRANG (PAGINATION) & SẮP XẾP (SORTING)

> **Môn học:** Phát triển ứng dụng Web  
> **Sinh viên:** Nguyễn Duy Trường  
> **Công nghệ:** Java 17+, Spring Boot, Spring Data JPA, Hibernate, MySQL, Maven, Postman  
> **Tham khảo chuẩn:** Baeldung (*Sorting Query Results with Spring Data* & *Pagination and Sorting using Spring Data JPA*)

---

## 1. Cấu Trúc Thư Mục Chuẩn Doanh Nghiệp

```text
Bai4/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java               # Khởi chạy Spring Boot
│   │   │   ├── StudentConsoleRunner.java          # Demo Phân trang, Sắp xếp và in bảng Console
│   │   │   │
│   │   │   ├── controller/                        # [Tầng REST Controller]
│   │   │   │   ├── StudentController.java         # API sinh viên (CRUD + /sort + /page + resolveDepartment)
│   │   │   │   └── UserController.java            # API người dùng (User)
│   │   │   │
│   │   │   ├── dao/                               # [Tầng DAO Interfaces]
│   │   │   │   ├── IGenericDao.java               # Interface Generic (CRUD + Sort + Pageable)
│   │   │   │   ├── IStudentDao.java               # Interface cho Student (kèm phân trang theo khoa)
│   │   │   │   ├── IDepartmentDao.java            # Interface cho Department
│   │   │   │   └── IUserDao.java                  # Interface cho User
│   │   │   │
│   │   │   ├── entity/                            # [Tầng JPA Entities]
│   │   │   │   ├── Department.java                # Bảng 'departments'
│   │   │   │   ├── Student.java                   # Bảng 'students'
│   │   │   │   └── User.java                      # Bảng 'users'
│   │   │   │
│   │   │   ├── hibernateDao/                      # [TẦNG CÁC LỚP HIBERNATE DAO TRIỂN KHAI]
│   │   │   │   ├── HibernateGenericDao.java       # Lớp cha Generic kế thừa SimpleJpaRepository (EntityManager)
│   │   │   │   ├── HibernateStudentDao.java       # DAO chi tiết cho Student (HQL/JPQL + PageImpl)
│   │   │   │   ├── HibernateDepartmentDao.java    # DAO chi tiết cho Department
│   │   │   │   └── HibernateUserDao.java          # DAO chi tiết cho User
│   │   │   │
│   │   │   ├── repository/                        # [Tầng Spring Data JPA Repository]
│   │   │   │   └── UserRepository.java            # Kế thừa JpaRepository<User, Long>
│   │   │   │
│   │   │   └── service/                           # [Tầng Business Service]
│   │   │       ├── StudentService.java            # Service Interface (có getStudentsSorted & getStudentsPaged)
│   │   │       └── impl/
│   │   │           └── StudentServiceImpl.java    # Service Impl gọi qua tầng Hibernate DAO
│   │   │
│   │   └── resources/
│   │       ├── application.properties             # Cấu hình MySQL (demo_db)
│   │       └── static/index.html                  # Giao diện Web hiển thị realtime
│   └── test/
├── pom.xml                                        # Cấu hình Maven
├── database.sql                                   # Script tạo các bảng CSDL
└── README.md
```

---

## 2. Các Lớp Trong Thư Mục `hibernateDao`

1. **`HibernateGenericDao<Pk, Entity>`**: Lớp cơ sở tổng quát cho toàn bộ các DAO, kế thừa `SimpleJpaRepository` của Spring Data JPA và tích hợp:
   - `create()`, `update()`, `findById()`, `findAll()`, `deleteById()`
   - **`findAll(Sort sort)`**: Sắp xếp linh hoạt theo mọi trường và chiều (Asc/Desc) theo bài viết Baeldung 1.
   - **`findAll(Pageable pageable)`**: Phân trang theo `PageRequest.of(page, size, sort)` theo bài viết Baeldung 2.
2. **`HibernateStudentDao`**: Kế thừa `HibernateGenericDao<Long, Student>` và triển khai `IStudentDao`:
   - `findByName(String name)`
   - `findByDepartment(String department)`
   - `findByDepartmentPaged(String department, Pageable pageable)`: Phân trang theo Khoa sử dụng `TypedQuery` và `PageImpl`.
3. **`HibernateDepartmentDao`**: Kế thừa `HibernateGenericDao<Long, Department>` và triển khai `IDepartmentDao`:
   - `findByDepartmentCode(String code)`
   - `findByDepartmentName(String name)`
4. **`HibernateUserDao`**: Kế thừa `HibernateGenericDao<Long, User>` và triển khai `IUserDao`:
   - `findByUsername(String username)`
   - `findByEmail(String email)`
   - `existsByUsername(String username)`

---

## 3. Các API Kiểm Thử (Theo Chuẩn Baeldung)

### 3.1. Sắp Xếp (Sorting - Baeldung 1):
- `GET http://localhost:8080/students/sort?sortBy=studentName&direction=asc`
- `GET http://localhost:8080/students/sort?sortBy=dob&direction=desc`

### 3.2. Phân Trang & Sắp Xếp (Pagination & Sorting - Baeldung 2):
- `GET http://localhost:8080/students/page?page=0&size=2&sortBy=studentName&direction=asc`
- `GET http://localhost:8080/students/page-by-department?department=Cong nghe thong tin&page=0&size=2`

Cấu trúc JSON trả về chuẩn `Page<Student>`:
```json
{
  "content": [
    {
      "studentId": 1,
      "studentName": "Nguyen Van A",
      "dob": "2003-05-15",
      "email": "vana@gmail.com",
      "department": { "departmentName": "Cong nghe thong tin" }
    }
  ],
  "pageable": { "pageNumber": 0, "pageSize": 2 },
  "totalElements": 5,
  "totalPages": 3,
  "last": false
}
```

---

## 4. Cách Khởi Chạy
```powershell
$env:JAVA_HOME="C:\Users\Admin\.jdks\openjdk-26.0.2.1"
.\mvnw.cmd spring-boot:run
```
