package com.joysistvi.sigsys.controller;
import com.joysistvi.sigsys.model.AcademicPeriod;
import com.joysistvi.sigsys.service.AcademicPeriodService;
import com.joysistvi.sigsys.service.AcademicPeriodServiceImpl;
import java.util.List;
public class AcademicPeriodController {
    private final AcademicPeriodService periodService = new AcademicPeriodServiceImpl();
    public boolean createPeriod(AcademicPeriod period) { return periodService.addPeriod(period); }
    public List<AcademicPeriod> getAllPeriods() { return periodService.getAllPeriods(); }
    public AcademicPeriod getActivePeriod() { return periodService.getActivePeriod(); }
}
