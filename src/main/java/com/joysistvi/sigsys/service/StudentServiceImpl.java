package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.repository.StudentRepository;
import com.joysistvi.sigsys.repository.StudentRepositoryImpl;
import com.joysistvi.sigsys.model.Student;
import java.util.List;

public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository = new StudentRepositoryImpl();

    @Override
    public boolean createStudent(Student student) {
        if (student == null || student.getUserId() <= 0 || student.getFirstName() == null
                || student.getLastName() == null || student.getFirstName().trim().isEmpty()
                || student.getLastName().trim().isEmpty()) return false;
        return studentRepository.createStudent(student);
    }

    @Override
    public Student getStudentById(int studentId) {
        return studentId > 0 ? studentRepository.getStudentById(studentId) : null;
    }

    @Override
    public Student getStudentByUserId(int userId) {
        return userId > 0 ? studentRepository.getStudentByUserId(userId) : null;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.getAllStudents();
    }

    @Override
    public boolean updateStudent(Student student) {
        return student != null && student.getStudentId() > 0 && studentRepository.updateStudent(student);
    }
}
