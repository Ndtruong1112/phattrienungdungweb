package com.example.demo.hibernateDao;

import com.example.demo.dao.IGenericDao;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class HibernateGenericDao<Pk, Entity> extends SimpleJpaRepository<Entity, Pk> implements IGenericDao<Pk, Entity> {

    private static final Logger logger = LoggerFactory.getLogger(HibernateGenericDao.class);
    private Class<Entity> type;
    protected EntityManager entityManager;

    public HibernateGenericDao(Class<Entity> type, EntityManager entityManager) {
        super(type, entityManager);
        this.type = type;
        this.entityManager = entityManager;
    }

    public Class<Entity> getType() {
        return type;
    }

    @Override
    @Transactional
    public Entity create(Entity anEntity) {
        if (anEntity == null) {
            logger.warn("Cannot create a null entity.");
            throw new IllegalArgumentException("Cannot create null entity[" + (getType() != null ? getType().getName() : "Unknown") + "]");
        } else {
            return save(anEntity);
        }
    }

    @Override
    @Transactional
    public Entity update(Entity anEntity) {
        if (anEntity == null) {
            logger.warn("Cannot update a null entity.");
            throw new IllegalArgumentException("Cannot update null entity[" + (getType() != null ? getType().getName() : "Unknown") + "]");
        }
        return save(anEntity);
    }

    // Helper: Sắp xếp theo tên thuộc tính (Sorting - Baeldung bài 1)
    public List<Entity> findAllSorted(String propertyName, boolean ascending) {
        Sort sort = ascending ? Sort.by(propertyName).ascending() : Sort.by(propertyName).descending();
        return findAll(sort);
    }

    // Helper: Phân trang kèm sắp xếp (Pagination & Sorting - Baeldung bài 2)
    public Page<Entity> findAllPaged(int page, int size, String sortBy, boolean ascending) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return findAll(pageable);
    }
}
