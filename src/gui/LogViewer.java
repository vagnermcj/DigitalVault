package gui;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LogViewer {

    public static void main(String[] args) {

        try (Connection conn = DatabaseConfig.getConnection()) {

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("""
                SELECT
                    r.data_hora,
                    r.mid,
                    m.mensagem,
                    u.login      AS login_name,
                    r.arquivo_nome
                FROM Registros r
                JOIN Mensagens m ON r.mid = m.mid
                LEFT JOIN Usuarios u ON r.uid = u.uid
                ORDER BY r.data_hora
            """);

            while (rs.next()) {
                String dataHora   = rs.getString("data_hora");
                int    mid        = rs.getInt("mid");
                String mensagem   = rs.getString("mensagem");
                String loginName  = rs.getString("login_name");   // pode ser null
                String arquivoNome = rs.getString("arquivo_nome"); // pode ser null

                if (loginName != null) {
                    mensagem = mensagem.replace("<login_name>", loginName);
                }
                if (arquivoNome != null) {
                    mensagem = mensagem.replace("<arq_name>", arquivoNome);
                }

                System.out.printf("%s | %04d | %s%n", dataHora, mid, mensagem);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}