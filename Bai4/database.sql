-- Script CSDL MySQL cho Bài 4
CREATE DATABASE IF NOT EXISTS demo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE demo_db;

-- 1. Bảng Khoa / Ngành (departments)
CREATE TABLE IF NOT EXISTS departments (
    department_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    department_code VARCHAR(20) NOT NULL UNIQUE,
    department_name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    created_at DATETIME(6),
    updated_at DATETIME(6)
);

-- 2. Bảng Người dùng / Tài khoản (users) - Quản lý bởi Spring Data JPA
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    full_name VARCHAR(100),
    role VARCHAR(20) DEFAULT 'USER',
    created_at DATETIME(6),
    updated_at DATETIME(6)
);

-- 3. Bảng Sinh viên (students) - Quản lý bởi Hibernate Generic DAO
CREATE TABLE IF NOT EXISTS students (
    student_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL,
    dob DATE,
    email VARCHAR(100),
    department_id BIGINT,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    CONSTRAINT fk_students_department FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL
);

-- Dữ liệu mẫu ban đầu
INSERT INTO departments (department_code, department_name, description) VALUES
('CNTT', 'Cong nghe thong tin', 'Khoa Cong nghe thong tin'),
('KHMT', 'Khoa hoc may tinh', 'Khoa Khoa hoc may tinh'),
('QTKD', 'Quan tri kinh doanh', 'Khoa Quan tri kinh doanh')
ON DUPLICATE KEY UPDATE department_name=VALUES(department_name);

INSERT INTO users (username, password, email, full_name, role) VALUES
('admin', '123456', 'admin@school.edu.vn', 'Quan Tri Vien', 'ADMIN'),
('gv_nam', '123456', 'nam@school.edu.vn', 'Nguyen Van Nam', 'USER')
ON DUPLICATE KEY UPDATE email=VALUES(email);

INSERT INTO students (student_name, dob, email, department_id) VALUES
('Nguyen Van A', '2003-05-15', 'vana@gmail.com', 1),
('Tran Thi B', '2004-08-20', 'thib@gmail.com', 2),
('Le Van C', '2002-11-10', 'vanc@gmail.com', 1)
ON DUPLICATE KEY UPDATE email=VALUES(email);
