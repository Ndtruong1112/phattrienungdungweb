package com.example.demo.dao;

import com.example.demo.entity.User;

import java.util.Optional;

public interface IUserDao extends IGenericDao<Long, User> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
}
