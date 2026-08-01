package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.config.DbConnection;
import com.joysistvi.sigsys.enumeration.UserRole;
import com.joysistvi.sigsys.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class UserRepositoryImpl implements UserRepository {

    private final DbConnection dbConnection; // Composition

    // Constructor injection
    public UserRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public User findRecordByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, username);
            ResultSet res = prep.executeQuery();

            if (res.next()) {
                return new User(
                        res.getInt("user_id"),
                        res.getString("username"),
                        res.getString("password_hash"),
                        UserRole.valueOf(res.getString("role")),
                        res.getString("email"),
                        res.getInt("is_active"),
                        res.getObject("created_at", LocalDateTime.class)
                );
            }

        } catch (SQLException e) {
            System.out.println("Find User By Username Error: " + e.getMessage());
        }

        return null;
    }
}
