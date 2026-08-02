package com.joysistvi.sigsys.controller;
import com.joysistvi.sigsys.model.Enrollment;
import com.joysistvi.sigsys.service.EnrollmentService;
import com.joysistvi.sigsys.service.EnrollmentServiceImpl;
import java.util.List;
public class EnrollmentController {
    private final EnrollmentService enrollmentService = new EnrollmentServiceImpl();
    public boolean enrollStudent(int studentId, int sectionId) { return enrollmentService.enrollStudent(studentId, sectionId); }
    public List<Enrollment> getStudentEnrollments(int studentId) { return enrollmentService.getStudentEnrollments(studentId); }
    public List<Enrollment> getSectionEnrollments(int sectionId) { return enrollmentService.getSectionEnrollments(sectionId); }
    public Enrollment getEnrollmentByStudentAndCourse(int studentId, int courseId) { return enrollmentService.getEnrollmentByStudentAndCourse(studentId, courseId); }
    public List<Enrollment> getAllEnrollments() { return enrollmentService.getAllEnrollments(); }
    public boolean updateRegistrationStatus(int enrollmentId, String status) { return enrollmentService.updateRegistrationStatus(enrollmentId, status); }
    public boolean submitGrade(int enrollmentId, String finalGrade, double gpaPoints) { return enrollmentService.submitGrade(enrollmentId, finalGrade, gpaPoints); }
    public boolean updateAttendanceSummary(int enrollmentId, int attendedDays, int maxAttendance) { return enrollmentService.updateAttendanceSummary(enrollmentId, attendedDays, maxAttendance); }
}
