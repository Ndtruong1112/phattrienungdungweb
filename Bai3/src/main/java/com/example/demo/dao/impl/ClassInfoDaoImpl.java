package com.example.demo.dao.impl;

import com.example.demo.dao.IClassInfoDao;
import com.example.demo.hibernateDao.HibernateGenericDao;
import com.example.demo.model.ClassInfo;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClassInfoDaoImpl extends HibernateGenericDao<Long, ClassInfo> implements IClassInfoDao {

    @Autowired
    public ClassInfoDaoImpl(EntityManager entityManager) {
        super(ClassInfo.class, entityManager);
    }

    @Override
    public List<ClassInfo> findByClassName(String className) {
        return entityManager.createQuery(
                "SELECT c FROM ClassInfo c WHERE LOWER(c.className) LIKE LOWER(CONCAT('%', :className, '%'))", ClassInfo.class)
                .setParameter("className", className)
                .getResultList();
    }
}
