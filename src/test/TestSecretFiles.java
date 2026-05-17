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
                        "admin.pem"
                );

        PublicKey publicKey =
                CertificateService.getPublicKey(cert);

        PrivateKey privateKey =
                RSAService.loadPrivateKey(
                        "admin.bin",
                        "admin123"
                );

        FileSecurityService service =
                new FileSecurityService();

        // ARQUIVO REAL

        service.encryptFile(
                "D:/vault/segredo.txt",
                "D:/vault/A1",
                privateKey,
                publicKey
        );

        // ÍNDICE

        service.encryptFile(
                "D:/vault/index.txt",
                "D:/vault/index",
                privateKey,
                publicKey
        );

        System.out.println(
                "Arquivos secretos gerados."
        );
    }
}