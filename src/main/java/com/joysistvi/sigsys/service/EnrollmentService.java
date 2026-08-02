package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.model.Enrollment;
import java.util.List;

public interface EnrollmentService {
    boolean enrollStudent(int studentId, int sectionId);
    List<Enrollment> getStudentEnrollments(int studentId);
    List<Enrollment> getSectionEnrollments(int sectionId);
    Enrollment getEnrollmentByStudentAndCourse(int studentId, int courseId);
    Enrollment getEnrollmentById(int enrollmentId);
    List<Enrollment> getAllEnrollments();
    boolean updateRegistrationStatus(int enrollmentId, String status);
    boolean submitGrade(int enrollmentId, String finalGrade, double gpaPoints);
    boolean updateAttendanceSummary(int enrollmentId, int attendedDays, int maxAttendance);
}
