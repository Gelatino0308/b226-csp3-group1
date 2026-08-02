package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.model.Student;
import java.util.List;

public interface StudentService {
    boolean createStudent(Student student);
    Student getStudentById(int studentId);
    Student getStudentByUserId(int userId);
    List<Student> getAllStudents();
    boolean updateStudent(Student student);
}