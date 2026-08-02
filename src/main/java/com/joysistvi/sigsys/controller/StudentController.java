package com.joysistvi.sigsys.controller;

import com.joysistvi.sigsys.dao.StudentDao;
import com.joysistvi.sigsys.model.Student;

import java.util.List;

public class StudentController {
    private final StudentDao studentDao;

    public StudentController() {
        this.studentDao = new StudentDao();
    }

    public List<Student> getAllStudents() {
        return studentDao.getAllStudents();
    }

    public boolean createStudentProfile(Student student) {
        if (student == null || student.getUserId() <= 0 || student.getFirstName() == null
                || student.getLastName() == null || student.getFirstName().trim().isEmpty()
                || student.getLastName().trim().isEmpty()) {
            System.err.println("Error: Student profile details are required.");
            return false;
        }
        return studentDao.createStudent(student);
    }

    public Student getStudentById(int studentId) {
        if (studentId <= 0) {
            System.err.println("Error: Invalid Student ID.");
            return null;
        }
        return studentDao.getStudentById(studentId);
    }

    public Student getStudentByUserId(int userId) {
        if (userId <= 0) {
            System.err.println("Error: Invalid User ID.");
            return null;
        }
        return studentDao.getStudentByUserId(userId);
    }

    public boolean updateStudentProfile(Student student) {
        if (student == null || student.getStudentId() <= 0) {
            System.err.println("Error: Cannot update invalid student record.");
            return false;
        }
        return studentDao.updateStudent(student);
    }
}
