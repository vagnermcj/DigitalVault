package gui;

import database.entity.Usuario;
import gui.MainFrame;
import service.AuthenticationService;

import javax.swing.*;
import java.awt.*;

public class TOTPFrame extends JFrame {

    private Usuario usuario;
    private String senha;

    private JTextField txtCode;

    private AuthenticationService authService =
            new AuthenticationService();

    public TOTPFrame(Usuario usuario,
                     String senha) {

        this.usuario = usuario;
        this.senha = senha;

        setTitle("Token TOTP");

        setSize(400, 200);

        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {

        JPanel panel = new JPanel(new GridLayout(3,1,10,10));

        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        txtCode = new JTextField();

        JButton btn = new JButton("Autenticar");

        btn.addActionListener(e -> validateTOTP());

        panel.add(new JLabel("Código Google Authenticator:"));
        panel.add(txtCode);
        panel.add(btn);

        add(panel);
    }

    private void validateTOTP() {

        try {

            boolean valid = authService.authenticateTOTP(
                    usuario,
                    senha,
                    txtCode.getText()
            );

            if (!valid) {

                JOptionPane.showMessageDialog(this,
                        "Token inválido");

                return;
            }

            MainFrame frame = new MainFrame(
                    usuario.getLogin(),
                    usuario.getGrupoNome(),
                    usuario.getNome(),
                    usuario.getTotalAcessos()
            );

            frame.setVisible(true);

            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    e.getMessage());
        }
    }
}
