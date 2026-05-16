package app;

import database.DatabaseInitializer;
import gui.LoginFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        try {

            DatabaseInitializer.initialize();

            SwingUtilities.invokeLater(() -> {

                LoginFrame frame = new LoginFrame();

                frame.setVisible(true);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}