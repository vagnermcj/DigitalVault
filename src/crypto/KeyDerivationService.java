package crypto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;

public class KeyDerivationService {

    public static SecretKey generateAESKeyFromPassword(String password)
            throws Exception {

        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");

        secureRandom.setSeed(password.getBytes());

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

        keyGenerator.init(256, secureRandom);

        return keyGenerator.generateKey();
    }
}