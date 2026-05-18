package service;

import crypto.*;

import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class FileSecurityService {

    public void encryptFile(
            String inputPath,
            String outputName,
            PrivateKey signer,
            PublicKey receiver
    ) throws Exception {

        byte[] data = Files.readAllBytes(Path.of(inputPath));

        byte[] seed = new byte[32];
        new SecureRandom().nextBytes(seed);

        SecretKey aesKey = AESService.generateKey(
                java.util.Base64.getEncoder().encodeToString(seed)
        );

        byte[] encrypted = AESService.encrypt(data, aesKey);

        byte[] signature = SignatureService.sign(
                encrypted,
                signer
        );

        byte[] envelope = EnvelopeService.encryptSeed(
                seed,
                receiver
        );

        Files.write(Path.of(outputName + ".enc"), encrypted);
        Files.write(Path.of(outputName + ".asd"), signature);
        Files.write(Path.of(outputName + ".env"), envelope);
    }

    public void decryptFile(
            String fileName,
            PrivateKey receiver,
            PublicKey signer
    ) throws Exception {

        byte[] encrypted = Files.readAllBytes(
                Path.of(fileName + ".enc")
        );

        byte[] signature = Files.readAllBytes(
                Path.of(fileName + ".asd")
        );

        byte[] envelope = Files.readAllBytes(
                Path.of(fileName + ".env")
        );

        byte[] seed = EnvelopeService.decryptSeed(
                envelope,
                receiver
        );

        SecretKey aesKey = AESService.generateKey(
                java.util.Base64.getEncoder().encodeToString(seed)
        );

        byte[] decrypted = AESService.decrypt(encrypted, aesKey);

        boolean valid = SignatureService.verify(
                encrypted,
                signature,
                signer
        );

        if (!valid) {
            throw new Exception("Assinatura inválida");
        }

        Files.write(Path.of(fileName + "_restored"), decrypted);
    }
}