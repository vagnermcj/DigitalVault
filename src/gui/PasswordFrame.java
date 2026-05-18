package gui;

import database.entity.Usuario;
import gui.panels.VirtualKeyboardPanel;
import service.AuthenticationService;
import service.LogService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

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
            LogService.registrar(3001, usuario.getUid(), null);
            if (!keyboard.isComplete()) {
                JOptionPane.showMessageDialog(this,
                        "Senha deve ter entre 8 e 10 dígitos");
                return;
            }

            String valid =
                    authService.authenticatePassword(
                            usuario,
                            keyboard.getClickedPairs()
                    );

            if (Objects.equals(valid, "")) {
                JOptionPane.showMessageDialog(this,
                        "Senha inválida");

                return;
            }
            else if(Objects.equals(valid, "blocked"))
            {
                JOptionPane.showMessageDialog(this,
                        "Tentativas máximas alcançadas! Usuário bloqueado por 2 minutos");
                LogService.registrar(3006, usuario.getUid(), null);
                LogService.registrar(3007, usuario.getUid(), null);
                LogService.registrar(2002, null, null);
                LoginFrame frame = new LoginFrame();
                frame.setVisible(true);
                dispose();
                return;
            }

            LogService.registrar(3003, usuario.getUid(), null);
            LogService.registrar(3002, usuario.getUid(), null);

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