package database.dao;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;

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