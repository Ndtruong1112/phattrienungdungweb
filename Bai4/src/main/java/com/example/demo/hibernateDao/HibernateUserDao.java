package com.example.demo.hibernateDao;

import com.example.demo.dao.IUserDao;
import com.example.demo.entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HibernateUserDao extends HibernateGenericDao<Long, User> implements IUserDao {

    @Autowired
    public HibernateUserDao(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        List<User> list = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> list = entityManager.createQuery(
                "SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)", User.class)
                .setParameter("email", email)
                .getResultList();
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public boolean existsByUsername(String username) {
        Long count = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count > 0;
    }
}
