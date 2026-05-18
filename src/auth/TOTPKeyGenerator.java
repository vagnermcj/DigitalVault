package auth;

import java.security.SecureRandom;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class TOTPKeyGenerator {
    public static String genRandomKey() {
        SecureRandom random = new SecureRandom();
        byte[] chave = new byte[20];
        random.nextBytes(chave);

        Base32 base32 = new Base32(Base32.Alphabet.BASE32, false, false);
        return base32.toString(chave);
    }
}