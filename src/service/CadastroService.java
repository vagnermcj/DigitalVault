package service;

import auth.PasswordManager;
import auth.TOTPKeyGenerator;

import crypto.*;

import database.dao.ChaveiroDAO;
import database.dao.UsuarioDAO;

import javax.crypto.SecretKey;

import java.nio.file.Files;
import java.nio.file.Path;

import java.security.PrivateKey;
import java.security.PublicKey;

import java.security.cert.X509Certificate;

import java.util.Base64;

public class CadastroService {

    private UsuarioDAO usuarioDAO =
            new UsuarioDAO();

    private ChaveiroDAO chaveiroDAO =
            new ChaveiroDAO();

    public void cadastrar(
            String certPath,
            String privateKeyPath,
            String secretPhrase,
            String senha
    ) throws Exception {

        X509Certificate cert =
                CertificateService.loadCertificate(certPath);

        PublicKey publicKey =
                CertificateService.getPublicKey(cert);

        String subject =
                cert.getSubjectX500Principal().getName();

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

        System.out.println(
                "Usuário cadastrado com sucesso"
        );

        System.out.println(
                "UID: " + uid
        );

        System.out.println(
                "KID: " + kid
        );

        System.out.println(
                "TOTP Secret: " + totpSecret
        );
    }

    private String extractEmail(String subject) {

        String[] parts = subject.split(",");

        for (String p : parts) {

            p = p.trim();

            if (p.startsWith("EMAILADDRESS=")) {

                return p.substring(
                        "EMAILADDRESS=".length()
                );
            }

            if (p.startsWith("E=")) {

                return p.substring(2);
            }
        }

        return "unknown@email.com";
    }

    private String extractCommonName(String subject) {

        String[] parts = subject.split(",");

        for (String p : parts) {

            p = p.trim();

            if (p.startsWith("CN=")) {

                return p.substring(3);
            }
        }

        return "Usuário";
    }
}