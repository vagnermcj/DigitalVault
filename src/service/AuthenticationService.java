package service;

import auth.PasswordManager;
import auth.TOTP;
import database.dao.UsuarioDAO;
import database.entity.Usuario;
import crypto.AESService;
import gui.panels.VirtualKeyboardPanel;

import javax.crypto.SecretKey;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

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

    public String authenticatePassword(
            Usuario usuario,
            List<String> possiblePasswords
    ) throws Exception {
        boolean valid = false;
        String validPassword = "";

        // Testar cada combinação
        for (String password : possiblePasswords) {
            if (PasswordManager.checkPassword(password, usuario.getSenhaHash())) {
                valid = true;
                validPassword = password;
            }
        }

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

            return validPassword;
        }

        usuario.setErrosSenha(0);
        usuarioDAO.update(usuario);

        return validPassword;
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
