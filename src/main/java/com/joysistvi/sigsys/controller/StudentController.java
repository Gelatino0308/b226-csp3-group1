package com.joysistvi.sigsys.controller;
import com.joysistvi.sigsys.model.Student;
import com.joysistvi.sigsys.service.StudentService;
import com.joysistvi.sigsys.service.StudentServiceImpl;
import java.util.List;
public class StudentController {
    private final StudentService studentService = new StudentServiceImpl();
    public List<Student> getAllStudents() { return studentService.getAllStudents(); }
    public boolean createStudentProfile(Student student) { return studentService.createStudent(student); }
    public Student getStudentById(int studentId) { return studentService.getStudentById(studentId); }
    public Student getStudentByUserId(int userId) { return studentService.getStudentByUserId(userId); }
    public boolean updateStudentProfile(Student student) { return studentService.updateStudent(student); }
}
