package com.example.demo;

import com.example.demo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByStudentNameContainingIgnoreCase(String keyword);

    @Query("SELECT s FROM Student s WHERE s.departmentId = :departmentId")
    List<Student> findByDepartmentIdCustom(@Param("departmentId") Long departmentId);

    @Query("SELECT s FROM Student s WHERE LOWER(s.studentName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Student> searchByNameCustom(@Param("keyword") String keyword);
}
