package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.model.Enrollment;
import java.util.List;

public interface EnrollmentService {
    boolean enrollStudent(int studentId, int sectionId);
    List<Enrollment> getStudentEnrollments(int studentId);
    boolean submitGrade(int enrollmentId, String finalGrade, double gpaPoints);
}