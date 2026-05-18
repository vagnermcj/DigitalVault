package crypto;

import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class RSAService {

    public static PrivateKey loadPrivateKey(
            String encryptedPath,
            String secretPhrase
    ) throws Exception {

        byte[] encrypted =
                Files.readAllBytes(Path.of(encryptedPath));

        SecretKey key = AESService.generateKey(secretPhrase);

        byte[] decrypted = AESService.decrypt(encrypted, key);

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(decrypted);

        KeyFactory factory = KeyFactory.getInstance("RSA");

        return factory.generatePrivate(spec);
    }

    // Adicione em RSAService.java
    public static PrivateKey loadPrivateKey(byte[] encryptedBytes, String secretPhrase)
            throws Exception {

        SecretKey key = AESService.generateKey(secretPhrase);
        byte[] decrypted = AESService.decrypt(encryptedBytes, key);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decrypted);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    public static byte[] encrypt(byte[] data,
                                 PublicKey publicKey)
            throws Exception {

        javax.crypto.Cipher cipher =
                javax.crypto.Cipher.getInstance("RSA");

        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] data,
                                 PrivateKey privateKey)
            throws Exception {

        javax.crypto.Cipher cipher =
                javax.crypto.Cipher.getInstance("RSA");

        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }
}
