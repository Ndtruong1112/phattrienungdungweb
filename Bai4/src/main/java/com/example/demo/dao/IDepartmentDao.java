package com.example.demo.dao;

import com.example.demo.model.Department;
import java.util.List;

public interface IDepartmentDao extends IGenericDao<Long, Department> {
    List<Department> findByName(String name);
    Department findByCode(String code);
}
