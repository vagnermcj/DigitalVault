package gui.panels;

import crypto.CertificateService;
import gui.MainFrame;

import javax.security.auth.x500.X500Principal;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import service.CadastroService;
import service.LogService;
import session.RuntimeSession;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class RegisterPanel extends JPanel {

    private MainFrame mainFrame;

    private JTextField txtCertPath;
    private JTextField txtKeyPath;
    private JPasswordField txtPassphrase;
    private JComboBox<String> cmbGroup;
    private JPasswordField txtPassword;
    private JPasswordField txtPasswordConfirm;

    public RegisterPanel(MainFrame frame) {
        this.mainFrame = frame;
        initComponents();
    }

    private void initComponents() {
        LogService.registrar(6001, RuntimeSession.getCurrentUser().getUid(), null);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Formulário de Cadastro", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;

        addFormField(formPanel, gbc, row++, "Caminho do arquivo do certificado digital:",
                txtCertPath = new JTextField(30));

        addFormField(formPanel, gbc, row++, "Caminho do arquivo da chave privada:",
                txtKeyPath = new JTextField(30));

        addFormField(formPanel, gbc, row++, "Frase secreta:",
                txtPassphrase = new JPasswordField(30));

        String[] groups = {"Administrador", "Usuário"};
        cmbGroup = new JComboBox<>(groups);
        addFormField(formPanel, gbc, row++, "Grupo:", cmbGroup);

        addFormField(formPanel, gbc, row++, "Senha pessoal:",
                txtPassword = new JPasswordField(30));

        addFormField(formPanel, gbc, row++, "Confirmação senha pessoal:",
                txtPasswordConfirm = new JPasswordField(30));

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnRegister = new JButton("Cadastrar");
        btnRegister.addActionListener(e -> handleRegister());

        JButton btnBack = new JButton("Voltar");
        btnBack.addActionListener(e -> {
            mainFrame.showMainMenu();
            LogService.registrar(6010, RuntimeSession.getCurrentUser().getUid(), null);
        });

        buttonsPanel.add(btnRegister);
        buttonsPanel.add(btnBack);

        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row,
                              String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    private void handleRegister() {
        LogService.registrar(6002, RuntimeSession.getCurrentUser().getUid(), null);
        try {

            String senha =
                    new String(txtPassword.getPassword());

            String confirmacao =
                    new String(txtPasswordConfirm.getPassword());

            if (!senha.equals(confirmacao)) {

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

            X509Certificate cert =
                    CertificateService.loadCertificate(
                            txtCertPath.getText()
                    );

            boolean confirmed =
                    showCertificateConfirmation(cert);

            if (!confirmed) {
                return;
            }


            CadastroService service = new CadastroService();

            service.cadastrar(
                    txtCertPath.getText(),
                    txtKeyPath.getText(),
                    new String(txtPassphrase.getPassword()),
                    senha,
                    Objects.equals(String.valueOf(cmbGroup.getSelectedItem()), "Administrador")
            );

            JOptionPane.showMessageDialog(this,
                    "Usuário cadastrado com sucesso");

              txtCertPath.setText("");
              txtKeyPath.setText("");
              txtPassphrase.setText("");
              txtPassword.setText("");
              txtPasswordConfirm.setText("");


        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    e.getMessage());
        }
    }

    private boolean showCertificateConfirmation(X509Certificate cert)
            throws Exception {

        String subject =
                formatX500Name(
                        cert.getSubjectX500Principal()
                                .getName(X500Principal.RFC1779)
                );

        String issuer =
                formatX500Name(
                        cert.getIssuerX500Principal()
                                .getName(X500Principal.RFC1779)
                );

        String email = "";

        Matcher matcher =
                Pattern.compile(
                                "(?:EMAILADDRESS|OID\\.1\\.2\\.840\\.113549\\.1\\.9\\.1)=([^,]+)"
                        )
                        .matcher(subject);

        if (matcher.find()) {
            email = matcher.group(1);
        }

        String message =
                "Versão: " + cert.getVersion() + "\n\n" +
                        "Série: " + cert.getSerialNumber() + "\n\n" +
                        "Validade: " + cert.getNotBefore() +
                        " até " + cert.getNotAfter() + "\n\n" +
                        "Tipo de Assinatura: " + cert.getSigAlgName() + "\n\n" +
                        "Emissor: " + extractCN(issuer) + "\n\n" +
                        "Sujeito: " + extractCN(subject) + "\n\n" +
                        "E-mail: " + email + "\n\n" +
                        "Confirmar cadastro?";

        int option =
                JOptionPane.showConfirmDialog(
                        this,
                        message,
                        "Confirmar Certificado",
                        JOptionPane.YES_NO_OPTION
                );

        return option == JOptionPane.YES_OPTION;
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

    private String formatX500Name(String name) {

        return name.replace(
                "OID.1.2.840.113549.1.9.1=",
                "EMAILADDRESS="
        );
    }

    private String extractCN(String x500Name) {

        Matcher matcher =
                Pattern.compile("CN=([^,]+)")
                        .matcher(x500Name);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return x500Name;
    }
}