package com.example.demo.dao.impl;

import com.example.demo.dao.IStudentDao;
import com.example.demo.hibernateDao.HibernateGenericDao;
import com.example.demo.model.Student;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentDaoImpl extends HibernateGenericDao<Long, Student> implements IStudentDao {

    @Autowired
    public StudentDaoImpl(EntityManager entityManager) {
        super(Student.class, entityManager);
    }

    @Override
    public List<Student> findByName(String name) {
        return entityManager.createQuery(
                "SELECT s FROM Student s WHERE LOWER(s.studentName) LIKE LOWER(CONCAT('%', :name, '%'))", Student.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public List<Student> findByDepartmentId(Long departmentId) {
        return entityManager.createQuery(
                "SELECT s FROM Student s WHERE s.departmentId = :departmentId", Student.class)
                .setParameter("departmentId", departmentId)
                .getResultList();
    }
}
