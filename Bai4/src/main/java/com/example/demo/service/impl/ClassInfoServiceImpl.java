package com.example.demo.service.impl;

import com.example.demo.dao.IClassInfoDao;
import com.example.demo.model.ClassInfo;
import com.example.demo.service.ClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClassInfoServiceImpl implements ClassInfoService {

    @Autowired
    private IClassInfoDao classInfoDao;

    @Override
    public ClassInfo create(ClassInfo classInfo) {
        return classInfoDao.create(classInfo);
    }

    @Override
    public ClassInfo update(Long id, ClassInfo classInfoDetails) {
        ClassInfo classInfo = classInfoDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ClassInfo not found with id: " + id));
        classInfo.setClassCode(classInfoDetails.getClassCode());
        classInfo.setClassName(classInfoDetails.getClassName());
        classInfo.setDescription(classInfoDetails.getDescription());
        return classInfoDao.update(classInfo);
    }

    @Override
    public void delete(Long id) {
        classInfoDao.deleteById(id);
    }

    @Override
    public Optional<ClassInfo> getById(Long id) {
        return classInfoDao.findById(id);
    }

    @Override
    public List<ClassInfo> getAll() {
        return classInfoDao.findAll();
    }

    @Override
    public List<ClassInfo> getByClassName(String className) {
        return classInfoDao.findByClassName(className);
    }
}
