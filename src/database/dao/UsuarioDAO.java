package database.dao;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UsuarioDAO {

    public void insert(
            String login,
            String nome,
            int gid,
            String senhaHash,
            String totpEncrypted,
            int kid
    ) throws Exception {

        try (Connection conn = DatabaseConfig.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                    """
                    INSERT INTO Usuarios(
                        login,
                        nome,
                        gid,
                        senha_hash,
                        totp_secret_encrypted,
                        kid
                    )
                    VALUES (?, ?, ?, ?, ?, ?)
                    """
            );

            stmt.setString(1, login);
            stmt.setString(2, nome);
            stmt.setInt(3, gid);
            stmt.setString(4, senhaHash);
            stmt.setString(5, totpEncrypted);
            stmt.setInt(6, kid);

            stmt.executeUpdate();
        }
    }
}
