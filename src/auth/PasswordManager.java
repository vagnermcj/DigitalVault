package auth;

import org.bouncycastle.crypto.generators.OpenBSDBCrypt;
import java.security.SecureRandom;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class PasswordManager {
    private static final int BCRYPT_COST = 8;

    public static String hashPassword(String password) {
        // Gerar salt de 16 bytes
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        // Gerar hash bcrypt
        return OpenBSDBCrypt.generate(password.toCharArray(), salt, BCRYPT_COST);
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        return OpenBSDBCrypt.checkPassword(hashedPassword, password.toCharArray());
    }

    public static boolean isValidPasswordFormat(String password) {
        if (password.length() < 8 || password.length() > 10) {
            return false;
        }

        if (!password.matches("\\d+")) {
            return false;
        }

        char firstChar = password.charAt(0);
        boolean allSame = true;
        for (int i = 1; i < password.length(); i++) {
            if (password.charAt(i) != firstChar) {
                allSame = false;
                break;
            }
        }

        return !allSame;
    }
}