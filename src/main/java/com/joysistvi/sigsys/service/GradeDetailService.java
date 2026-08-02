package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.model.GradeDetail;
import java.util.List;

public interface GradeDetailService {
    boolean addGradeDetail(GradeDetail gradeDetail);
    boolean saveTermMark(int enrollmentId, String term, double score, double maxScore);
    List<GradeDetail> getGradesForEnrollment(int enrollmentId);
}
