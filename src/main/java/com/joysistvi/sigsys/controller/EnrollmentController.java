package com.joysistvi.sigsys.controller;

import com.joysistvi.sigsys.dao.EnrollmentDao;
import com.joysistvi.sigsys.model.Course;
import com.joysistvi.sigsys.model.CourseSection;
import com.joysistvi.sigsys.model.Enrollment;
import com.joysistvi.sigsys.util.ScheduleRules;

import java.util.List;

public class EnrollmentController {
    private final EnrollmentDao enrollmentDao;
    private final CourseSectionController sectionController;
    private final CourseController courseController;
    private final SystemConfigController configController;
    private final OverloadRequestController overloadController;

    public EnrollmentController() {
        this.enrollmentDao = new EnrollmentDao();
        this.sectionController = new CourseSectionController();
        this.courseController = new CourseController();
        this.configController = new SystemConfigController();
        this.overloadController = new OverloadRequestController();
    }

    /**
     * Enrolls a student into a specific course section.
     */
    public boolean enrollStudent(int studentId, int sectionId) {
        if (studentId <= 0 || sectionId <= 0) {
            System.err.println("Error: Invalid Student ID or Section ID.");
            return false;
        }

        CourseSection section = sectionController.getSectionById(sectionId);
        Course course = section == null ? null : courseController.getCourseById(section.getCourseId());
        if (course == null) {
            System.err.println("Error: Course section does not have a valid course.");
            return false;
        }
        int enrolledCount = enrollmentDao.getEnrollmentsBySectionId(sectionId).size();
        if (section.getCapacity() > 0 && enrolledCount >= section.getCapacity()) {
            System.err.println("Registration denied: this course section is full.");
            return false;
        }
        int maximumUnits;
        try {
            maximumUnits = Integer.parseInt(configController.getConfig("MAX_CREDITS_PER_TERM"));
        } catch (Exception exception) {
            maximumUnits = 24;
        }
        Integer approvedUnits = overloadController.getApprovedMaxUnits(studentId);
        if (approvedUnits != null) maximumUnits = Math.max(maximumUnits, approvedUnits);
        if (!Boolean.parseBoolean(defaultValue(configController.getConfig("allow_class_conflicts"), "false"))
                && hasClassConflict(studentId, section)) {
            System.err.println("Registration denied: the class schedule conflicts with an enrolled course.");
            return false;
        }
        int currentUnits = 0;
        for (Enrollment enrollment : enrollmentDao.getEnrollmentsByStudentId(studentId)) {
            if (!"DROPPED".equalsIgnoreCase(enrollment.getRegistrationStatus())) {
                CourseSection enrolledSection = sectionController.getSectionById(enrollment.getSectionId());
                Course enrolledCourse = enrolledSection == null ? null
                        : courseController.getCourseById(enrolledSection.getCourseId());
                if (enrolledSection != null && enrolledSection.getCourseId() == section.getCourseId()) {
                    System.err.println("Registration denied: the student is already enrolled in this course.");
                    return false;
                }
                if (enrolledCourse != null) currentUnits += enrolledCourse.getCredits();
            }
        }
        if (currentUnits + course.getCredits() > maximumUnits) {
            System.err.printf("Registration denied: %d current units + %d credits exceeds the %d-unit limit.%n",
                    currentUnits, course.getCredits(), maximumUnits);
            System.err.println("Submit an overload request and wait for Admin approval.");
            return false;
        }

        // Logic check: Verify student isn't already enrolled in this section
        if (enrollmentDao.isAlreadyEnrolled(studentId, sectionId)) {
            System.err.println("Error: Student is already enrolled in this course section.");
            return false;
        }

        return enrollmentDao.createEnrollment(studentId, sectionId);
    }

    private boolean hasClassConflict(int studentId, CourseSection requestedSection) {
        List<CourseSection> enrolledSections = new java.util.ArrayList<>();
        for (Enrollment enrollment : enrollmentDao.getEnrollmentsByStudentId(studentId)) {
            if ("DROPPED".equalsIgnoreCase(enrollment.getRegistrationStatus())) continue;
            CourseSection enrolledSection = sectionController.getSectionById(enrollment.getSectionId());
            if (enrolledSection != null) enrolledSections.add(enrolledSection);
        }
        return ScheduleRules.conflicts(requestedSection, enrolledSections);
    }

    private String defaultValue(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }

    /**
     * Fetches all enrollments for a specific student.
     */
    public List<Enrollment> getStudentEnrollments(int studentId) {
        return enrollmentDao.getEnrollmentsByStudentId(studentId);
    }

    public List<Enrollment> getSectionEnrollments(int sectionId) {
        return sectionId > 0 ? enrollmentDao.getEnrollmentsBySectionId(sectionId) : java.util.Collections.emptyList();
    }

    public Enrollment getEnrollmentByStudentAndCourse(int studentId, int courseId) {
        return studentId > 0 && courseId > 0
                ? enrollmentDao.getEnrollmentByStudentAndCourse(studentId, courseId) : null;
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollmentDao.getAllEnrollments();
    }

    public boolean updateRegistrationStatus(int enrollmentId, String status) {
        if (enrollmentId <= 0 || status == null
                || !status.trim().toUpperCase().matches("PENDING|ENROLLED|DROPPED")) return false;
        return enrollmentDao.updateRegistrationStatus(enrollmentId, status.trim().toUpperCase());
    }

    /**
     * Updates the final grade and GPA points for an enrollment.
     */
    public boolean submitGrade(int enrollmentId, String finalGrade, double gpaPoints) {
        if (enrollmentId <= 0 || finalGrade == null || gpaPoints < 0.0 || gpaPoints > 4.0) {
            System.err.println("Error: Invalid enrollment record or GPA range.");
            return false;
        }
        Enrollment enrollment = enrollmentDao.getEnrollmentById(enrollmentId);
        if (enrollment == null || !"ENROLLED".equalsIgnoreCase(enrollment.getRegistrationStatus())) {
            System.err.println("Error: Final grades can only be submitted for enrolled students.");
            return false;
        }
        return enrollmentDao.updateGrade(enrollmentId, finalGrade, gpaPoints);
    }

    public boolean updateAttendanceSummary(int enrollmentId, int attendedDays, int maxAttendance) {
        if (enrollmentId <= 0 || attendedDays < 0 || maxAttendance <= 0 || attendedDays > maxAttendance
                || enrollmentDao.getEnrollmentById(enrollmentId) == null) return false;
        return enrollmentDao.updateAttendanceSummary(enrollmentId, attendedDays, maxAttendance);
    }
}
