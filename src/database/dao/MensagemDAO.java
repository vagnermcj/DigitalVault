package database.dao;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class MensagemDAO {

    public void initializeMessages() throws Exception {
        // 1000 – Sistema
        insert(1001, "Sistema iniciado.");
        insert(1002, "Sistema encerrado.");
        insert(1003, "Sessão iniciada para <login_name>.");
        insert(1004, "Sessão encerrada para <login_name>.");
        insert(1005, "Partida do sistema iniciada para cadastro do administrador.");
        insert(1006, "Partida do sistema iniciada para operação normal pelos usuários.");

        // 2000 – Autenticação etapa 1
        insert(2001, "Autenticação etapa 1 iniciada.");
        insert(2002, "Autenticação etapa 1 encerrada.");
        insert(2003, "Login name <login_name> identificado com acesso liberado.");
        insert(2004, "Login name <login_name> identificado com acesso bloqueado.");
        insert(2005, "Login name <login_name> não identificado.");

        // 3000 – Autenticação etapa 2
        insert(3001, "Autenticação etapa 2 iniciada para <login_name>.");
        insert(3002, "Autenticação etapa 2 encerrada para <login_name>.");
        insert(3003, "Senha pessoal verificada positivamente para <login_name>.");
        insert(3004, "Primeiro erro da senha pessoal contabilizado para <login_name>.");
        insert(3005, "Segundo erro da senha pessoal contabilizado para <login_name>.");
        insert(3006, "Terceiro erro da senha pessoal contabilizado para <login_name>.");
        insert(3007, "Acesso do usuario <login_name> bloqueado pela autenticação etapa 2.");

        // 4000 – Autenticação etapa 3
        insert(4001, "Autenticação etapa 3 iniciada para <login_name>.");
        insert(4002, "Autenticação etapa 3 encerrada para <login_name>.");
        insert(4003, "Token verificado positivamente para <login_name>.");
        insert(4004, "Primeiro erro de token contabilizado para <login_name>.");
        insert(4005, "Segundo erro de token contabilizado para <login_name>.");
        insert(4006, "Terceiro erro de token contabilizado para <login_name>.");
        insert(4007, "Acesso do usuario <login_name> bloqueado pela autenticação etapa 3.");

        // 5000 – Tela principal
        insert(5001, "Tela principal apresentada para <login_name>.");
        insert(5002, "Opção 1 do menu principal selecionada por <login_name>.");
        insert(5003, "Opção 2 do menu principal selecionada por <login_name>.");
        insert(5004, "Opção 3 do menu principal selecionada por <login_name>.");

        // 6000 – Cadastro
        insert(6001, "Tela de cadastro apresentada para <login_name>.");
        insert(6002, "Botão cadastrar pressionado por <login_name>.");
        insert(6003, "Senha pessoal inválida fornecida por <login_name>.");
        insert(6004, "Caminho do certificado digital inválido fornecido por <login_name>.");
        insert(6005, "Chave privada verificada negativamente para <login_name> (caminho inválido).");
        insert(6006, "Chave privada verificada negativamente para <login_name> (frase secreta inválida).");
        insert(6007, "Chave privada verificada negativamente para <login_name> (assinatura digital inválida).");
        insert(6008, "Confirmação de dados aceita por <login_name>.");
        insert(6009, "Confirmação de dados rejeitada por <login_name>.");
        insert(6010, "Botão voltar de cadastro para o menu principal pressionado por <login_name>.");

        // 7000 – Consulta de arquivos
        insert(7001, "Tela de consulta de arquivos secretos apresentada para <login_name>.");
        insert(7002, "Botão voltar de consulta para o menu principal pressionado por <login_name>.");
        insert(7003, "Botão Listar de consulta pressionado por <login_name>.");
        insert(7004, "Caminho de pasta inválido fornecido por <login_name>.");
        insert(7005, "Arquivo de índice decriptado com sucesso para <login_name>.");
        insert(7006, "Arquivo de índice verificado (integridade e autenticidade) com sucesso para <login_name>.");
        insert(7007, "Falha na decriptação do arquivo de índice para <login_name>.");
        insert(7008, "Falha na verificação (integridade e autenticidade) do arquivo de índice para <login_name>.");
        insert(7009, "Lista de arquivos presentes no índice apresentada para <login_name>.");
        insert(7010, "Arquivo <arq_name> selecionado por <login_name> para decriptação.");
        insert(7011, "Acesso permitido ao arquivo <arq_name> para <login_name>.");
        insert(7012, "Acesso negado ao arquivo <arq_name> para <login_name>.");
        insert(7013, "Arquivo <arq_name> decriptado com sucesso para <login_name>.");
        insert(7014, "Arquivo <arq_name> verificado (integridade e autenticidade) com sucesso para <login_name>.");
        insert(7015, "Falha na decriptação do arquivo <arq_name> para <login_name>.");
        insert(7016, "Falha na verificação (integridade e autenticidade) do arquivo <arq_name> para <login_name>.");

        // 8000 – Saída
        insert(8001, "Tela de saída apresentada para <login_name>.");
        insert(8002, "Botão encerrar sessão pressionado por <login_name>.");
        insert(8003, "Botão encerrar sistema pressionado por <login_name>.");
        insert(8004, "Botão voltar de sair para o menu principal pressionado por <login_name>.");
    }

    private void insert(int mid, String msg) throws Exception {
        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT OR IGNORE INTO Mensagens(mid, mensagem) VALUES (?, ?)"
            );
            stmt.setInt(1, mid);
            stmt.setString(2, msg);
            stmt.executeUpdate();
        }
    }
}