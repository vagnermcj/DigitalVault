package gui;

import javax.swing.*;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class TestMainFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(
                    "admin@inf1416.puc-rio.br",
                    "Administrador",
                    "João Silva",
                    15
            );
            frame.setVisible(true);
        });
    }
}