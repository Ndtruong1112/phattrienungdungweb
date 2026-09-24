# Môn Học: Phát Triển Ứng Dụng Web

Repository tổng hợp các bài tập, bài thực hành và dự án môn **Phát triển ứng dụng Web**.

- **Sinh viên thực hiện:** Nguyễn Duy Trường
- **Tài khoản GitHub:** [@Ndtruong1112](https://github.com/Ndtruong1112)
- **Công nghệ sử dụng:** Java, Spring Boot, Spring Data JPA, Maven, MySQL, RESTful API, Postman, Bootstrap

---

## 📑 Danh Mục Các Bài Tập

| Thư mục | Tên Bài | Nội Dung Tóm Tắt | Trạng Thái |
| :---: | :--- | :--- | :---: |
| [**`Bai1/`**](./Bai1) | **Bài 1: Quản Lý Sinh Viên Cơ Bản** | Ứng dụng Java Spring Boot kết nối MySQL, quản lý thông tin sinh viên (`id`, `name`, `dob`, `department`, `email`). Hỗ trợ hiển thị bảng dữ liệu ra màn hình Console khi chạy và cung cấp REST API đầy đủ CRUD với định dạng JSON qua Postman. |  **Hoàn thành** |
| [**`Bai2/`**](./Bai2) | **Bài 2: Custom Query & Hibernate Generic DAO** | Tùy biến truy vấn với `@Query` JPQL (tìm theo khoa, từ khóa), gọi trong Console Runner và Controller, tích hợp kiến trúc Generic DAO Pattern (`dao` & `hibernatedao`). |  **Hoàn thành** |

---

## 🛠 Hướng Dẫn Chung Khi Mở Dự Án

1. Yêu cầu môi trường: **JDK 17 trở lên** và dịch vụ **MySQL Server** (cổng 3306).
2. Di chuyển vào thư mục bài tập tương ứng (ví dụ `cd Bai1`).
3. Khởi chạy ứng dụng:
   ```bash
   .\mvnw.cmd spring-boot:run
   ```
4. Truy cập giao diện trực tiếp trên trình duyệt: `http://localhost:8080`.
