package gui.setup;

import gui.LoginFrame;
import service.CadastroService;
import session.RuntimeSession;

import javax.swing.*;
import java.awt.*;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class AdminSetupFrame extends JFrame {

    private JTextField txtCert;
    private JTextField txtKey;

    private JPasswordField txtPhrase;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirm;

    public AdminSetupFrame() {

        setTitle("Cadastro Inicial do Administrador");

        setSize(600, 400);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {

        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5,5,5,5);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        txtCert = new JTextField(30);
        txtKey = new JTextField(30);

        txtPhrase = new JPasswordField(30);

        txtPassword = new JPasswordField(30);
        txtConfirm = new JPasswordField(30);

        addField(panel, gbc, row++,
                "Certificado:",
                txtCert);

        addField(panel, gbc, row++,
                "Chave privada:",
                txtKey);

        addField(panel, gbc, row++,
                "Frase secreta:",
                txtPhrase);

        addField(panel, gbc, row++,
                "Senha pessoal:",
                txtPassword);

        addField(panel, gbc, row++,
                "Confirmar senha:",
                txtConfirm);

        JButton btn = new JButton(
                "Cadastrar Administrador"
        );

        btn.addActionListener(e -> register());

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;

        panel.add(btn, gbc);

        add(panel);
    }

    private void addField(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String label,
            JComponent field
    ) {

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;

        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;

        panel.add(field, gbc);
    }

    private void register() {

        try {

            String senha =
                    new String(txtPassword.getPassword());

            String confirm =
                    new String(txtConfirm.getPassword());

            if (!senha.equals(confirm)) {

                JOptionPane.showMessageDialog(this,
                        "Senhas não coincidem");

                return;
            }
            else if(hasConsecutiveRepeats(senha, 3))
            {
                JOptionPane.showMessageDialog(this,
                        "Senha com sequência de numeros repetidos");

                return;
            }
            else if(!senha.matches("\\d+"))
            {
                JOptionPane.showMessageDialog(this,
                        "Senha deve possuir apenas digitos");

                return;
            }
            else if(senha.isEmpty())
            {
                JOptionPane.showMessageDialog(this,
                        "Senha deve possuir apenas digitos");

                return;
            }
            else if(senha.length() < 8 || senha.length() > 10)
            {
                JOptionPane.showMessageDialog(this,
                        "Senha deve ser entre 8 e 10 digitos");

                return;
            }

            CadastroService service =
                    new CadastroService();

            service.cadastrar(
                    txtCert.getText(),
                    txtKey.getText(),
                    new String(txtPhrase.getPassword()),
                    senha,
                    true
            );

            RuntimeSession.setAdminSecretPhrase(
                    new String(txtPhrase.getPassword())
            );

            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );
        }
    }

    private static boolean hasConsecutiveRepeats(String senha, int count) {
        for (int i = 0; i <= senha.length() - count; i++) {
            char digit = senha.charAt(i);
            boolean allSame = true;

            for (int j = 1; j < count; j++) {
                if (senha.charAt(i + j) != digit) {
                    allSame = false;
                    break;
                }
            }

            if (allSame) {
                return true;
            }
        }
        return false;
    }
}

