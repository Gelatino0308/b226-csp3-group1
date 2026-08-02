package com.joysistvi.sigsys.dao;

import com.joysistvi.sigsys.config.DbConnection;
import com.joysistvi.sigsys.model.TranscriptRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TranscriptRequestDao {

    public boolean createRequest(int studentId) {
        String sql = "INSERT INTO transcript_requests (student_id, status) VALUES (?, 'PENDING')";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStatus(int requestId, String status, int processedByUserId) {
        String sql = "UPDATE transcript_requests SET status = ?, processed_by = ? WHERE request_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, processedByUserId);
            pstmt.setInt(3, requestId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<TranscriptRequest> getRequestsByStudentId(int studentId) {
        List<TranscriptRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM transcript_requests WHERE student_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToRequest(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<TranscriptRequest> getAllRequests() {
        List<TranscriptRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM transcript_requests ORDER BY request_date";
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapResultSetToRequest(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private TranscriptRequest mapResultSetToRequest(ResultSet rs) throws SQLException {
        TranscriptRequest tr = new TranscriptRequest();
        tr.setRequestId(rs.getInt("request_id"));
        tr.setStudentId(rs.getInt("student_id"));
        Timestamp requestDate = rs.getTimestamp("request_date");
        tr.setRequestDate(requestDate == null ? null : requestDate.toLocalDateTime());
        tr.setStatus(rs.getString("status"));
        Number processedBy = (Number) rs.getObject("processed_by");
        tr.setProcessedBy(processedBy == null ? null : processedBy.intValue());
        return tr;
    }
}
