package com.example.demo.service;

import com.example.demo.model.Student;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student create(Student student);
    Student update(Long id, Student student);
    void delete(Long id);
    Optional<Student> getById(Long id);
    List<Student> getAll();
    List<Student> getByName(String name);
    List<Student> getByDepartmentId(Long departmentId);
}
