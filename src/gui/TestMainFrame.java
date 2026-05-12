package gui;

import javax.swing.*;

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