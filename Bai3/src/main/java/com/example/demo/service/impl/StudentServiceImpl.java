package com.example.demo.service.impl;

import com.example.demo.dao.IStudentDao;
import com.example.demo.model.Student;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    @Autowired
    private IStudentDao studentDao;

    @Override
    public Student create(Student student) {
        return studentDao.create(student);
    }

    @Override
    public Student update(Long id, Student studentDetails) {
        Student student = studentDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        student.setStudentName(studentDetails.getStudentName());
        student.setDob(studentDetails.getDob());
        student.setEmail(studentDetails.getEmail());
        student.setDepartmentId(studentDetails.getDepartmentId());
        return studentDao.update(student);
    }

    @Override
    public void delete(Long id) {
        studentDao.deleteById(id);
    }

    @Override
    public Optional<Student> getById(Long id) {
        return studentDao.findById(id);
    }

    @Override
    public List<Student> getAll() {
        return studentDao.findAll();
    }

    @Override
    public List<Student> getByName(String name) {
        return studentDao.findByName(name);
    }

    @Override
    public List<Student> getByDepartmentId(Long departmentId) {
        return studentDao.findByDepartmentId(departmentId);
    }
}
