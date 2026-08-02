package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.dao.StudentDao;
import com.joysistvi.sigsys.model.Student;
import java.util.List;

public class StudentRepositoryImpl implements StudentRepository {
    private final StudentDao studentDao = new StudentDao();

    @Override
    public boolean createStudent(Student student) {
        return studentDao.createStudent(student);
    }

    @Override
    public Student getStudentById(int studentId) {
        return studentDao.getStudentById(studentId);
    }

    @Override
    public Student getStudentByUserId(int userId) {
        return studentDao.getStudentByUserId(userId);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDao.getAllStudents();
    }

    @Override
    public boolean updateStudent(Student student) {
        return studentDao.updateStudent(student);
    }

    @Override
    public boolean deleteStudent(int studentId) {
        return studentDao.deleteStudent(studentId);
    }
}