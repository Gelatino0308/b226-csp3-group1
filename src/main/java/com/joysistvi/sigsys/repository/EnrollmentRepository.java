package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.model.Enrollment;
import java.util.List;

public interface EnrollmentRepository {
    boolean createEnrollment(int studentId, int sectionId);
    boolean isAlreadyEnrolled(int studentId, int sectionId);
    List<Enrollment> getEnrollmentsByStudentId(int studentId);
    List<Enrollment> getEnrollmentsBySectionId(int sectionId);
    Enrollment getEnrollmentById(int enrollmentId);
    Enrollment getEnrollmentByStudentAndCourse(int studentId, int courseId);
    List<Enrollment> getAllEnrollments();
    boolean updateRegistrationStatus(int enrollmentId, String status);
    boolean updateGrade(int enrollmentId, String finalGrade, double gpaPoints);
    boolean updateAttendanceSummary(int enrollmentId, int attendedDays, int maxAttendance);
}
