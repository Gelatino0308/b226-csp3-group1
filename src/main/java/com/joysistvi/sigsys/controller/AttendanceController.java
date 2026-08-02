package com.joysistvi.sigsys.controller;

import com.joysistvi.sigsys.dao.AttendanceDao;
import com.joysistvi.sigsys.dao.EnrollmentDao;
import com.joysistvi.sigsys.model.Attendance;

import java.sql.Date;
import java.util.List;

public class AttendanceController {
    private final AttendanceDao attendanceDao;
    private final EnrollmentDao enrollmentDao;

    public AttendanceController() {
        this.attendanceDao = new AttendanceDao();
        this.enrollmentDao = new EnrollmentDao();
    }

    /**
     * Logs attendance for a specific enrollment record.
     */
    public boolean recordAttendance(int enrollmentId, Date date, String status) {
        // Validate enum status matching the database constraints
        if (enrollmentId <= 0 || date == null || status == null
                || !status.trim().toUpperCase().matches("PRESENT|ABSENT|LATE|EXCUSED")) {
            System.err.println("Error: Invalid status. Must be PRESENT, ABSENT, LATE, or EXCUSED.");
            return false;
        }
        if (enrollmentDao.getEnrollmentById(enrollmentId) == null) {
            System.err.println("Error: Enrollment ID " + enrollmentId + " does not exist.");
            return false;
        }

        Attendance attendance = new Attendance();
        attendance.setEnrollmentId(enrollmentId);
        attendance.setDate(date.toLocalDate());
        attendance.setStatus(status.trim().toUpperCase());

        return attendanceDao.saveAttendance(attendance);
    }

    public List<Attendance> getAttendanceByEnrollment(int enrollmentId) {
        return attendanceDao.getAttendanceByEnrollmentId(enrollmentId);
    }
}
