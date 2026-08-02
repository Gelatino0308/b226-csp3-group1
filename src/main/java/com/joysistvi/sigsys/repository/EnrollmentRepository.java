package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.model.Enrollment;
import java.util.List;

public interface EnrollmentRepository {
    boolean createEnrollment(int studentId, int sectionId);
    boolean isAlreadyEnrolled(int studentId, int sectionId);
    List<Enrollment> getEnrollmentsByStudentId(int studentId);
    boolean updateGrade(int enrollmentId, String finalGrade, double gpaPoints);
}