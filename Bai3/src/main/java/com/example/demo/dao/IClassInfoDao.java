package com.example.demo.dao;

import com.example.demo.model.ClassInfo;
import java.util.List;

public interface IClassInfoDao extends IGenericDao<Long, ClassInfo> {
    List<ClassInfo> findByClassName(String className);
}
