package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.dao.AcademicPeriodDao;
import com.joysistvi.sigsys.model.AcademicPeriod;
import java.util.List;

public class AcademicPeriodRepositoryImpl implements AcademicPeriodRepository {
    private final AcademicPeriodDao academicPeriodDao = new AcademicPeriodDao();

    @Override
    public boolean createPeriod(AcademicPeriod period) {
        return academicPeriodDao.createPeriod(period);
    }

    @Override
    public List<AcademicPeriod> getAllPeriods() {
        return academicPeriodDao.getAllPeriods();
    }

    @Override
    public AcademicPeriod getActivePeriod() {
        return academicPeriodDao.getActivePeriod();
    }
}