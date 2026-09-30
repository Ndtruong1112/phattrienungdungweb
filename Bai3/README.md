# BÀI TẬP 3: MÔ HÌNH DAO VÀ HIBERNATE DAO (GENERIC DAO PATTERN)

Dự án này triển khai cấu trúc dữ liệu theo mẫu thiết kế **DAO (Data Access Object)** và **Hibernate DAO (Generic DAO Pattern)** chuẩn enterprise trong Java Spring Boot.

---

## 1. Mục Đích & Ý Nghĩa Kiến Trúc

### 1.1. DAO (Data Access Object) là gì?
- **DAO** là một mẫu thiết kế (Design Pattern) giúp **tách biệt hoàn toàn tầng nghiệp vụ (Business/Service/Controller) khỏi tầng truy xuất cơ sở dữ liệu (Database)**.
- Khi cần đổi cơ sở dữ liệu hoặc đổi thư viện ORM (từ Hibernate sang JDBC hay MyBatis), tầng Controller/Service không cần thay đổi bất kỳ dòng mã nào.

### 1.2. Hibernate Generic DAO là gì?
- Thay vì mỗi Entity (Student, Teacher, Course...) phải viết lại các hàm CRUD (`create`, `update`, `deleteById`, `findById`, `findAll`), ta xây dựng một lớp cơ sở `HibernateGenericDao<Pk, Entity>` tổng quát.
- Lớp `HibernateGenericDao` sử dụng `EntityManager` của JPA/Hibernate để thực thi các tác vụ cơ bản.

```
       Controller / Service
               │
               ▼
         [IStudentDao]  ────────► Kế thừa  [IGenericDao<Long, Student>]
               │                                      │
           implements                             implements
               │                                      │
               ▼                                      ▼
     [HibernateStudentDao] ─────► Kế thừa  [HibernateGenericDao<Long, Student>]
               │                                      │
               └─────────── EntityManager ────────────┘
                                  │
                                  ▼
                             MySQL Database
```

---

## 2. Cấu Trúc Thư Mục & Các File

```
Bai3/src/main/java/com/example/demo/
├── DemoApplication.java
├── Student.java
├── StudentConsoleRunner.java
├── StudentController.java
├── dao/
│   ├── IGenericDao.java       # Interface CRUD tổng quát
│   └── IStudentDao.java       # Interface nghiệp vụ riêng của Student
└── hibernatedao/
    ├── HibernateGenericDao.java # Triển khai chung CRUD dùng EntityManager
    └── HibernateStudentDao.java # Triển khai chi tiết cho Student (HQL/JPQL)
```

---

## 3. Mã Nguồn Cốt Lõi

### 3.1. Generic DAO Interface (`IGenericDao.java`)
```java
public interface IGenericDao<Pk, Entity> {
    Entity create(Entity anEntity);
    Optional<Entity> findById(Pk id);
    List<Entity> findAll();
    Entity update(Entity anEntity);
    void deleteById(Pk id);
}
```

### 3.2. Student DAO Interface (`IStudentDao.java`)
```java
public interface IStudentDao extends IGenericDao<Long, Student> {
    List<Student> findByName(String name);
    List<Student> findByDepartment(String department);
}
```

### 3.3. Hibernate Student DAO (`HibernateStudentDao.java`)
```java
@Repository
public class HibernateStudentDao extends HibernateGenericDao<Long, Student> implements IStudentDao {

    @Autowired
    public HibernateStudentDao(EntityManager entityManager) {
        super(Student.class, entityManager);
    }

    @Override
    public List<Student> findByName(String name) {
        return entityManager.createQuery(
                "SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))", Student.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public List<Student> findByDepartment(String department) {
        return entityManager.createQuery(
                "SELECT s FROM Student s WHERE LOWER(s.department) = LOWER(:department)", Student.class)
                .setParameter("department", department)
                .getResultList();
    }
}
```

---

## 4. Hướng Dẫn Chạy & Kiểm Thử

### Khởi động ứng dụng:
```bash
$env:JAVA_HOME="C:\Users\Admin\.jdks\openjdk-26.0.2.1"
.\mvnw.cmd spring-boot:run
```

### Test API Postman / Trình duyệt:
1. **Lấy toàn bộ sinh viên (qua DAO)**:
   - `GET http://localhost:8080/students`
2. **Lấy chi tiết sinh viên theo ID**:
   - `GET http://localhost:8080/students/1`
3. **Thêm sinh viên mới (gọi `studentDao.create`)**:
   - `POST http://localhost:8080/students`
   - Body: `{"name":"Nguyen Van A","dob":"2003-05-15","department":"Cong nghe thong tin","email":"vana@gmail.com"}`
4. **Cập nhật sinh viên (gọi `studentDao.update`)**:
   - `PUT http://localhost:8080/students/1`
5. **Xóa sinh viên (gọi `studentDao.deleteById`)**:
   - `DELETE http://localhost:8080/students/1`
6. **Tìm kiếm theo Khoa qua DAO**:
   - `GET http://localhost:8080/students/dao/by-department?department=Cong nghe thong tin`
7. **Tìm kiếm theo Tên qua DAO**:
   - `GET http://localhost:8080/students/dao/by-name?name=Nguyen`
