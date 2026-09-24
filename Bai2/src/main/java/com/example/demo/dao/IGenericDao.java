package com.example.demo.dao;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IGenericDao<Pk, Entity> {

    @Transactional
    Entity create(Entity anEntity);

    Optional<Entity> findById(Pk id);

    List<Entity> findAll();

    @Transactional
    Entity update(Entity anEntity);

    @Transactional
    void deleteById(Pk id);
}
