package com.example.demo.hibernateDao;

import com.example.demo.dao.IDepartmentDao;
import com.example.demo.entity.Department;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HibernateDepartmentDao extends HibernateGenericDao<Long, Department> implements IDepartmentDao {

    @Autowired
    public HibernateDepartmentDao(EntityManager entityManager) {
        super(Department.class, entityManager);
    }

    @Override
    public Optional<Department> findByDepartmentCode(String departmentCode) {
        List<Department> list = entityManager.createQuery(
                "SELECT d FROM Department d WHERE LOWER(d.departmentCode) = LOWER(:code)", Department.class)
                .setParameter("code", departmentCode)
                .getResultList();
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Optional<Department> findByDepartmentName(String departmentName) {
        List<Department> list = entityManager.createQuery(
                "SELECT d FROM Department d WHERE LOWER(d.departmentName) = LOWER(:name)", Department.class)
                .setParameter("name", departmentName)
                .getResultList();
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }
}
