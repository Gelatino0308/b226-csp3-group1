package com.joysistvi.sigsys.controller;

import com.joysistvi.sigsys.dao.AcademicPeriodDao;
import com.joysistvi.sigsys.model.AcademicPeriod;
import java.util.List;

public class AcademicPeriodController {
    private final AcademicPeriodDao periodDao;

    public AcademicPeriodController() {
        this.periodDao = new AcademicPeriodDao();
    }

    public boolean createPeriod(AcademicPeriod period) {
        if (period == null || period.getStartDate() == null || period.getEndDate() == null
                || period.getTermName() == null || period.getTermName().trim().isEmpty()) {
            System.err.println("Error: Term name and dates are required.");
            return false;
        }
        if (period.getStartDate().isAfter(period.getEndDate())) {
            System.err.println("Error: Start date cannot be after end date.");
            return false;
        }
        return periodDao.createPeriod(period);
    }

    public List<AcademicPeriod> getAllPeriods() {
        return periodDao.getAllPeriods();
    }

    public AcademicPeriod getActivePeriod() {
        return periodDao.getActivePeriod();
    }
}
