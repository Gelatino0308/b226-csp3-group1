package com.joysistvi.sigsys.model;

import java.time.LocalDate; // latest class to map date

public class AcademicPeriod {
    private int periodId;
    private String termName;
    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;
    private boolean isActive;

    public AcademicPeriod(int periodId, String termName, LocalDate startDate, LocalDate endDate, boolean isActive) {
        this.periodId = periodId;
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }

    public AcademicPeriod(String termName, LocalDate startDate, LocalDate endDate, boolean isActive) {
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }

    public AcademicPeriod() {
    }

    public int getPeriodId() {
        return periodId;
    }

    public void setPeriodId(int periodId) {
        this.periodId = periodId;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}
