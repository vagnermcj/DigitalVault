package auth;

import org.bouncycastle.crypto.generators.OpenBSDBCrypt;

public class PasswordManager {
    private static final int BCRYPT_COST = 8;

    public static String hashPassword(String password) {
        byte[] passwordBytes = password.getBytes();
        return OpenBSDBCrypt.generate(passwordBytes,
                new byte[16], // salt gerado automaticamente
                BCRYPT_COST);
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        return OpenBSDBCrypt.checkPassword(hashedPassword, password.getBytes());
    }

    public static boolean isValidPasswordFormat(String password) {
        // TODO: Validar 8-10 dígitos, sem repetições
        return false;
    }
}