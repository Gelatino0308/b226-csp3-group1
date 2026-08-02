package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.dao.GradeDetailsDao;
import com.joysistvi.sigsys.model.GradeDetail;
import java.util.List;

public class GradeDetailRepositoryImpl implements GradeDetailRepository {
    private final GradeDetailsDao gradeDetailsDao = new GradeDetailsDao();

    @Override
    public boolean addGradeDetail(GradeDetail gradeDetail) {
        return gradeDetailsDao.addGradeDetail(gradeDetail);
    }

    @Override
    public List<GradeDetail> getGradesByEnrollment(int enrollmentId) {
        return gradeDetailsDao.getGradesByEnrollment(enrollmentId);
    }
}