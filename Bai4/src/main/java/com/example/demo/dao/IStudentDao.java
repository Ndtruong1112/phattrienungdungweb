package com.example.demo.dao;

import com.example.demo.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IStudentDao extends IGenericDao<Long, Student> {
    List<Student> findByName(String name);
    List<Student> findByDepartment(String department);
    Page<Student> findByDepartmentPaged(String department, Pageable pageable);
}
