package util;

import org.bouncycastle.crypto.generators.OpenBSDBCrypt;

import java.security.SecureRandom;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class BCryptUtil {

    public static String hashPassword(String password) {

        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        return OpenBSDBCrypt.generate(
                "2y",
                password.toCharArray(),
                salt,
                8
        );
    }

    public static boolean verifyPassword(String password, String hash) {
        return OpenBSDBCrypt.checkPassword(hash, password.toCharArray());
    }
}