package com.example.demo.dao;

import com.example.demo.Student;

import java.util.List;

public interface IStudentDao extends IGenericDao<Long, Student> {
    List<Student> findByName(String name);
    List<Student> findByDepartment(String department);
}
