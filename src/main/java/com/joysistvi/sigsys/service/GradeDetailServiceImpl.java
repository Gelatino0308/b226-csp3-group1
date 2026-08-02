package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.repository.GradeDetailRepository;
import com.joysistvi.sigsys.repository.GradeDetailRepositoryImpl;
import com.joysistvi.sigsys.repository.EnrollmentRepository;
import com.joysistvi.sigsys.repository.EnrollmentRepositoryImpl;
import com.joysistvi.sigsys.model.GradeDetail;
import java.util.List;

public class GradeDetailServiceImpl implements GradeDetailService {
    private final GradeDetailRepository gradeRepository = new GradeDetailRepositoryImpl();
    private final EnrollmentRepository enrollmentRepository = new EnrollmentRepositoryImpl();

    @Override
    public boolean addGradeDetail(GradeDetail gradeDetail) {
        if (gradeDetail == null || gradeDetail.getEnrollmentId() <= 0 || gradeDetail.getAssessmentName() == null
                || gradeDetail.getAssessmentName().trim().isEmpty() || gradeDetail.getMaxScore() <= 0
                || gradeDetail.getScoreObtained() < 0 || gradeDetail.getScoreObtained() > gradeDetail.getMaxScore()
                || gradeDetail.getWeightPercentage() < 0 || gradeDetail.getWeightPercentage() > 100) {
            System.err.println("Score obtained cannot exceed maximum score.");
            return false;
        }
        if (enrollmentRepository.getEnrollmentById(gradeDetail.getEnrollmentId()) == null) return false;
        return gradeRepository.addGradeDetail(gradeDetail);
    }

    @Override
    public List<GradeDetail> getGradesForEnrollment(int enrollmentId) {
        return enrollmentId > 0 ? gradeRepository.getGradesByEnrollment(enrollmentId) : java.util.Collections.emptyList();
    }

    @Override public boolean saveTermMark(int enrollmentId, String term, double score, double maxScore) {
        if (enrollmentId <= 0 || term == null || maxScore <= 0 || score < 0 || score > maxScore) return false;
        String normalizedTerm = term.trim().toUpperCase();
        if (!normalizedTerm.matches("PRELIM|MIDTERM|FINALS")) return false;
        com.joysistvi.sigsys.model.Enrollment enrollment = enrollmentRepository.getEnrollmentById(enrollmentId);
        if (enrollment == null || !"ENROLLED".equalsIgnoreCase(enrollment.getRegistrationStatus())) return false;
        if (enrollment.getFinalGrade() != null && !enrollment.getFinalGrade().trim().isEmpty()) return false;
        GradeDetail grade = new GradeDetail();
        grade.setEnrollmentId(enrollmentId); grade.setAssessmentName(normalizedTerm);
        grade.setScoreObtained(score); grade.setMaxScore(maxScore); grade.setWeightPercentage(100.0 / 3.0);
        return gradeRepository.saveAssessmentMark(grade);
    }
}
