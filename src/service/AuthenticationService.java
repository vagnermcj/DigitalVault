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

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

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

    private String findMatchingPassword(
            String bcryptHash,
            List<int[]> pairs,
            int index,
            StringBuilder current
    ) {

        if (index == pairs.size()) {

            String password =
                    current.toString();

            if (PasswordManager.checkPassword(
                    password,
                    bcryptHash
            )) {

                return password;
            }

            return null;
        }

        int[] pair = pairs.get(index);

        current.append(pair[0]);

        String left =
                findMatchingPassword(
                        bcryptHash,
                        pairs,
                        index + 1,
                        current
                );

        if (left != null) {
            return left;
        }

        current.deleteCharAt(
                current.length() - 1
        );

        current.append(pair[1]);

        String right =
                findMatchingPassword(
                        bcryptHash,
                        pairs,
                        index + 1,
                        current
                );

        if (right != null) {
            return right;
        }

        current.deleteCharAt(
                current.length() - 1
        );

        return null;
    }

    public String authenticatePassword(
            Usuario usuario,
            List<int[]> clickedPairs
    ) throws Exception {

        String result =
                findMatchingPassword(
                        usuario.getSenhaHash(),
                        clickedPairs,
                        0,
                        new StringBuilder()
                );

        if (result != null) {

            usuario.setErrosSenha(0);

            usuarioDAO.update(usuario);

            return result;
        }

        // ERRO

        usuario.setErrosSenha(
                usuario.getErrosSenha() + 1
        );

        if (usuario.getErrosSenha() >= 3) {

            usuario.setBloqueadoAte(
                    Timestamp.valueOf(
                            LocalDateTime.now()
                                    .plusMinutes(2)
                    )
            );

            usuarioDAO.update(usuario);

            return "blocked";
        }

        usuarioDAO.update(usuario);

        return "";
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
            LogService.registrar(4003 + usuario.getErrosTotp(), usuario.getUid(), null);

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
