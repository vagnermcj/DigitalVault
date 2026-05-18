package database.dao;

import config.DatabaseConfig;
import database.entity.Chaveiro;

import java.sql.*;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class ChaveiroDAO {

    public int insert(int uid, byte[] privateKey, String pem) throws Exception {

        try (Connection conn = DatabaseConfig.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                    """
                    INSERT INTO Chaveiro(uid, private_key_encrypted, certificate_pem)
                    VALUES (?, ?, ?)
                    """,
                    Statement.RETURN_GENERATED_KEYS
            );

            stmt.setInt(1, uid);
            stmt.setBytes(2, privateKey);
            stmt.setString(3, pem);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

            throw new Exception("Falha ao gerar KID");
        }
    }

    public Chaveiro findByUid(int uid) throws Exception {

        try (Connection conn = DatabaseConfig.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM Chaveiro WHERE uid = ? LIMIT 1"
            );

            stmt.setInt(1, uid);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return null;

            Chaveiro c = new Chaveiro();
            c.setKid(rs.getInt("kid"));
            c.setUid(rs.getInt("uid"));
            c.setPrivateKeyEncrypted(rs.getBytes("private_key_encrypted"));
            c.setCertificadoPem(rs.getString("certificate_pem"));

            return c;
        }
    }
}