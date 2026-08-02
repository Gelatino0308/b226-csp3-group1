package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.dao.AcademicPeriodDao;
import com.joysistvi.sigsys.model.AcademicPeriod;
import java.util.List;

public class AcademicPeriodServiceImpl implements AcademicPeriodService {
    private final AcademicPeriodDao periodDao = new AcademicPeriodDao();

    @Override
    public boolean addPeriod(AcademicPeriod period) {
        if (period == null || period.getTermName() == null || period.getTermName().trim().isEmpty()
                || period.getStartDate() == null || period.getEndDate() == null) return false;
        if (period.getStartDate().isAfter(period.getEndDate())) {
            System.err.println("Error: Start date cannot be after end date.");
            return false;
        }
        return periodDao.createPeriod(period);
    }

    @Override
    public List<AcademicPeriod> getAllPeriods() {
        return periodDao.getAllPeriods();
    }

    @Override
    public AcademicPeriod getActivePeriod() {
        return periodDao.getActivePeriod();
    }
}
