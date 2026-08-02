package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.config.DbConnection;
import com.joysistvi.sigsys.model.Attendance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceRepositoryImpl implements AttendanceRepository {

    public boolean saveAttendance(Attendance attendance) {
        String sql = "INSERT INTO attendance (enrollment_id, date, status) VALUES (?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, attendance.getEnrollmentId());
            pstmt.setDate(2, Date.valueOf(attendance.getDate()));
            pstmt.setString(3, attendance.getStatus());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Attendance> getAttendanceByEnrollmentId(int enrollmentId) {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE enrollment_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, enrollmentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Attendance a = new Attendance();
                    a.setAttendanceId(rs.getInt("attendance_id"));
                    a.setEnrollmentId(rs.getInt("enrollment_id"));
                    a.setDate(rs.getDate("date").toLocalDate());
                    a.setStatus(rs.getString("status"));
                    list.add(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
