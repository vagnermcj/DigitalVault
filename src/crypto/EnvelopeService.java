package crypto;

import java.security.PrivateKey;
import java.security.PublicKey;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

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