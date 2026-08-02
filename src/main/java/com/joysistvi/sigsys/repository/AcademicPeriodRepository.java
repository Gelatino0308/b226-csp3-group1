package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.model.AcademicPeriod;
import java.util.List;

public interface AcademicPeriodRepository {
    boolean createPeriod(AcademicPeriod period);
    List<AcademicPeriod> getAllPeriods();
    AcademicPeriod getActivePeriod();
}