package crypto;

import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class RSAService {

    public static PrivateKey loadPrivateKey(
            byte[] encryptedBytes,
            String secretPhrase
    ) throws Exception {

        SecretKey key =
                AESService.generateKey(secretPhrase);

        byte[] decrypted =
                AESService.decrypt(encryptedBytes, key);

        String pem =
                new String(decrypted);

        pem = pem
                .replaceAll("-----BEGIN ([A-Z ]*)-----", "")
                .replaceAll("-----END ([A-Z ]*)-----", "")
                .replaceAll("\\s", "");

        byte[] decoded =
                Base64.getDecoder().decode(pem);

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(decoded);

        return KeyFactory
                .getInstance("RSA")
                .generatePrivate(spec);
    }

    public static PrivateKey loadPrivateKey(
            String encryptedPath,
            String secretPhrase
    ) throws Exception {

        byte[] encrypted =
                Files.readAllBytes(Path.of(encryptedPath));

        return loadPrivateKey(
                encrypted,
                secretPhrase
        );
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
