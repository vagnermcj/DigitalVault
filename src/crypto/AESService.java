package crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;

public class AESService {

    public static SecretKey generateKey(String password)
            throws Exception {

        SecureRandom secureRandom =
                SecureRandom.getInstance("SHA1PRNG");

        secureRandom.setSeed(password.getBytes());

        KeyGenerator generator = KeyGenerator.getInstance("AES");

        generator.init(256, secureRandom);

        return generator.generateKey();
    }

    public static byte[] encrypt(byte[] data,
                                 SecretKey key)
            throws Exception {

        Cipher cipher =
                Cipher.getInstance("AES/ECB/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, key);

        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] data,
                                 SecretKey key)
            throws Exception {

        Cipher cipher =
                Cipher.getInstance("AES/ECB/PKCS5Padding");

        cipher.init(Cipher.DECRYPT_MODE, key);

        return cipher.doFinal(data);
    }
}
