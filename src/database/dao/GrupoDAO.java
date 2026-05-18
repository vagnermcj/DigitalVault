package database.dao;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class GrupoDAO {

    public void initializeGroups()
            throws Exception {

        try (Connection conn = DatabaseConfig.getConnection()) {

            PreparedStatement stmt1 =
                    conn.prepareStatement(
                            """
                            INSERT OR IGNORE INTO Grupos(gid, nome)
                            VALUES (1, 'Administrador')
                            """
                    );

            stmt1.executeUpdate();

            PreparedStatement stmt2 =
                    conn.prepareStatement(
                            """
                            INSERT OR IGNORE INTO Grupos(gid, nome)
                            VALUES (2, 'Usuário')
                            """
                    );

            stmt2.executeUpdate();
        }
    }
}