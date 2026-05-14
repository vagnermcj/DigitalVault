package database.dao;

import config.DatabaseConfig;

import java.sql.*;

public class ChaveiroDAO {

    public int insert(int uid,
                      byte[] privateKey,
                      String pem)
            throws Exception {

        try (Connection conn = DatabaseConfig.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                    """
                    INSERT INTO Chaveiro(
                        uid,
                        private_key_encrypted,
                        certificate_pem
                    )
                    VALUES (?, ?, ?)
                    """,
                    Statement.RETURN_GENERATED_KEYS
            );

            stmt.setInt(1, uid);
            stmt.setBytes(2, privateKey);
            stmt.setString(3, pem);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

            return -1;
        }
    }
}
