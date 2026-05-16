package app;

import database.dao.UsuarioDAO;
import database.DatabaseInitializer;
import gui.LoginFrame;
import gui.setup.AdminSetupFrame;
import service.SystemStartupService;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        try {

            DatabaseInitializer.initialize();

            UsuarioDAO usuarioDAO =
                    new UsuarioDAO();

            boolean hasUsers =
                    usuarioDAO.existsAnyUser();

            SwingUtilities.invokeLater(() -> {

                try {

                    if (!hasUsers) {

                        AdminSetupFrame frame =
                                new AdminSetupFrame();

                        frame.setVisible(true);

                        return;
                    }

                    boolean valid =
                            SystemStartupService
                                    .validateAdministratorKey();

                    if (!valid) {

                        JOptionPane.showMessageDialog(
                                null,
                                "Falha na validação da chave privada do administrador."
                        );

                        System.exit(0);
                    }

                    LoginFrame frame =
                            new LoginFrame();

                    frame.setVisible(true);

                } catch (Exception e) {

                    e.printStackTrace();

                    JOptionPane.showMessageDialog(
                            null,
                            e.getMessage()
                    );

                    System.exit(0);
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}