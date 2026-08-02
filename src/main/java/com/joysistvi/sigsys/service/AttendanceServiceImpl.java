package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.dao.AttendanceDao;
import com.joysistvi.sigsys.model.Attendance;
import java.util.List;

public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceDao attendanceDao = new AttendanceDao();

    @Override
    public boolean recordAttendance(Attendance attendance) {
        if (attendance == null || attendance.getEnrollmentId() <= 0 || attendance.getDate() == null
                || attendance.getStatus() == null
                || !attendance.getStatus().trim().toUpperCase().matches("PRESENT|ABSENT|LATE|EXCUSED")) {
            return false;
        }
        attendance.setStatus(attendance.getStatus().trim().toUpperCase());
        return attendanceDao.saveAttendance(attendance);
    }

    @Override
    public List<Attendance> getAttendanceByEnrollment(int enrollmentId) {
        return enrollmentId > 0 ? attendanceDao.getAttendanceByEnrollmentId(enrollmentId) : java.util.Collections.emptyList();
    }
}
