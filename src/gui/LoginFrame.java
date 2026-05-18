package gui;

import database.entity.Usuario;
import gui.MainFrame;
import service.AuthenticationService;
import service.LogService;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class LoginFrame extends JFrame {

    private JTextField txtLogin;

    private AuthenticationService authService =
            new AuthenticationService();

    public LoginFrame() {

        setTitle("Autenticação - Cofre Digital");

        setSize(400, 200);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JPanel form = new JPanel(new GridLayout(2,1,10,10));

        txtLogin = new JTextField();

        form.add(new JLabel("Login (e-mail):"));
        form.add(txtLogin);

        JButton btnNext = new JButton("Próximo");

        btnNext.addActionListener(e -> authenticate());

        panel.add(form, BorderLayout.CENTER);
        panel.add(btnNext, BorderLayout.SOUTH);

        add(panel);
    }

    private void authenticate() {
        LogService.registrar(2001, null, null);
        try {

            Usuario usuario = authService.authenticateLogin(
                    txtLogin.getText()
            );

            if (usuario == null) {
                LogService.registrar(2005, null, null);
                JOptionPane.showMessageDialog(this,
                        "Usuário não encontrado");

                return;
            }
            else if (usuario.getBloqueadoAte() != null &&
                    LocalDateTime.now().isBefore(usuario.getBloqueadoAte().toLocalDateTime())) {

                JOptionPane.showMessageDialog(this,
                        "Usuário está bloqueado!");

                LogService.registrar(2004, null, null);
                return;
            }

            usuario.setBloqueadoAte(null);
            usuario.setErrosSenha(0);
            usuario.setErrosTotp(0);
            LogService.registrar(2003, null, null);

            PasswordFrame frame =
                    new PasswordFrame(usuario);

            frame.setVisible(true);

            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    e.getMessage());
        }
    }
}