package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.dao.AttendanceDao;
import com.joysistvi.sigsys.model.Attendance;
import java.util.List;

public class AttendanceRepositoryImpl implements AttendanceRepository {
    private final AttendanceDao attendanceDao = new AttendanceDao();

    @Override
    public boolean saveAttendance(Attendance attendance) {
        return attendanceDao.saveAttendance(attendance);
    }

    @Override
    public List<Attendance> getAttendanceByEnrollmentId(int enrollmentId) {
        return attendanceDao.getAttendanceByEnrollmentId(enrollmentId);
    }
}