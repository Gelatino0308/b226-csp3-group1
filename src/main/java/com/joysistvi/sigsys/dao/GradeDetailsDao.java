package com.joysistvi.sigsys.dao;

import com.joysistvi.sigsys.config.DbConnection;
import com.joysistvi.sigsys.model.GradeDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDetailsDao {

    public boolean addGradeDetail(GradeDetail grade) {
        String sql = "INSERT INTO grade_details (enrollment_id, assessment_name, score_obtained, max_score, weight_percentage) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, grade.getEnrollmentId());
            pstmt.setString(2, grade.getAssessmentName());
            pstmt.setDouble(3, grade.getScoreObtained());
            pstmt.setDouble(4, grade.getMaxScore());
            pstmt.setDouble(5, grade.getWeightPercentage());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveAssessmentMark(GradeDetail grade) {
        String updateSql = "UPDATE grade_details SET score_obtained = ?, max_score = ?, weight_percentage = ? "
                + "WHERE enrollment_id = ? AND assessment_name = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement update = conn.prepareStatement(updateSql)) {
            update.setDouble(1, grade.getScoreObtained());
            update.setDouble(2, grade.getMaxScore());
            update.setDouble(3, grade.getWeightPercentage());
            update.setInt(4, grade.getEnrollmentId());
            update.setString(5, grade.getAssessmentName());
            if (update.executeUpdate() > 0) return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return addGradeDetail(grade);
    }

    public List<GradeDetail> getGradesByEnrollment(int enrollmentId) {
        List<GradeDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM grade_details WHERE enrollment_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, enrollmentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    GradeDetail gd = new GradeDetail();
                    gd.setGradeDetailId(rs.getInt("grade_detail_id"));
                    gd.setEnrollmentId(rs.getInt("enrollment_id"));
                    gd.setAssessmentName(rs.getString("assessment_name"));
                    gd.setScoreObtained(rs.getDouble("score_obtained"));
                    gd.setMaxScore(rs.getDouble("max_score"));
                    gd.setWeightPercentage(rs.getDouble("weight_percentage"));
                    Timestamp lastUpdated = rs.getTimestamp("last_updated");
                    gd.setLastUpdated(lastUpdated == null ? null : lastUpdated.toLocalDateTime());
                    list.add(gd);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
