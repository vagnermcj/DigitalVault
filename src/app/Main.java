package app;

import database.DatabaseInitializer;
import service.LogService;
import auth.TOTPKeyGenerator;
import auth.TOTP;
import auth.PasswordManager;

public class Main {

    public static void main(String[] args) {

        try {

            DatabaseInitializer.initialize();

            LogService.registrar(1001, null, null);

            System.out.println("Sistema iniciado.");

            String secret = TOTPKeyGenerator.genRandomKey();

            TOTP totp = new TOTP(secret, 30);

            String code = totp.generateCode();

            System.out.println("Código TOTP: " + code);

            System.out.println(
                    "Validação: " + totp.validateCode(code)
            );

            String senha = "12345678";

            String hash = PasswordManager.hashPassword(senha);

            System.out.println(hash);

            System.out.println(
                    PasswordManager.checkPassword(senha, hash)
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}