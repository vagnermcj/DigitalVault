package test;

import crypto.CertificateService;
import crypto.RSAService;

import service.FileSecurityService;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

public class TestSecretFiles {

    public static void main(String[] args)
            throws Exception {

        X509Certificate cert =
                CertificateService.loadCertificate(
                        "D:/TrabalhoSeguranca/Assets/admin_cert.pem"
                );

        PublicKey publicKey =
                CertificateService.getPublicKey(cert);

        PrivateKey privateKey =
                RSAService.loadPrivateKey(
                        "D:/TrabalhoSeguranca/Assets/admin_key.bin",
                        "AdminSecret2026!"
                );

        FileSecurityService service =
                new FileSecurityService();

        // ARQUIVO REAL

        service.encryptFile(
                "D:/TrabalhoSeguranca/arqs/segredo.txt",
                "D:/TrabalhoSeguranca/arqs/A1",
                privateKey,
                publicKey
        );

        // ÍNDICE

        service.encryptFile(
                "D:/TrabalhoSeguranca/arqs/index.txt",
                "D:/TrabalhoSeguranca/arqs/index",
                privateKey,
                publicKey
        );

        System.out.println(
                "Arquivos secretos gerados."
        );
    }
}