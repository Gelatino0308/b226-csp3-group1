package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.dao.StudentDao;
import com.joysistvi.sigsys.model.Student;
import java.util.List;

public class StudentServiceImpl implements StudentService {
    private final StudentDao studentDao = new StudentDao();

    @Override
    public boolean createStudent(Student student) {
        if (student == null || student.getUserId() <= 0 || student.getFirstName() == null
                || student.getLastName() == null || student.getFirstName().trim().isEmpty()
                || student.getLastName().trim().isEmpty()) return false;
        return studentDao.createStudent(student);
    }

    @Override
    public Student getStudentById(int studentId) {
        return studentId > 0 ? studentDao.getStudentById(studentId) : null;
    }

    @Override
    public Student getStudentByUserId(int userId) {
        return userId > 0 ? studentDao.getStudentByUserId(userId) : null;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDao.getAllStudents();
    }

    @Override
    public boolean updateStudent(Student student) {
        return student != null && student.getStudentId() > 0 && studentDao.updateStudent(student);
    }
}
