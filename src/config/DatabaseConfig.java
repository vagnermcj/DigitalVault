package config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConfig {

    private static final String URL = "jdbc:sqlite:vault.db";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL);
    }
}