package com.example.demo.service.impl;

import com.example.demo.dao.IStudentDao;
import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private IStudentDao studentDao;

    @Override
    public List<Student> getAllStudents() {
        return studentDao.findAll();
    }

    @Override
    public Optional<Student> getStudentById(Long id) {
        return studentDao.findById(id);
    }

    @Override
    public Student createStudent(Student student) {
        return studentDao.create(student);
    }

    @Override
    public Student updateStudent(Long id, Student student) {
        return studentDao.findById(id).map(existing -> {
            existing.setStudentName(student.getStudentName());
            existing.setDob(student.getDob());
            existing.setEmail(student.getEmail());
            if (student.getDepartment() != null) {
                existing.setDepartment(student.getDepartment());
            }
            return studentDao.update(existing);
        }).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên có ID: " + id));
    }

    @Override
    public void deleteStudent(Long id) {
        studentDao.deleteById(id);
    }

    @Override
    public List<Student> findByName(String name) {
        return studentDao.findByName(name);
    }

    @Override
    public List<Student> findByDepartment(String department) {
        return studentDao.findByDepartment(department);
    }

    @Override
    public List<Student> getStudentsSorted(Sort sort) {
        return studentDao.findAll(sort);
    }

    @Override
    public Page<Student> getStudentsPaged(Pageable pageable) {
        return studentDao.findAll(pageable);
    }

    @Override
    public Page<Student> getStudentsByDepartmentPaged(String department, Pageable pageable) {
        return studentDao.findByDepartmentPaged(department, pageable);
    }
}
