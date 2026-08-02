package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.config.DbConnection;
import com.joysistvi.sigsys.model.AcademicPeriod;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AcademicPeriodRepositoryImpl implements AcademicPeriodRepository {

    public boolean createPeriod(AcademicPeriod period) {
        String sql = "INSERT INTO academic_periods (term_name, start_date, end_date, is_active) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, period.getTermName());
            pstmt.setDate(2, Date.valueOf(period.getStartDate()));
            pstmt.setDate(3, Date.valueOf(period.getEndDate()));
            pstmt.setBoolean(4, period.isActive());

            if (pstmt.executeUpdate() > 0) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) period.setPeriodId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<AcademicPeriod> getAllPeriods() {
        List<AcademicPeriod> list = new ArrayList<>();
        String sql = "SELECT * FROM academic_periods";
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AcademicPeriod p = mapResultSetToPeriod(rs);
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public AcademicPeriod getActivePeriod() {
        String sql = "SELECT * FROM academic_periods WHERE is_active = 1 LIMIT 1";
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return mapResultSetToPeriod(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private AcademicPeriod mapResultSetToPeriod(ResultSet rs) throws SQLException {
        AcademicPeriod p = new AcademicPeriod();
        p.setPeriodId(rs.getInt("period_id"));
        p.setTermName(rs.getString("term_name"));
        p.setStartDate(rs.getDate("start_date").toLocalDate());
        p.setEndDate(rs.getDate("end_date").toLocalDate());
        p.setActive(rs.getBoolean("is_active"));
        return p;
    }
}
