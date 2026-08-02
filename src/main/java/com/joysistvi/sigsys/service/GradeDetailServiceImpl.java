package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.dao.GradeDetailsDao;
import com.joysistvi.sigsys.model.GradeDetail;
import java.util.List;

public class GradeDetailServiceImpl implements GradeDetailService {
    private final GradeDetailsDao gradeDetailsDao = new GradeDetailsDao();

    @Override
    public boolean addGradeDetail(GradeDetail gradeDetail) {
        if (gradeDetail == null || gradeDetail.getEnrollmentId() <= 0 || gradeDetail.getAssessmentName() == null
                || gradeDetail.getAssessmentName().trim().isEmpty() || gradeDetail.getMaxScore() <= 0
                || gradeDetail.getScoreObtained() < 0 || gradeDetail.getScoreObtained() > gradeDetail.getMaxScore()
                || gradeDetail.getWeightPercentage() < 0 || gradeDetail.getWeightPercentage() > 100) {
            System.err.println("Score obtained cannot exceed maximum score.");
            return false;
        }
        return gradeDetailsDao.addGradeDetail(gradeDetail);
    }

    @Override
    public List<GradeDetail> getGradesForEnrollment(int enrollmentId) {
        return enrollmentId > 0 ? gradeDetailsDao.getGradesByEnrollment(enrollmentId) : java.util.Collections.emptyList();
    }
}
