package database;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.Statement;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() throws Exception {

        try (Connection conn = DatabaseConfig.getConnection()) {

            Statement stmt = conn.createStatement();

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Grupos (
                    gid INTEGER PRIMARY KEY,
                    nome VARCHAR(50) UNIQUE NOT NULL
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Usuarios (
                    uid INTEGER PRIMARY KEY AUTOINCREMENT,
                    login VARCHAR(255) UNIQUE NOT NULL,
                    nome VARCHAR(255) NOT NULL,
                    gid INTEGER NOT NULL,
                    senha_hash CHAR(60) NOT NULL,
                    totp_secret_encrypted TEXT NOT NULL,
                    kid INTEGER,
                    bloqueado_ate TIMESTAMP,
                    erros_senha INTEGER DEFAULT 0,
                    erros_totp INTEGER DEFAULT 0,
                    total_acessos INTEGER DEFAULT 0
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Chaveiro (
                    kid INTEGER PRIMARY KEY AUTOINCREMENT,
                    uid INTEGER NOT NULL,
                    private_key_encrypted BLOB NOT NULL,
                    certificate_pem TEXT NOT NULL
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Mensagens (
                    mid INTEGER PRIMARY KEY,
                    mensagem TEXT NOT NULL
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Registros (
                    rid INTEGER PRIMARY KEY AUTOINCREMENT,
                    data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    mid INTEGER NOT NULL,
                    uid INTEGER,
                    arquivo_nome VARCHAR(255)
                )
            """);
        }
    }
}