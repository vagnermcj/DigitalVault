package service;

import auth.PasswordManager;
import auth.TOTPKeyGenerator;

import crypto.*;

import database.dao.ChaveiroDAO;
import database.dao.UsuarioDAO;
import gui.setup.TOTPSetupDialog;

import javax.crypto.SecretKey;
import javax.security.auth.x500.X500Principal;
import javax.swing.*;

import java.nio.file.Files;
import java.nio.file.Path;

import java.security.PrivateKey;
import java.security.PublicKey;

import java.security.cert.X509Certificate;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class CadastroService {

    private UsuarioDAO usuarioDAO =
            new UsuarioDAO();

    private ChaveiroDAO chaveiroDAO =
            new ChaveiroDAO();

    public void cadastrar(
            String certPath,
            String privateKeyPath,
            String secretPhrase,
            String senha,
            boolean admin
    ) throws Exception {

        X509Certificate cert =
                CertificateService.loadCertificate(certPath);

        PublicKey publicKey =
                CertificateService.getPublicKey(cert);

        String subject =
                cert.getSubjectX500Principal().getName(X500Principal.RFC1779);

        String login =
                extractEmail(subject);

        String nome =
                extractCommonName(subject);

        PrivateKey privateKey =
                RSAService.loadPrivateKey(
                        privateKeyPath,
                        secretPhrase
                );

        boolean valid =
                SignatureService.validatePrivateKey(
                        privateKey,
                        publicKey
                );

        if (!valid) {

            throw new Exception(
                    "Chave privada inválida"
            );
        }

        String hash =
                PasswordManager.hashPassword(senha);

        String totpSecret =
                TOTPKeyGenerator.genRandomKey();

        SecretKey aesKey =
                AESService.generateKey(senha);

        byte[] encryptedTOTP =
                AESService.encrypt(
                        totpSecret.getBytes(),
                        aesKey
                );

        String encryptedBase64 =
                Base64.getEncoder()
                        .encodeToString(encryptedTOTP);

        int gid = 1;

        int uid =
                usuarioDAO.insertReturningId(
                        login,
                        nome,
                        gid,
                        hash,
                        encryptedBase64
                );

        String pem =
                Files.readString(
                        Path.of(certPath)
                );

        byte[] privateKeyEncrypted =
                Files.readAllBytes(
                        Path.of(privateKeyPath)
                );

        int kid =
                chaveiroDAO.insert(
                        uid,
                        privateKeyEncrypted,
                        pem
                );

        usuarioDAO.updateKid(uid, kid);

        SwingUtilities.invokeLater(() -> {
            TOTPSetupDialog dialog = new TOTPSetupDialog( login, totpSecret, admin);
            dialog.setVisible(true);
        });
    }

    private String extractEmail(String subject) {
        // Aceitar qualquer formato de email: EMAILADDRESS, E, ou OID
        Pattern pattern = Pattern.compile("(?:EMAILADDRESS|E|OID\\.1\\.2\\.840\\.113549\\.1\\.9\\.1)=([^,]+)");
        Matcher matcher = pattern.matcher(subject);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return null;
    }

    private String extractCommonName(String subject) {

        return extractField(subject, "CN");
    }

    private static String extractField(String dn, String field) {
        Pattern pattern = Pattern.compile(field + "=([^,]+)");
        Matcher matcher = pattern.matcher(dn);
        return matcher.find() ? matcher.group(1).trim() : null;
    }

    public static ValidatedCryptoContext validateCredentials(
            String certPath,
            String privateKeyPath,
            String secretPhrase
    ) throws Exception {

        X509Certificate cert =
                CertificateService.loadCertificate(
                        certPath
                );

        PublicKey publicKey =
                CertificateService.getPublicKey(
                        cert
                );

        PrivateKey privateKey =
                RSAService.loadPrivateKey(
                        privateKeyPath,
                        secretPhrase
                );

        boolean valid =
                SignatureService.validatePrivateKey(
                        privateKey,
                        publicKey
                );

        if (!valid) {

            throw new Exception(
                    "Chave privada inválida"
            );
        }

        return new ValidatedCryptoContext(
                privateKey,
                publicKey,
                cert
        );
    }
}