package gui;

import database.entity.Usuario;
import gui.panels.VirtualKeyboardPanel;
import service.AuthenticationService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

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

        VirtualKeyboardPanel keyboard =
                new VirtualKeyboardPanel();

        panel.add(keyboard, BorderLayout.CENTER);

        JButton btnNext = new JButton("Validar");

        btnNext.addActionListener(e -> validatePassword(keyboard));

        panel.add(btnNext, BorderLayout.SOUTH);

        add(panel);
    }

    private void validatePassword(VirtualKeyboardPanel keyboard) {

        try {

            if (!keyboard.isComplete()) {
                JOptionPane.showMessageDialog(this,
                        "Senha deve ter entre 8 e 10 dígitos");
                return;
            }

            List<String> possiblePasswords = keyboard.getAllPossiblePasswords();

            String valid = authService.authenticatePassword(
                    usuario,
                    possiblePasswords
            );

            if (Objects.equals(valid, "")) {

                JOptionPane.showMessageDialog(this,
                        "Senha inválida");

                return;
            }

            TOTPFrame frame = new TOTPFrame(
                    usuario,
                    valid
            );

            frame.setVisible(true);

            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    e.getMessage());
        }
    }
}