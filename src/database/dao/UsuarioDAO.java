package database.dao;

import config.DatabaseConfig;
import database.entity.Usuario;

import java.sql.*;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class UsuarioDAO {

    public int insertReturningId(
            String login,
            String nome,
            int gid,
            String senhaHash,
            String totpEncrypted
    ) throws Exception {

        try (Connection conn = DatabaseConfig.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                    """
                    INSERT INTO Usuarios(
                        login,
                        nome,
                        gid,
                        senha_hash,
                        totp_secret_encrypted
                    )
                    VALUES (?, ?, ?, ?, ?)
                    """,
                    Statement.RETURN_GENERATED_KEYS
            );

            stmt.setString(1, login);
            stmt.setString(2, nome);
            stmt.setInt(3, gid);
            stmt.setString(4, senhaHash);
            stmt.setString(5, totpEncrypted);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

            throw new Exception("Falha ao gerar UID");
        }
    }

    public void updateKid(int uid, int kid)
            throws Exception {

        try (Connection conn = DatabaseConfig.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                    """
                    UPDATE Usuarios
                    SET kid = ?
                    WHERE uid = ?
                    """
            );

            stmt.setInt(1, kid);
            stmt.setInt(2, uid);

            stmt.executeUpdate();
        }
    }

    public Usuario findByLogin(String login)
            throws Exception {

        try (Connection conn = DatabaseConfig.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM Usuarios WHERE login = ?"
            );

            stmt.setString(1, login);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return null;
            }

            Usuario usuario = new Usuario();

            usuario.setUid(rs.getInt("uid"));
            usuario.setLogin(rs.getString("login"));
            usuario.setNome(rs.getString("nome"));
            usuario.setGid(rs.getInt("gid"));

            usuario.setSenhaHash(
                    rs.getString("senha_hash")
            );

            usuario.setTotpSecretEncrypted(
                    rs.getString("totp_secret_encrypted")
            );

            usuario.setKid(rs.getInt("kid"));

            usuario.setErrosSenha(
                    rs.getInt("erros_senha")
            );

            usuario.setErrosTotp(
                    rs.getInt("erros_totp")
            );

            usuario.setTotalAcessos(
                    rs.getInt("total_acessos")
            );

            usuario.setBloqueadoAte(
                    rs.getTimestamp("bloqueado_ate")
            );

            usuario.setGrupoNome(
                    usuario.getGid() == 1
                            ? "Administrador"
                            : "Usuário"
            );

            return usuario;
        }
    }

    public void update(Usuario usuario)
            throws Exception {

        try (Connection conn = DatabaseConfig.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                    """
                    UPDATE Usuarios
                    SET bloqueado_ate = ?,
                        erros_senha = ?,
                        erros_totp = ?,
                        total_acessos = ?
                    WHERE uid = ?
                    """
            );

            stmt.setTimestamp(1, usuario.getBloqueadoAte());

            stmt.setInt(2, usuario.getErrosSenha());

            stmt.setInt(3, usuario.getErrosTotp());

            stmt.setInt(4, usuario.getTotalAcessos());

            stmt.setInt(5, usuario.getUid());

            stmt.executeUpdate();
        }
    }

    public boolean existsAnyUser()
            throws Exception {

        try (Connection conn = DatabaseConfig.getConnection()) {

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) FROM Usuarios"
            );

            return rs.next() && rs.getInt(1) > 0;
        }
    }
}