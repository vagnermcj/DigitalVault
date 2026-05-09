package auth;

public class Main {
    public static void main(String[] args) {
        try {
            // Teste TOTP
            System.out.println("=== TESTE TOTP ===");
            String secret = "JBSWY3DPEHPK3PXP"; // Chave de teste
            TOTP totp = new TOTP(secret, 30);

            String code = totp.generateCode();
            System.out.println("Código gerado: " + code);
            System.out.println("Validação: " + totp.validateCode(code));

            // Teste BCrypt
            System.out.println("\n=== TESTE BCRYPT ===");
            String senha = "12345678";

            // Validar formato
            System.out.println("Formato válido: " +
                    PasswordManager.isValidPasswordFormat(senha));

            // Gerar hash
            String hash = PasswordManager.hashPassword(senha);
            System.out.println("Hash gerado: " + hash);

            // Validar senha
            System.out.println("Senha correta: " +
                    PasswordManager.checkPassword(senha, hash));
            System.out.println("Senha errada: " +
                    PasswordManager.checkPassword("99999999", hash));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}