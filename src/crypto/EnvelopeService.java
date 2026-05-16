package crypto;

import java.security.PrivateKey;
import java.security.PublicKey;

public class EnvelopeService {

    public static byte[] encryptSeed(byte[] seed,
                                     PublicKey publicKey)
            throws Exception {

        return RSAService.encrypt(seed, publicKey);
    }

    public static byte[] decryptSeed(byte[] seed,
                                     PrivateKey privateKey)
            throws Exception {

        return RSAService.decrypt(seed, privateKey);
    }
}