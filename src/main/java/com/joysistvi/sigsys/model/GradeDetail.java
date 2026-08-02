package com.joysistvi.sigsys.model;

import java.time.LocalDateTime;

public class GradeDetail {
    private int gradeDetailId;
    private int enrollmentId; // FK to enrollments table
    private String assessmentName;
    private double scoreObtained;
    private double maxScore;
    private double weightPercentage;
    private java.time.LocalDateTime lastUpdated;

    public GradeDetail(int gradeDetailId, int enrollmentId, String assessmentName, double scoreObtained, double maxScore, double weightPercentage, LocalDateTime lastUpdated) {
        this.gradeDetailId = gradeDetailId;
        this.enrollmentId = enrollmentId;
        this.assessmentName = assessmentName;
        this.scoreObtained = scoreObtained;
        this.maxScore = maxScore;
        this.weightPercentage = weightPercentage;
        this.lastUpdated = lastUpdated;
    }

    public GradeDetail(int enrollmentId, String assessmentName, double scoreObtained, double maxScore, double weightPercentage, LocalDateTime lastUpdated) {
        this.enrollmentId = enrollmentId;
        this.assessmentName = assessmentName;
        this.scoreObtained = scoreObtained;
        this.maxScore = maxScore;
        this.weightPercentage = weightPercentage;
        this.lastUpdated = lastUpdated;
    }

    public GradeDetail() {
    }

    public int getGradeDetailId() {
        return gradeDetailId;
    }

    public void setGradeDetailId(int gradeDetailId) {
        this.gradeDetailId = gradeDetailId;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public double getScoreObtained() {
        return scoreObtained;
    }

    public void setScoreObtained(double scoreObtained) {
        this.scoreObtained = scoreObtained;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public double getWeightPercentage() {
        return weightPercentage;
    }

    public void setWeightPercentage(double weightPercentage) {
        this.weightPercentage = weightPercentage;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
