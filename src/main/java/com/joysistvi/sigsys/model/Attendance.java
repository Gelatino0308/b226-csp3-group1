package com.joysistvi.sigsys.model;

import java.time.LocalDate; // latest class to map date

public class Attendance {
    private int attendanceId;
    private int enrollmentId; // FK to enrollments table
    private LocalDate date;
    private String status; // Enum: 'PRESENT', 'ABSENT', 'LATE', 'EXCUSED'

    public Attendance(int attendanceId, int enrollmentId, LocalDate date, String status) {
        this.attendanceId = attendanceId;
        this.enrollmentId = enrollmentId;
        this.date = date;
        this.status = status;
    }

    public Attendance(int enrollmentId, LocalDate date, String status) {
        this.enrollmentId = enrollmentId;
        this.date = date;
        this.status = status;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
