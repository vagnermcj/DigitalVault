package gui.panels;

import gui.MainFrame;
import javax.swing.*;
import java.awt.*;

public class ConsultPanel extends JPanel {

    private MainFrame mainFrame;
    private JTextField txtFolderPath;
    private JPasswordField txtPassphrase;
    private JTable fileTable;

    public ConsultPanel(MainFrame frame) {
        this.mainFrame = frame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        inputPanel.add(new JLabel("Caminho da pasta:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        txtFolderPath = new JTextField(30);
        inputPanel.add(txtFolderPath, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        inputPanel.add(new JLabel("Frase secreta:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        txtPassphrase = new JPasswordField(30);
        inputPanel.add(txtPassphrase, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JButton btnList = new JButton("Listar");
        btnList.addActionListener(e -> handleList());
        inputPanel.add(btnList, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // Table panel
        String[] columns = {"Nome Código", "Nome Secreto", "Dono", "Grupo"};
        Object[][] data = {}; // Empty initially

        fileTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(fileTable);
        add(scrollPane, BorderLayout.CENTER);

        // Back button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnBack = new JButton("Voltar");
        btnBack.addActionListener(e -> mainFrame.showMainMenu());
        bottomPanel.add(btnBack);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void handleList() {
        // TODO: Validar frase secreta
        // TODO: Decriptar índice
        // TODO: Listar arquivos na tabela
        JOptionPane.showMessageDialog(this,
                "Listagem em desenvolvimento",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }
}