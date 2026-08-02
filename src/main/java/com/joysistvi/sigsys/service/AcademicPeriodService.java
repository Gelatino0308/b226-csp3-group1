package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.model.AcademicPeriod;
import java.util.List;

public interface AcademicPeriodService {
    boolean addPeriod(AcademicPeriod period);
    List<AcademicPeriod> getAllPeriods();
    AcademicPeriod getActivePeriod();
}