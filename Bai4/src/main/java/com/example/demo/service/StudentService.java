package com.example.demo.service;

import com.example.demo.entity.Student;

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
}
