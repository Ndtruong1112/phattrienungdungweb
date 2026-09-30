package com.example.demo.service;

import com.example.demo.model.ClassInfo;
import java.util.List;
import java.util.Optional;

public interface ClassInfoService {
    ClassInfo create(ClassInfo classInfo);
    ClassInfo update(Long id, ClassInfo classInfo);
    void delete(Long id);
    Optional<ClassInfo> getById(Long id);
    List<ClassInfo> getAll();
    List<ClassInfo> getByClassName(String className);
}
