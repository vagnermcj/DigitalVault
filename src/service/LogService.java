package service;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class LogService {

    public static void registrar(
            int mid,
            Integer uid,
            String arquivo
    ) {

        try (Connection conn = DatabaseConfig.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                    """
                    INSERT INTO Registros(
                        mid,
                        uid,
                        arquivo_nome
                    )
                    VALUES (?, ?, ?)
                    """
            );

            stmt.setInt(1, mid);

            if (uid == null)
                stmt.setNull(2, java.sql.Types.INTEGER);
            else
                stmt.setInt(2, uid);

            stmt.setString(3, arquivo);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
