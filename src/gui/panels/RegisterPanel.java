package gui.panels;

import gui.MainFrame;
import javax.swing.*;
import java.awt.*;
import service.CadastroService;

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
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Title
        JLabel title = new JLabel("Formulário de Cadastro", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;

        // Certificate path
        addFormField(formPanel, gbc, row++, "Caminho do arquivo do certificado digital:",
                txtCertPath = new JTextField(30));

        // Private key path
        addFormField(formPanel, gbc, row++, "Caminho do arquivo da chave privada:",
                txtKeyPath = new JTextField(30));

        // Passphrase
        addFormField(formPanel, gbc, row++, "Frase secreta:",
                txtPassphrase = new JPasswordField(30));

        // Group
        String[] groups = {"Administrador", "Usuário"};
        cmbGroup = new JComboBox<>(groups);
        addFormField(formPanel, gbc, row++, "Grupo:", cmbGroup);

        // Password
        addFormField(formPanel, gbc, row++, "Senha pessoal:",
                txtPassword = new JPasswordField(30));

        // Password confirmation
        addFormField(formPanel, gbc, row++, "Confirmação senha pessoal:",
                txtPasswordConfirm = new JPasswordField(30));

        add(formPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnRegister = new JButton("Cadastrar");
        btnRegister.addActionListener(e -> handleRegister());

        JButton btnBack = new JButton("Voltar");
        btnBack.addActionListener(e -> mainFrame.showMainMenu());

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

            CadastroService service = new CadastroService();

            service.cadastrar(
                    txtCertPath.getText(),
                    txtKeyPath.getText(),
                    new String(txtPassphrase.getPassword()),
                    senha
            );

            JOptionPane.showMessageDialog(this,
                    "Usuário cadastrado com sucesso");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    e.getMessage());
        }
    }
}