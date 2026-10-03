package com.example.demo.dao;

import com.example.demo.entity.Department;

import java.util.Optional;

public interface IDepartmentDao extends IGenericDao<Long, Department> {
    Optional<Department> findByDepartmentCode(String departmentCode);
    Optional<Department> findByDepartmentName(String departmentName);
}
