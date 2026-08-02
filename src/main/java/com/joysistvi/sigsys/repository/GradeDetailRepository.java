package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.model.GradeDetail;
import java.util.List;

public interface GradeDetailRepository {
    boolean addGradeDetail(GradeDetail gradeDetail);
    boolean saveAssessmentMark(GradeDetail gradeDetail);
    List<GradeDetail> getGradesByEnrollment(int enrollmentId);
}
