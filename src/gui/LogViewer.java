package gui;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LogViewer {

    public static void main(String[] args) {

        try (Connection conn = DatabaseConfig.getConnection()) {

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(
                    """
                    SELECT r.data_hora,
                           r.mid,
                           m.mensagem
                    FROM Registros r
                    JOIN Mensagens m
                        ON r.mid = m.mid
                    ORDER BY r.data_hora
                    """
            );

            while (rs.next()) {

                System.out.println(
                        rs.getString("data_hora") +
                                " - " +
                                rs.getInt("mid") +
                                " - " +
                                rs.getString("mensagem")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
