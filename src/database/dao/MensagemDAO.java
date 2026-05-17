package database.dao;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class MensagemDAO {

    public void initializeMessages()
            throws Exception {

        insert(1001, "Sistema iniciado.");
        insert(1002, "Sistema encerrado.");

        insert(1003, "Sessão iniciada.");
        insert(1004, "Sessão encerrada.");

        insert(2001, "Autenticação etapa 1 iniciada.");
        insert(2002, "Autenticação etapa 1 encerrada.");

        insert(3001, "Autenticação etapa 2 iniciada.");
        insert(3002, "Autenticação etapa 2 encerrada.");

        insert(4001, "Autenticação etapa 3 iniciada.");
        insert(4002, "Autenticação etapa 3 encerrada.");

        insert(5001, "Tela principal apresentada.");

        insert(6001, "Tela cadastro apresentada.");

        insert(7001, "Tela consulta apresentada.");
        insert(7002, "Botão voltar consulta.");

        insert(7010, "Arquivo selecionado.");
        insert(7011, "Acesso permitido.");
        insert(7012, "Acesso negado.");

        insert(8001, "Tela saída apresentada.");

        // CONTINUE COM TODOS OS MIDs
    }

    private void insert(int mid,
                        String msg)
            throws Exception {

        try (Connection conn = DatabaseConfig.getConnection()) {

            PreparedStatement stmt =
                    conn.prepareStatement(
                            """
                            INSERT OR IGNORE INTO Mensagens(
                                mid,
                                mensagem
                            )
                            VALUES (?, ?)
                            """
                    );

            stmt.setInt(1, mid);
            stmt.setString(2, msg);

            stmt.executeUpdate();
        }
    }
}
