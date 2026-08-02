package com.joysistvi.sigsys.controller;

import com.joysistvi.sigsys.dao.GradeDetailsDao;
import com.joysistvi.sigsys.dao.EnrollmentDao;
import com.joysistvi.sigsys.model.GradeDetail;

import java.util.List;

public class GradeDetailsController {
    private final GradeDetailsDao gradeDetailsDao;
    private final EnrollmentDao enrollmentDao;

    public GradeDetailsController() {
        this.gradeDetailsDao = new GradeDetailsDao();
        this.enrollmentDao = new EnrollmentDao();
    }

    public boolean recordGradeDetail(GradeDetail gradeDetail) {
        if (gradeDetail == null || gradeDetail.getEnrollmentId() <= 0) {
            System.err.println("Error: Invalid enrollment association.");
            return false;
        }
        if (gradeDetail.getMaxScore() <= 0 || gradeDetail.getWeightPercentage() < 0
                || gradeDetail.getWeightPercentage() > 100 || gradeDetail.getScoreObtained() < 0
                || gradeDetail.getScoreObtained() > gradeDetail.getMaxScore()) {
            System.err.println("Error: Score obtained cannot exceed max score or be negative.");
            return false;
        }
        if (enrollmentDao.getEnrollmentById(gradeDetail.getEnrollmentId()) == null) {
            System.err.println("Error: Enrollment ID " + gradeDetail.getEnrollmentId() + " does not exist.");
            return false;
        }
        return gradeDetailsDao.addGradeDetail(gradeDetail);
    }

    public boolean saveTermMark(int enrollmentId, String term, double score, double maxScore) {
        if (enrollmentId <= 0 || term == null || maxScore <= 0 || score < 0 || score > maxScore) return false;
        String normalizedTerm = term.trim().toUpperCase();
        if (!normalizedTerm.matches("PRELIM|MIDTERM|FINALS")) return false;
        com.joysistvi.sigsys.model.Enrollment enrollment = enrollmentDao.getEnrollmentById(enrollmentId);
        if (enrollment == null) return false;
        if (!"ENROLLED".equalsIgnoreCase(enrollment.getRegistrationStatus())) {
            System.err.println("Error: Grades can only be entered for enrolled students.");
            return false;
        }
        if (enrollment.getFinalGrade() != null && !enrollment.getFinalGrade().trim().isEmpty()) {
            System.err.println("Error: Final grade already submitted; marks are locked.");
            return false;
        }
        GradeDetail grade = new GradeDetail();
        grade.setEnrollmentId(enrollmentId);
        grade.setAssessmentName(normalizedTerm);
        grade.setScoreObtained(score);
        grade.setMaxScore(maxScore);
        grade.setWeightPercentage(100.0 / 3.0);
        return gradeDetailsDao.saveAssessmentMark(grade);
    }

    public List<GradeDetail> getGradesForEnrollment(int enrollmentId) {
        if (enrollmentId <= 0) {
            System.err.println("Error: Invalid Enrollment ID.");
            return List.of();
        }
        return gradeDetailsDao.getGradesByEnrollment(enrollmentId);
    }
}
