package com.joysistvi.sigsys.controller;
import com.joysistvi.sigsys.model.Attendance;
import com.joysistvi.sigsys.service.AttendanceService;
import com.joysistvi.sigsys.service.AttendanceServiceImpl;
import java.sql.Date;
import java.util.List;
public class AttendanceController {
    private final AttendanceService attendanceService = new AttendanceServiceImpl();
    public boolean recordAttendance(int enrollmentId, Date date, String status) {
        Attendance attendance = new Attendance();
        attendance.setEnrollmentId(enrollmentId);
        if (date != null) attendance.setDate(date.toLocalDate());
        attendance.setStatus(status);
        return attendanceService.recordAttendance(attendance);
    }
    public List<Attendance> getAttendanceByEnrollment(int enrollmentId) { return attendanceService.getAttendanceByEnrollment(enrollmentId); }
}
