package com.joysistvi.sigsys.model;

import java.time.LocalDateTime;

public class Enrollment {
    private int enrollmentId;
    private int studentId;
    private int sectionId;
    private String registrationStatus;
    private String finalGrade;
    private Double gpaPoints;
    private LocalDateTime enrolledAt; // Added field
    private int attendedDays;
    private int maxAttendance;

    public Enrollment() {}

    // Getters and Setters
    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getSectionId() { return sectionId; }
    public void setSectionId(int sectionId) { this.sectionId = sectionId; }

    public String getRegistrationStatus() { return registrationStatus; }
    public void setRegistrationStatus(String registrationStatus) { this.registrationStatus = registrationStatus; }

    public String getFinalGrade() { return finalGrade; }
    public void setFinalGrade(String finalGrade) { this.finalGrade = finalGrade; }

    public Double getGpaPoints() { return gpaPoints; }
    public void setGpaPoints(Double gpaPoints) { this.gpaPoints = gpaPoints; }

    // ADDED: Getter and Setter for enrolledAt
    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(LocalDateTime enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public int getAttendedDays() { return attendedDays; }
    public void setAttendedDays(int attendedDays) { this.attendedDays = attendedDays; }

    public int getMaxAttendance() { return maxAttendance; }
    public void setMaxAttendance(int maxAttendance) { this.maxAttendance = maxAttendance; }
}
