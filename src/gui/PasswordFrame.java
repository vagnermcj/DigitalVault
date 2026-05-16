package gui;

import database.entity.Usuario;
import gui.panels.VirtualKeyboardPanel;
import service.AuthenticationService;

import javax.swing.*;
import java.awt.*;

public class PasswordFrame extends JFrame {

    private Usuario usuario;

    private JTextField txtPassword;

    private AuthenticationService authService =
            new AuthenticationService();

    public PasswordFrame(Usuario usuario) {

        this.usuario = usuario;

        setTitle("Senha Pessoal");

        setSize(600, 300);

        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {

        JPanel panel = new JPanel(new BorderLayout(10,10));

        txtPassword = new JTextField();
        txtPassword.setEditable(false);

        panel.add(txtPassword, BorderLayout.NORTH);

        VirtualKeyboardPanel keyboard =
                new VirtualKeyboardPanel(txtPassword);

        panel.add(keyboard, BorderLayout.CENTER);

        JButton btnNext = new JButton("Validar");

        btnNext.addActionListener(e -> validatePassword());

        panel.add(btnNext, BorderLayout.SOUTH);

        add(panel);
    }

    private void validatePassword() {

        try {

            boolean valid = authService.authenticatePassword(
                    usuario,
                    txtPassword.getText()
            );

            if (!valid) {

                JOptionPane.showMessageDialog(this,
                        "Senha inválida");

                return;
            }

            TOTPFrame frame = new TOTPFrame(
                    usuario,
                    txtPassword.getText()
            );

            frame.setVisible(true);

            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    e.getMessage());
        }
    }
}