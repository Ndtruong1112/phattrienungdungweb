package com.example.demo.service.impl;

import com.example.demo.dao.IDepartmentDao;
import com.example.demo.model.Department;
import com.example.demo.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private IDepartmentDao departmentDao;

    @Override
    public Department create(Department department) {
        return departmentDao.create(department);
    }

    @Override
    public Department update(Long id, Department departmentDetails) {
        Department dept = departmentDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + id));
        dept.setDepartmentCode(departmentDetails.getDepartmentCode());
        dept.setDepartmentName(departmentDetails.getDepartmentName());
        dept.setDescription(departmentDetails.getDescription());
        return departmentDao.update(dept);
    }

    @Override
    public void delete(Long id) {
        departmentDao.deleteById(id);
    }

    @Override
    public Optional<Department> getById(Long id) {
        return departmentDao.findById(id);
    }

    @Override
    public List<Department> getAll() {
        return departmentDao.findAll();
    }

    @Override
    public List<Department> getByName(String name) {
        return departmentDao.findByName(name);
    }

    @Override
    public Department getByCode(String code) {
        return departmentDao.findByCode(code);
    }
}
