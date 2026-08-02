package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.repository.AttendanceRepository;
import com.joysistvi.sigsys.repository.AttendanceRepositoryImpl;
import com.joysistvi.sigsys.repository.EnrollmentRepository;
import com.joysistvi.sigsys.repository.EnrollmentRepositoryImpl;
import com.joysistvi.sigsys.model.Attendance;
import java.util.List;

public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository = new AttendanceRepositoryImpl();
    private final EnrollmentRepository enrollmentRepository = new EnrollmentRepositoryImpl();

    @Override
    public boolean recordAttendance(Attendance attendance) {
        if (attendance == null || attendance.getEnrollmentId() <= 0 || attendance.getDate() == null
                || attendance.getStatus() == null
                || !attendance.getStatus().trim().toUpperCase().matches("PRESENT|ABSENT|LATE|EXCUSED")) {
            return false;
        }
        attendance.setStatus(attendance.getStatus().trim().toUpperCase());
        if (enrollmentRepository.getEnrollmentById(attendance.getEnrollmentId()) == null) return false;
        return attendanceRepository.saveAttendance(attendance);
    }

    @Override
    public List<Attendance> getAttendanceByEnrollment(int enrollmentId) {
        return enrollmentId > 0 ? attendanceRepository.getAttendanceByEnrollmentId(enrollmentId) : java.util.Collections.emptyList();
    }
}
