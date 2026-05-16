package service;

import auth.PasswordManager;
import auth.TOTP;
import database.dao.UsuarioDAO;
import database.entity.Usuario;
import crypto.AESService;

import javax.crypto.SecretKey;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;

public class AuthenticationService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario authenticateLogin(String login)
            throws Exception {

        Usuario usuario = usuarioDAO.findByLogin(login);

        if (usuario == null) {
            return null;
        }

        if (usuario.getBloqueadoAte() != null) {

            if (usuario.getBloqueadoAte()
                    .after(Timestamp.valueOf(LocalDateTime.now()))) {

                throw new Exception("Usuário bloqueado");
            }
        }

        return usuario;
    }

    public boolean authenticatePassword(
            Usuario usuario,
            String senha
    ) throws Exception {

        boolean valid = PasswordManager.checkPassword(
                senha,
                usuario.getSenhaHash()
        );

        if (!valid) {

            usuario.setErrosSenha(usuario.getErrosSenha() + 1);

            if (usuario.getErrosSenha() >= 3) {

                usuario.setBloqueadoAte(
                        Timestamp.valueOf(
                                LocalDateTime.now().plusMinutes(2)
                        )
                );
            }

            usuarioDAO.update(usuario);

            return false;
        }

        usuario.setErrosSenha(0);
        usuarioDAO.update(usuario);

        return true;
    }

    public boolean authenticateTOTP(
            Usuario usuario,
            String senha,
            String code
    ) throws Exception {

        SecretKey aesKey = AESService.generateKey(senha);

        byte[] encrypted = Base64.getDecoder().decode(
                usuario.getTotpSecretEncrypted()
        );

        byte[] decrypted = AESService.decrypt(encrypted, aesKey);

        String secret = new String(decrypted);

        TOTP totp = new TOTP(secret, 30);

        boolean valid = totp.validateCode(code);

        if (!valid) {

            usuario.setErrosTotp(usuario.getErrosTotp() + 1);

            if (usuario.getErrosTotp() >= 3) {

                usuario.setBloqueadoAte(
                        Timestamp.valueOf(
                                LocalDateTime.now().plusMinutes(2)
                        )
                );
            }

            usuarioDAO.update(usuario);

            return false;
        }

        usuario.setErrosTotp(0);
        usuario.setTotalAcessos(usuario.getTotalAcessos() + 1);

        usuarioDAO.update(usuario);

        return true;
    }
}
