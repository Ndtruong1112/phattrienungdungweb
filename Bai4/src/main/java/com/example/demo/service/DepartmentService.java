package com.example.demo.service;

import com.example.demo.model.Department;
import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    Department create(Department department);
    Department update(Long id, Department department);
    void delete(Long id);
    Optional<Department> getById(Long id);
    List<Department> getAll();
    List<Department> getByName(String name);
    Department getByCode(String code);
}
