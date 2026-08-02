package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.config.DbConnection;
import com.joysistvi.sigsys.model.Faculty;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacultyRepositoryImpl implements FacultyRepository {

    public boolean createFaculty(Faculty faculty) {
        String sql = "INSERT INTO faculty (user_id, first_name, last_name, department) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, faculty.getUserId());
            pstmt.setString(2, faculty.getFirstName());
            pstmt.setString(3, faculty.getLastName());
            pstmt.setString(4, faculty.getDepartment());

            if (pstmt.executeUpdate() > 0) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) faculty.setFacultyId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Faculty getFacultyById(int facultyId) {
        String sql = "SELECT * FROM faculty WHERE faculty_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, facultyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapResultSetToFaculty(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Faculty getFacultyByUserId(int userId) {
        String sql = "SELECT * FROM faculty WHERE user_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapResultSetToFaculty(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Faculty> getAllFaculty() {
        List<Faculty> list = new ArrayList<>();
        String sql = "SELECT * FROM faculty";
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) list.add(mapResultSetToFaculty(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Faculty mapResultSetToFaculty(ResultSet rs) throws SQLException {
        Faculty f = new Faculty();
        f.setFacultyId(rs.getInt("faculty_id"));
        f.setUserId(rs.getInt("user_id"));
        f.setFirstName(rs.getString("first_name"));
        f.setLastName(rs.getString("last_name"));
        f.setDepartment(rs.getString("department"));
        return f;
    }
}
