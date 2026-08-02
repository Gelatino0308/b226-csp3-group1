package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.model.Attendance;
import java.util.List;

public interface AttendanceService {
    boolean recordAttendance(Attendance attendance);
    List<Attendance> getAttendanceByEnrollment(int enrollmentId);
}