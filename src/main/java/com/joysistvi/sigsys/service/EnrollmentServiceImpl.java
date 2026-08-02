package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.model.Course;
import com.joysistvi.sigsys.model.CourseSection;
import com.joysistvi.sigsys.model.Enrollment;
import com.joysistvi.sigsys.repository.CourseRepository;
import com.joysistvi.sigsys.repository.CourseRepositoryImpl;
import com.joysistvi.sigsys.repository.CourseSectionRepository;
import com.joysistvi.sigsys.repository.CourseSectionRepositoryImpl;
import com.joysistvi.sigsys.repository.EnrollmentRepository;
import com.joysistvi.sigsys.repository.EnrollmentRepositoryImpl;
import com.joysistvi.sigsys.repository.OverloadRequestRepository;
import com.joysistvi.sigsys.repository.OverloadRequestRepositoryImpl;
import com.joysistvi.sigsys.repository.SystemConfigRepository;
import com.joysistvi.sigsys.repository.SystemConfigRepositoryImpl;
import com.joysistvi.sigsys.util.ScheduleRules;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository = new EnrollmentRepositoryImpl();
    private final CourseRepository courseRepository = new CourseRepositoryImpl();
    private final CourseSectionRepository sectionRepository = new CourseSectionRepositoryImpl();
    private final SystemConfigRepository configRepository = new SystemConfigRepositoryImpl();
    private final OverloadRequestRepository overloadRepository = new OverloadRequestRepositoryImpl();

    @Override
    public boolean enrollStudent(int studentId, int sectionId) {
        if (studentId <= 0 || sectionId <= 0) return false;
        CourseSection section = sectionRepository.getSectionById(sectionId);
        Course course = section == null ? null : courseRepository.getCourseById(section.getCourseId());
        if (course == null) return false;
        if (section.getCapacity() > 0 && enrollmentRepository.getEnrollmentsBySectionId(sectionId).size() >= section.getCapacity()) return false;
        int maximumUnits;
        try { maximumUnits = Integer.parseInt(configRepository.getValueByKey("MAX_CREDITS_PER_TERM")); }
        catch (Exception exception) { maximumUnits = 24; }
        Integer approvedUnits = overloadRepository.getApprovedUnits(studentId);
        if (approvedUnits != null) maximumUnits = Math.max(maximumUnits, approvedUnits);
        List<CourseSection> enrolledSections = new ArrayList<>();
        int currentUnits = 0;
        for (Enrollment enrollment : enrollmentRepository.getEnrollmentsByStudentId(studentId)) {
            if ("DROPPED".equalsIgnoreCase(enrollment.getRegistrationStatus())) continue;
            CourseSection enrolledSection = sectionRepository.getSectionById(enrollment.getSectionId());
            if (enrolledSection != null) {
                enrolledSections.add(enrolledSection);
                if (enrolledSection.getCourseId() == section.getCourseId()) return false;
                Course enrolledCourse = courseRepository.getCourseById(enrolledSection.getCourseId());
                if (enrolledCourse != null) currentUnits += enrolledCourse.getCredits();
            }
        }
        if (!Boolean.parseBoolean(defaultValue(configRepository.getValueByKey("allow_class_conflicts"), "false"))
                && ScheduleRules.conflicts(section, enrolledSections)) return false;
        if (currentUnits + course.getCredits() > maximumUnits) return false;
        if (enrollmentRepository.isAlreadyEnrolled(studentId, sectionId)) return false;
        return enrollmentRepository.createEnrollment(studentId, sectionId);
    }

    @Override public List<Enrollment> getStudentEnrollments(int studentId) {
        return studentId > 0 ? enrollmentRepository.getEnrollmentsByStudentId(studentId) : java.util.Collections.emptyList();
    }
    @Override public List<Enrollment> getSectionEnrollments(int sectionId) {
        return sectionId > 0 ? enrollmentRepository.getEnrollmentsBySectionId(sectionId) : java.util.Collections.emptyList();
    }
    @Override public Enrollment getEnrollmentByStudentAndCourse(int studentId, int courseId) {
        return studentId > 0 && courseId > 0 ? enrollmentRepository.getEnrollmentByStudentAndCourse(studentId, courseId) : null;
    }
    @Override public Enrollment getEnrollmentById(int enrollmentId) {
        return enrollmentId > 0 ? enrollmentRepository.getEnrollmentById(enrollmentId) : null;
    }
    @Override public List<Enrollment> getAllEnrollments() { return enrollmentRepository.getAllEnrollments(); }
    @Override public boolean updateRegistrationStatus(int enrollmentId, String status) {
        return enrollmentId > 0 && status != null && status.trim().toUpperCase().matches("PENDING|ENROLLED|DROPPED")
                && enrollmentRepository.updateRegistrationStatus(enrollmentId, status.trim().toUpperCase());
    }
    @Override public boolean submitGrade(int enrollmentId, String finalGrade, double gpaPoints) {
        if (enrollmentId <= 0 || finalGrade == null || finalGrade.trim().isEmpty() || Double.isNaN(gpaPoints)
                || Double.isInfinite(gpaPoints) || gpaPoints < 0.0 || gpaPoints > 4.0) return false;
        Enrollment enrollment = enrollmentRepository.getEnrollmentById(enrollmentId);
        return enrollment != null && "ENROLLED".equalsIgnoreCase(enrollment.getRegistrationStatus())
                && enrollmentRepository.updateGrade(enrollmentId, finalGrade.trim(), gpaPoints);
    }
    @Override public boolean updateAttendanceSummary(int enrollmentId, int attendedDays, int maxAttendance) {
        return enrollmentId > 0 && attendedDays >= 0 && maxAttendance > 0 && attendedDays <= maxAttendance
                && enrollmentRepository.getEnrollmentById(enrollmentId) != null
                && enrollmentRepository.updateAttendanceSummary(enrollmentId, attendedDays, maxAttendance);
    }
    private String defaultValue(String value, String fallback) { return value == null || value.trim().isEmpty() ? fallback : value.trim(); }
}
