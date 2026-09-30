-- Script tạo Database và bảng sinh viên cho MySQL
CREATE DATABASE IF NOT EXISTS demo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE demo_db;

CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    dob DATE,
    department VARCHAR(255),
    email VARCHAR(255)
);

-- Dữ liệu mẫu ban đầu
INSERT INTO students (name, dob, department, email) VALUES
('Nguyen Van A', '2003-05-15', 'Cong nghe thong tin', 'vana@gmail.com'),
('Tran Thi B', '2004-08-20', 'Khoa hoc may tinh', 'thib@gmail.com'),
('Le Van C', '2002-11-10', 'Quan tri kinh doanh', 'vanc@gmail.com');
