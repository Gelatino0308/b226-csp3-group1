package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.config.DbConnection;
import com.joysistvi.sigsys.model.CourseSection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseSectionRepositoryImpl implements CourseSectionRepository {

    public boolean createSection(CourseSection section) {
        String sql = "INSERT INTO course_sections (course_id, faculty_id, period_id, schedule_days, schedule_time, room, capacity) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, section.getCourseId());
            pstmt.setInt(2, section.getFacultyId());
            pstmt.setInt(3, section.getPeriodId());
            pstmt.setString(4, section.getScheduleDays());
            pstmt.setString(5, section.getScheduleTime());
            pstmt.setString(6, section.getRoom());
            pstmt.setInt(7, section.getCapacity());

            if (pstmt.executeUpdate() > 0) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) section.setSectionId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<CourseSection> getSectionsByPeriodId(int periodId) {
        List<CourseSection> list = new ArrayList<>();
        String sql = "SELECT * FROM course_sections WHERE period_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, periodId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSection(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<CourseSection> getSectionsByFacultyId(int facultyId) {
        List<CourseSection> list = new ArrayList<>();
        String sql = "SELECT * FROM course_sections WHERE faculty_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, facultyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) list.add(mapResultSetToSection(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<CourseSection> getSectionsByCourseId(int courseId) {
        List<CourseSection> list = new ArrayList<>();
        String sql = "SELECT * FROM course_sections WHERE course_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) list.add(mapResultSetToSection(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public CourseSection getSectionById(int sectionId) {
        String sql = "SELECT * FROM course_sections WHERE section_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sectionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapResultSetToSection(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteSection(int sectionId) {
        String sql = "DELETE FROM course_sections WHERE section_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sectionId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private CourseSection mapResultSetToSection(ResultSet rs) throws SQLException {
        CourseSection cs = new CourseSection();
        cs.setSectionId(rs.getInt("section_id"));
        cs.setCourseId(rs.getInt("course_id"));
        cs.setFacultyId(rs.getInt("faculty_id"));
        cs.setPeriodId(rs.getInt("period_id"));
        cs.setScheduleDays(rs.getString("schedule_days"));
        cs.setScheduleTime(rs.getString("schedule_time"));
        cs.setRoom(rs.getString("room"));
        cs.setCapacity(rs.getInt("capacity"));
        return cs;
    }
}
