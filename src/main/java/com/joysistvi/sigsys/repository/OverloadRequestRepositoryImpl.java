package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.config.DbConnection;
import com.joysistvi.sigsys.model.OverloadRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OverloadRequestRepositoryImpl implements OverloadRequestRepository {
    private boolean ensureTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS overload_requests ("
                + "request_id INT UNSIGNED NOT NULL AUTO_INCREMENT,"
                + "student_id INT UNSIGNED NOT NULL,"
                + "requested_units INT UNSIGNED NOT NULL,"
                + "status ENUM('PENDING','APPROVED','DISAPPROVED') NOT NULL DEFAULT 'PENDING',"
                + "requested_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                + "reviewed_at TIMESTAMP NULL DEFAULT NULL,"
                + "reviewed_by INT UNSIGNED NULL DEFAULT NULL,"
                + "PRIMARY KEY (request_id),"
                + "CONSTRAINT fk_overload_student FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,"
                + "CONSTRAINT fk_overload_reviewer FOREIGN KEY (reviewed_by) REFERENCES users(user_id) ON DELETE SET NULL"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
        return true;
    }

    public boolean create(OverloadRequest request) {
        String sql = "INSERT INTO overload_requests (student_id, requested_units) VALUES (?, ?)";
        try (Connection connection = DbConnection.getConnection()) {
            ensureTable(connection);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, request.getStudentId());
                statement.setInt(2, request.getRequestedUnits());
                return statement.executeUpdate() > 0;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public List<OverloadRequest> getPending() {
        return getByStatus("PENDING");
    }

    public List<OverloadRequest> getByStudent(int studentId) {
        List<OverloadRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM overload_requests WHERE student_id = ? ORDER BY request_id DESC";
        try (Connection connection = DbConnection.getConnection()) {
            ensureTable(connection);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, studentId);
                try (ResultSet result = statement.executeQuery()) {
                    while (result.next()) requests.add(map(result));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return requests;
    }

    public Integer getApprovedUnits(int studentId) {
        String sql = "SELECT requested_units FROM overload_requests WHERE student_id = ? "
                + "AND status = 'APPROVED' ORDER BY reviewed_at DESC, request_id DESC LIMIT 1";
        try (Connection connection = DbConnection.getConnection()) {
            ensureTable(connection);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, studentId);
                try (ResultSet result = statement.executeQuery()) {
                    return result.next() ? result.getInt("requested_units") : null;
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public boolean hasPending(int studentId) {
        String sql = "SELECT 1 FROM overload_requests WHERE student_id = ? AND status = 'PENDING' LIMIT 1";
        try (Connection connection = DbConnection.getConnection()) {
            ensureTable(connection);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, studentId);
                try (ResultSet result = statement.executeQuery()) { return result.next(); }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean updateStatus(int requestId, String status, int reviewedBy) {
        String sql = "UPDATE overload_requests SET status = ?, reviewed_at = CURRENT_TIMESTAMP, reviewed_by = ? "
                + "WHERE request_id = ? AND status = 'PENDING'";
        try (Connection connection = DbConnection.getConnection()) {
            ensureTable(connection);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, status);
                statement.setInt(2, reviewedBy);
                statement.setInt(3, requestId);
                return statement.executeUpdate() > 0;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private List<OverloadRequest> getByStatus(String status) {
        List<OverloadRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM overload_requests WHERE status = ? ORDER BY requested_at";
        try (Connection connection = DbConnection.getConnection()) {
            ensureTable(connection);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, status);
                try (ResultSet result = statement.executeQuery()) {
                    while (result.next()) requests.add(map(result));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return requests;
    }

    private OverloadRequest map(ResultSet result) throws SQLException {
        OverloadRequest request = new OverloadRequest();
        request.setRequestId(result.getInt("request_id"));
        request.setStudentId(result.getInt("student_id"));
        request.setRequestedUnits(result.getInt("requested_units"));
        request.setStatus(result.getString("status"));
        Timestamp requestedAt = result.getTimestamp("requested_at");
        Timestamp reviewedAt = result.getTimestamp("reviewed_at");
        request.setRequestedAt(requestedAt == null ? null : requestedAt.toLocalDateTime());
        request.setReviewedAt(reviewedAt == null ? null : reviewedAt.toLocalDateTime());
        int reviewedBy = result.getInt("reviewed_by");
        request.setReviewedBy(result.wasNull() ? null : reviewedBy);
        return request;
    }
}
