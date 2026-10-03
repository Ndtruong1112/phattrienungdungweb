package com.example.demo.hibernateDao;

import com.example.demo.dao.IStudentDao;
import com.example.demo.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class HibernateStudentDao extends HibernateGenericDao<Long, Student> implements IStudentDao {

    @Autowired
    public HibernateStudentDao(EntityManager entityManager) {
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
    public List<Student> findByDepartment(String department) {
        return entityManager.createQuery(
                "SELECT s FROM Student s WHERE LOWER(s.department.departmentName) = LOWER(:department)", Student.class)
                .setParameter("department", department)
                .getResultList();
    }

    @Override
    public Page<Student> findByDepartmentPaged(String department, Pageable pageable) {
        String countJpql = "SELECT COUNT(s) FROM Student s WHERE LOWER(s.department.departmentName) = LOWER(:department)";
        Long total = entityManager.createQuery(countJpql, Long.class)
                .setParameter("department", department)
                .getSingleResult();

        String selectJpql = "SELECT s FROM Student s WHERE LOWER(s.department.departmentName) = LOWER(:department)";
        TypedQuery<Student> query = entityManager.createQuery(selectJpql, Student.class)
                .setParameter("department", department)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        List<Student> content = query.getResultList();
        return new PageImpl<>(content, pageable, total);
    }
}
