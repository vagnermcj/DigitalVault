package config;

import java.sql.Connection;
import java.sql.DriverManager;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class DatabaseConfig {

    private static final String URL = "jdbc:sqlite:vault.db";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL);
    }
}