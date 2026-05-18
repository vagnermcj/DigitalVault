package service;

import crypto.CertificateService;
import crypto.RSAService;
import crypto.SignatureService;
import session.RuntimeSession;

import javax.swing.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class SystemStartupService {

    public static boolean validateAdministratorKey()
            throws Exception {

        JTextField txtCert = new JTextField();
        JTextField txtKey = new JTextField();
        JPasswordField txtPhrase = new JPasswordField();

        Object[] fields = {
                "Certificado:", txtCert,
                "Chave privada:", txtKey,
                "Frase secreta:", txtPhrase
        };

        int result = JOptionPane.showConfirmDialog(
                null,
                fields,
                "Inicialização do Sistema",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) {
            return false;
        }

        String certPath = txtCert.getText();
        String keyPath = txtKey.getText();
        String phrase = new String(txtPhrase.getPassword());

        X509Certificate cert =
                CertificateService.loadCertificate(certPath);

        PublicKey publicKey =
                CertificateService.getPublicKey(cert);

        PrivateKey privateKey =
                RSAService.loadPrivateKey(
                        keyPath,
                        phrase
                );

        byte[] random = new byte[9216];

        new SecureRandom().nextBytes(random);

        byte[] signature =
                SignatureService.sign(random, privateKey);

        boolean valid =
                SignatureService.verify(
                        random,
                        signature,
                        publicKey
                );

        if (!valid) {
            return false;
        }

        RuntimeSession.setAdminSecretPhrase(phrase);

        return true;
    }
}