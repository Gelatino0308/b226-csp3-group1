package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.config.DbConnection;
import com.joysistvi.sigsys.model.Enrollment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentRepositoryImpl implements EnrollmentRepository {

    public boolean isAlreadyEnrolled(int studentId, int sectionId) {
        String sql = "SELECT 1 FROM enrollments WHERE student_id = ? AND section_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, sectionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createEnrollment(int studentId, int sectionId) {
        String sql = "INSERT INTO enrollments (student_id, section_id, registration_status) VALUES (?, ?, 'PENDING')";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, sectionId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Enrollment> getEnrollmentsByStudentId(int studentId) {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE student_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Enrollment e = new Enrollment();
                    e.setEnrollmentId(rs.getInt("enrollment_id"));
                    e.setStudentId(rs.getInt("student_id"));
                    e.setSectionId(rs.getInt("section_id"));
                    e.setRegistrationStatus(rs.getString("registration_status"));
                    e.setFinalGrade(rs.getString("final_grade"));
                    Object gpaPoints = rs.getObject("gpa_points");
                    e.setGpaPoints(gpaPoints == null ? null : ((Number) gpaPoints).doubleValue());
                    Timestamp enrolledAt = rs.getTimestamp("enrolled_at");
                    e.setEnrolledAt(enrolledAt == null ? null : enrolledAt.toLocalDateTime());
                    e.setAttendedDays(rs.getInt("attended_days"));
                    e.setMaxAttendance(rs.getInt("max_attendance"));
                    list.add(e);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Enrollment> getEnrollmentsBySectionId(int sectionId) {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE section_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sectionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) list.add(mapResultSetToEnrollment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Enrollment getEnrollmentById(int enrollmentId) {
        String sql = "SELECT * FROM enrollments WHERE enrollment_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, enrollmentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapResultSetToEnrollment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Enrollment getEnrollmentByStudentAndCourse(int studentId, int courseId) {
        String sql = "SELECT e.* FROM enrollments e "
                + "JOIN course_sections cs ON cs.section_id = e.section_id "
                + "WHERE e.student_id = ? AND cs.course_id = ? "
                + "ORDER BY e.enrollment_id DESC LIMIT 1";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapResultSetToEnrollment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Enrollment> getAllEnrollments() {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollments ORDER BY enrollment_id";
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapResultSetToEnrollment(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateRegistrationStatus(int enrollmentId, String status) {
        String sql = "UPDATE enrollments SET registration_status = ? WHERE enrollment_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, enrollmentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateGrade(int enrollmentId, String finalGrade, double gpaPoints) {
        String sql = "UPDATE enrollments SET final_grade = ?, gpa_points = ? WHERE enrollment_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, finalGrade);
            pstmt.setDouble(2, gpaPoints);
            pstmt.setInt(3, enrollmentId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAttendanceSummary(int enrollmentId, int attendedDays, int maxAttendance) {
        String sql = "UPDATE enrollments SET attended_days = ?, max_attendance = ? WHERE enrollment_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, attendedDays);
            pstmt.setInt(2, maxAttendance);
            pstmt.setInt(3, enrollmentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Enrollment mapResultSetToEnrollment(ResultSet rs) throws SQLException {
        Enrollment e = new Enrollment();
        e.setEnrollmentId(rs.getInt("enrollment_id"));
        e.setStudentId(rs.getInt("student_id"));
        e.setSectionId(rs.getInt("section_id"));
        e.setRegistrationStatus(rs.getString("registration_status"));
        e.setFinalGrade(rs.getString("final_grade"));
        Object gpaPoints = rs.getObject("gpa_points");
        e.setGpaPoints(gpaPoints == null ? null : ((Number) gpaPoints).doubleValue());
        Timestamp enrolledAt = rs.getTimestamp("enrolled_at");
        e.setEnrolledAt(enrolledAt == null ? null : enrolledAt.toLocalDateTime());
        e.setAttendedDays(rs.getInt("attended_days"));
        e.setMaxAttendance(rs.getInt("max_attendance"));
        return e;
    }
}
