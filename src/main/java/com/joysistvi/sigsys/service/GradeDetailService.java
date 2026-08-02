package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.model.GradeDetail;
import java.util.List;

public interface GradeDetailService {
    boolean addGradeDetail(GradeDetail gradeDetail);
    List<GradeDetail> getGradesForEnrollment(int enrollmentId);
}