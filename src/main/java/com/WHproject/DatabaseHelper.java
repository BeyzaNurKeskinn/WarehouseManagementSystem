package com.WHproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {

    private static final String URL = "jdbc:sqlserver://DESKTOP-9NE921I:1433;databaseName=WHproject;encrypt=false;";
    private static final String USER = "sa";
    private static final String PASSWORD = "3625";
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    static {
        try {
            // JDBC Driver'ı yükle
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
