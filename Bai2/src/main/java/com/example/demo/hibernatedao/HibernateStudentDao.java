package com.example.demo.hibernatedao;

import com.example.demo.Student;
import com.example.demo.dao.IStudentDao;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HibernateStudentDao extends HibernateGenericDao<Long, Student> implements IStudentDao {

    @Autowired
    public HibernateStudentDao(EntityManager entityManager) {
        super(Student.class, entityManager);
    }

    @Override
    public List<Student> findByName(String name) {
        return entityManager.createQuery(
                "SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))", Student.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public List<Student> findByDepartment(String department) {
        return entityManager.createQuery(
                "SELECT s FROM Student s WHERE LOWER(s.department) = LOWER(:department)", Student.class)
                .setParameter("department", department)
                .getResultList();
    }
}
