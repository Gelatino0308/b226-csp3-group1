package com.joysistvi.sigsys.controller;
import com.joysistvi.sigsys.model.GradeDetail;
import com.joysistvi.sigsys.service.GradeDetailService;
import com.joysistvi.sigsys.service.GradeDetailServiceImpl;
import java.util.List;
public class GradeDetailsController {
    private final GradeDetailService gradeService = new GradeDetailServiceImpl();
    public boolean recordGradeDetail(GradeDetail gradeDetail) { return gradeService.addGradeDetail(gradeDetail); }
    public boolean saveTermMark(int enrollmentId, String term, double score, double maxScore) { return gradeService.saveTermMark(enrollmentId, term, score, maxScore); }
    public List<GradeDetail> getGradesForEnrollment(int enrollmentId) { return gradeService.getGradesForEnrollment(enrollmentId); }
}
