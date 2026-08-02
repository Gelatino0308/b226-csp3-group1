package com.joysistvi.sigsys.dao;

import com.joysistvi.sigsys.config.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.joysistvi.sigsys.model.SystemConfig;

public class SystemConfigDao {

    public String getValueByKey(String key) {
        String sql = "SELECT setting_value FROM system_configs WHERE setting_key = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, key);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("setting_value");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateValue(String key, String value) {
        String sql = "INSERT INTO system_configs (setting_key, setting_value) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE setting_value = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, key);
            pstmt.setString(2, value);
            pstmt.setString(3, value);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<SystemConfig> getAll() {
        List<SystemConfig> configs = new ArrayList<>();
        String sql = "SELECT config_id, setting_key, setting_value FROM system_configs ORDER BY setting_key";
        try (Connection conn = DbConnection.getConnection();
             Statement statement = conn.createStatement();
             ResultSet result = statement.executeQuery(sql)) {
            while (result.next()) {
                configs.add(new SystemConfig(result.getInt("config_id"),
                        result.getString("setting_key"), result.getString("setting_value")));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return configs;
    }
}
