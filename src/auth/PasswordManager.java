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
        // Verifica tamanho
        if (password.length() < 8 || password.length() > 10) {
            return false;
        }

        // Verifica se são apenas dígitos
        if (!password.matches("\\d+")) {
            return false;
        }

        // Verifica sequências repetidas (ex: 11111111)
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