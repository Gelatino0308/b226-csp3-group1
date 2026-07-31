package com.joysistvi.sigsys.model;

import java.time.LocalDateTime; // latest class to map timestamp

public class Enrollment {
    private int enrollmentId;
    private int studentId; // FK to students table
    private int sectionId; // FK to course_sections table
    private String registrationStatus; // Enum: 'PENDING', 'APPROVED', 'ENROLLED', 'DROPPED'
    private String finalGrade;
    private double gpaPoints;
    private LocalDateTime enrolledAt;
    private String studentFirstName;
    private String studentLastName;

    public Enrollment(int enrollmentId, int studentId, int sectionId, String registrationStatus, String finalGrade, double gpaPoints, LocalDateTime enrolledAt) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.sectionId = sectionId;
        this.registrationStatus = registrationStatus;
        this.finalGrade = finalGrade;
        this.gpaPoints = gpaPoints;
        this.enrolledAt = enrolledAt;
    }

    public Enrollment(int enrollmentId, int sectionId, String registrationStatus, String finalGrade, double gpaPoints, LocalDateTime enrolledAt, String studentFirstName, String studentLastName) {
        this.enrollmentId = enrollmentId;
        this.sectionId = sectionId;
        this.registrationStatus = registrationStatus;
        this.finalGrade = finalGrade;
        this.gpaPoints = gpaPoints;
        this.enrolledAt = enrolledAt;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
    }

    public Enrollment(int studentId, int sectionId, String registrationStatus, String finalGrade, double gpaPoints, LocalDateTime enrolledAt) {
        this.studentId = studentId;
        this.sectionId = sectionId;
        this.registrationStatus = registrationStatus;
        this.finalGrade = finalGrade;
        this.gpaPoints = gpaPoints;
        this.enrolledAt = enrolledAt;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(String finalGrade) {
        this.finalGrade = finalGrade;
    }

    public double getGpaPoints() {
        return gpaPoints;
    }

    public void setGpaPoints(double gpaPoints) {
        this.gpaPoints = gpaPoints;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(LocalDateTime enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public void setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName;
    }
}
