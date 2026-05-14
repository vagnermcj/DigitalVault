package service;

import auth.PasswordManager;
import auth.TOTPKeyGenerator;
import crypto.*;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class CadastroService {

    public static boolean validatePrivateKey(
            PrivateKey privateKey,
            PublicKey publicKey
    ) throws Exception {

        byte[] randomData = new byte[9216];

        new java.security.SecureRandom().nextBytes(randomData);

        byte[] signature =
                SignatureService.sign(randomData, privateKey);

        return SignatureService.verify(
                randomData,
                signature,
                publicKey
        );
    }

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

        PrivateKey privateKey =
                RSAService.loadPrivateKey(
                        privateKeyPath,
                        secretPhrase
                );

        boolean valid = validatePrivateKey(
                privateKey,
                publicKey
        );

        if (!valid) {
            throw new Exception("Chave privada inválida");
        }

        String hash = PasswordManager.hashPassword(senha);

        String totpSecret = TOTPKeyGenerator.genRandomKey();

        SecretKey aesKey = AESService.generateKey(senha);

        byte[] encryptedTOTP = AESService.encrypt(
                totpSecret.getBytes(),
                aesKey
        );

        String encryptedBase64 =
                Base64.getEncoder().encodeToString(encryptedTOTP);

        System.out.println("TOTP Secret: " + totpSecret);

        System.out.println("Hash BCrypt: " + hash);

        System.out.println("TOTP criptografado: " + encryptedBase64);
    }
}
