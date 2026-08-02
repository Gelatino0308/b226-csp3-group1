package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.model.Student;
import java.util.List;

public interface StudentRepository {
    boolean createStudent(Student student);
    Student getStudentById(int studentId);
    Student getStudentByUserId(int userId);
    List<Student> getAllStudents();
    boolean updateStudent(Student student);
    boolean deleteStudent(int studentId);
}