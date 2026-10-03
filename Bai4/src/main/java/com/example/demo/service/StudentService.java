package com.example.demo.service;

import com.example.demo.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> getAllStudents();
    Optional<Student> getStudentById(Long id);
    Student createStudent(Student student);
    Student updateStudent(Long id, Student student);
    void deleteStudent(Long id);
    List<Student> findByName(String name);
    List<Student> findByDepartment(String department);

    // Hỗ trợ Sắp xếp (Sorting - Baeldung bài 1)
    List<Student> getStudentsSorted(Sort sort);

    // Hỗ trợ Phân trang (Pagination - Baeldung bài 2)
    Page<Student> getStudentsPaged(Pageable pageable);

    // Phân trang theo Khoa
    Page<Student> getStudentsByDepartmentPaged(String department, Pageable pageable);
}
