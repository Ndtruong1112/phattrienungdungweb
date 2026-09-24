package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByNameContainingIgnoreCase(String keyword);
    List<Student> findByDepartmentContainingIgnoreCase(String department);

    // ==========================================================
    // BÀI 2: VIẾT CÂU QUERY TÙY CHỈNH (CUSTOM JPQL QUERY)
    // ==========================================================

    // 1. Query tìm sinh viên theo đúng tên khoa (không phân biệt hoa thường)
    @Query("SELECT s FROM Student s WHERE LOWER(s.department) = LOWER(:department)")
    List<Student> findByDepartmentCustom(@Param("department") String department);

    // 2. Query tìm kiếm theo từ khóa xuất hiện trong Tên hoặc Khoa
    @Query("SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.department) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Student> searchByNameOrDepartmentCustom(@Param("keyword") String keyword);
}


