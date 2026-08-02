package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.repository.AcademicPeriodRepository;
import com.joysistvi.sigsys.repository.AcademicPeriodRepositoryImpl;
import com.joysistvi.sigsys.model.AcademicPeriod;
import java.util.List;

public class AcademicPeriodServiceImpl implements AcademicPeriodService {
    private final AcademicPeriodRepository periodRepository = new AcademicPeriodRepositoryImpl();

    @Override
    public boolean addPeriod(AcademicPeriod period) {
        if (period == null || period.getTermName() == null || period.getTermName().trim().isEmpty()
                || period.getStartDate() == null || period.getEndDate() == null) return false;
        if (period.getStartDate().isAfter(period.getEndDate())) {
            System.err.println("Error: Start date cannot be after end date.");
            return false;
        }
        return periodRepository.createPeriod(period);
    }

    @Override
    public List<AcademicPeriod> getAllPeriods() {
        return periodRepository.getAllPeriods();
    }

    @Override
    public AcademicPeriod getActivePeriod() {
        return periodRepository.getActivePeriod();
    }
}
