package service;

import crypto.CertificateService;
import crypto.RSAService;
import crypto.SignatureService;

import javax.swing.*;

import java.security.PrivateKey;
import java.security.PublicKey;

import java.security.SecureRandom;

import java.security.cert.X509Certificate;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class UserPrivateKeyValidationService {

    public static ValidatedUserKeys validate()
            throws Exception {

        JTextField txtCert =
                new JTextField();

        JTextField txtKey =
                new JTextField();

        JPasswordField txtPhrase =
                new JPasswordField();

        Object[] fields = {

                "Certificado (.pem):",
                txtCert,

                "Chave privada (.bin):",
                txtKey,

                "Frase secreta:",
                txtPhrase
        };

        int result =
                JOptionPane.showConfirmDialog(
                        null,
                        fields,
                        "Validação da Chave Privada",
                        JOptionPane.OK_CANCEL_OPTION
                );

        if (result != JOptionPane.OK_OPTION) {

            throw new Exception(
                    "Operação cancelada"
            );
        }

        String certPath =
                txtCert.getText();

        String keyPath =
                txtKey.getText();

        String phrase =
                new String(
                        txtPhrase.getPassword()
                );

        // CERTIFICADO

        X509Certificate cert =
                CertificateService.loadCertificate(
                        certPath
                );

        PublicKey publicKey =
                CertificateService.getPublicKey(
                        cert
                );

        // CHAVE PRIVADA

        PrivateKey privateKey =
                RSAService.loadPrivateKey(
                        keyPath,
                        phrase
                );

        // VALIDAÇÃO REAL

        byte[] random =
                new byte[9216];

        new SecureRandom()
                .nextBytes(random);

        byte[] signature =
                SignatureService.sign(
                        random,
                        privateKey
                );

        boolean valid =
                SignatureService.verify(
                        random,
                        signature,
                        publicKey
                );

        if (!valid) {

            throw new Exception(
                    "Falha validação da chave privada"
            );
        }

        return new ValidatedUserKeys(
                privateKey,
                publicKey
        );
    }
}
