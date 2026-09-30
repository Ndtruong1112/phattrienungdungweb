package com.example.demo.dao.impl;

import com.example.demo.dao.IDepartmentDao;
import com.example.demo.hibernateDao.HibernateGenericDao;
import com.example.demo.model.Department;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DepartmentDaoImpl extends HibernateGenericDao<Long, Department> implements IDepartmentDao {

    @Autowired
    public DepartmentDaoImpl(EntityManager entityManager) {
        super(Department.class, entityManager);
    }

    @Override
    public List<Department> findByName(String name) {
        return entityManager.createQuery(
                "SELECT d FROM Department d WHERE LOWER(d.departmentName) LIKE LOWER(CONCAT('%', :name, '%'))", Department.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public Department findByCode(String code) {
        try {
            return entityManager.createQuery(
                    "SELECT d FROM Department d WHERE d.departmentCode = :code", Department.class)
                    .setParameter("code", code)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
