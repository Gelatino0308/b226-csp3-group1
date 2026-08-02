package com.joysistvi.sigsys.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/sigs_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final boolean DRIVER_AVAILABLE;

    static {
        boolean available;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            available = true;
        } catch (ClassNotFoundException e) {
            available = false;
        }
        DRIVER_AVAILABLE = available;
    }

    public static Connection getConnection() throws SQLException {
        if (!DRIVER_AVAILABLE) {
            throw new SQLException("MySQL Connector/J is not on the runtime classpath. "
                    + "Reload Maven or run the shaded JAR from target.");
        }
        try {
            return DriverManager.getConnection(
                    setting("SIGS_DB_URL", "sigs.db.url", DEFAULT_URL),
                    setting("SIGS_DB_USER", "sigs.db.user", "root"),
                    setting("SIGS_DB_PASSWORD", "sigs.db.password", ""));
        } catch (SQLException e) {
            throw new SQLException("Unable to connect to MySQL. Verify that MySQL is running, "
                    + "the sigs_db database exists, and the credentials are correct.", e);
        }
    }

    private static String setting(String environmentName, String propertyName, String defaultValue) {
        String value = System.getProperty(propertyName);
        if (value == null || value.trim().isEmpty()) value = System.getenv(environmentName);
        return value == null || value.trim().isEmpty() ? defaultValue : value.trim();
    }
}
