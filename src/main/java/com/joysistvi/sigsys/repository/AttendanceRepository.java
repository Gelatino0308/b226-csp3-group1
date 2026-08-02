package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.model.Attendance;
import java.util.List;

public interface AttendanceRepository {
    boolean saveAttendance(Attendance attendance);
    List<Attendance> getAttendanceByEnrollmentId(int enrollmentId);
}