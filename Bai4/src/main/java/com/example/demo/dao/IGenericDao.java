package com.example.demo.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IGenericDao<Pk, Entity> {

    @Transactional
    Entity create(Entity anEntity);

    Optional<Entity> findById(Pk id);

    List<Entity> findAll();

    // Sắp xếp (Sorting - Baeldung bài 1)
    List<Entity> findAll(Sort sort);

    // Phân trang (Pagination - Baeldung bài 2)
    Page<Entity> findAll(Pageable pageable);

    @Transactional
    Entity update(Entity anEntity);

    @Transactional
    void deleteById(Pk id);
}
