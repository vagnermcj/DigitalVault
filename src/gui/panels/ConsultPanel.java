package gui.panels;

import crypto.AESService;
import crypto.EnvelopeService;
import crypto.RSAService;
import crypto.SignatureService;
import crypto.CertificateService;

import database.dao.ChaveiroDAO;
import database.entity.Chaveiro;
import database.entity.SecretFileRecord;
import database.entity.Usuario;

import gui.MainFrame;

import service.LogService;
import service.SecretFolderService;

import session.RuntimeSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.ByteArrayInputStream;

import java.nio.file.Files;
import java.nio.file.Path;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;

import java.util.List;

public class ConsultPanel extends JPanel {

    private MainFrame mainFrame;
    private JTextField txtFolderPath;
    private JPasswordField txtPassphrase;
    private JTable fileTable;

    private SecretFolderService service = new SecretFolderService();
    private List<SecretFileRecord> currentFiles;

    private PrivateKey adminPrivateKey;
    private PublicKey  adminPublicKey;

    public ConsultPanel(MainFrame frame) {
        this.mainFrame = frame;
        initComponents();
        registerScreenLog();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        inputPanel.add(new JLabel("Caminho da pasta:"), gbc);
        gbc.gridx = 1;
        txtFolderPath = new JTextField(30);
        inputPanel.add(txtFolderPath, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Frase secreta:"), gbc);
        gbc.gridx = 1;
        txtPassphrase = new JPasswordField(30);
        inputPanel.add(txtPassphrase, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JButton btnList = new JButton("Listar");
        btnList.addActionListener(e -> handleList());
        inputPanel.add(btnList, gbc);

        add(inputPanel, BorderLayout.NORTH);

        String[] columns = {"Nome Código", "Nome Secreto", "Dono", "Grupo"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        fileTable = new JTable(model);
        fileTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        fileTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = fileTable.getSelectedRow();
                    if (row >= 0) openFile(row);
                }
            }
        });

        add(new JScrollPane(fileTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnBack = new JButton("Voltar");
        btnBack.addActionListener(e -> {
            try {
                LogService.registrar(7002, RuntimeSession.getCurrentUser().getUid(), null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            mainFrame.showMainMenu();
        });
        bottom.add(btnBack);
        add(bottom, BorderLayout.SOUTH);
    }

    private void registerScreenLog() {
        try {
            LogService.registrar(7001, RuntimeSession.getCurrentUser().getUid(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleList() {
        Usuario current = RuntimeSession.getCurrentUser();

        try {
            LogService.registrar(7003, current.getUid(), null);

            String folder = txtFolderPath.getText().trim();
            if (folder.isEmpty() || !Files.isDirectory(Path.of(folder))) {
                LogService.registrar(7004, current.getUid(), null);
                JOptionPane.showMessageDialog(this, "Caminho de pasta inválido.");
                return;
            }

            String passphrase = new String(txtPassphrase.getPassword());

            ChaveiroDAO chaveiroDAO = new ChaveiroDAO();
            Chaveiro chaveiro = chaveiroDAO.findByUid(current.getUid());

            if (chaveiro == null) {
                JOptionPane.showMessageDialog(this, "Chave do usuário não encontrada.");
                return;
            }

            PrivateKey userPrivateKey = decryptPrivateKey(
                    chaveiro.getPrivateKeyEncrypted(),
                    passphrase
            );

            PublicKey userPublicKey = extractPublicKey(chaveiro.getCertificadoPem());

            boolean valid = SignatureService.validatePrivateKey(userPrivateKey, userPublicKey);
            if (!valid) {
                JOptionPane.showMessageDialog(this, "Frase secreta inválida.");
                return;
            }

            Chaveiro chaveiroAdmin = chaveiroDAO.findByUid(1);
            adminPublicKey  = extractPublicKey(chaveiroAdmin.getCertificadoPem());
            adminPrivateKey = RSAService.loadPrivateKey(
                    chaveiroAdmin.getPrivateKeyEncrypted(),
                    RuntimeSession.getAdminSecretPhrase()
            );

            currentFiles = service.loadIndex(folder, adminPrivateKey, adminPublicKey);

            LogService.registrar(7005, current.getUid(), null);
            LogService.registrar(7006, current.getUid(), null);

            populateTable();

            LogService.registrar(7009, current.getUid(), null);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                LogService.registrar(7007, current.getUid(), null);
            } catch (Exception ignored) {}
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }

    private void populateTable() {
        DefaultTableModel model = (DefaultTableModel) fileTable.getModel();
        model.setRowCount(0);
        for (SecretFileRecord r : currentFiles) {
            model.addRow(new Object[]{r.getCodigo(), r.getNome(), r.getDono(), r.getGrupo()});
        }
    }

    private void openFile(int row) {
        Usuario current = RuntimeSession.getCurrentUser();
        SecretFileRecord record = currentFiles.get(row);

        try {
            LogService.registrar(7010, current.getUid(), record.getNome());

            if (!service.canAccess(record, current)) {
                LogService.registrar(7012, current.getUid(), record.getNome());
                JOptionPane.showMessageDialog(this, "Acesso negado ao arquivo.");
                return;
            }

            LogService.registrar(7011, current.getUid(), record.getNome());

            // Arquivos são assinados/envelopados com chaves do admin
            service.decryptSecretFile(
                    txtFolderPath.getText().trim(),
                    record,
                    adminPrivateKey,
                    adminPublicKey
            );

            LogService.registrar(7013, current.getUid(), record.getNome());
            LogService.registrar(7014, current.getUid(), record.getNome());

            JOptionPane.showMessageDialog(this,
                    "Arquivo \"" + record.getNome() + "\" restaurado com sucesso.");

        } catch (Exception e) {
            e.printStackTrace();
            try {
                LogService.registrar(7015, current.getUid(), record.getNome());
            } catch (Exception ignored) {}
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }

    private PrivateKey decryptPrivateKey(byte[] encryptedKey, String passphrase)
            throws Exception {

        javax.crypto.SecretKey aesKey = AESService.generateKey(passphrase);
        byte[] decrypted = AESService.decrypt(encryptedKey, aesKey);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decrypted);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    private PublicKey extractPublicKey(String pem) throws Exception {

        byte[] bytes = pem.getBytes();
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate)
                cf.generateCertificate(new ByteArrayInputStream(bytes));

        return cert.getPublicKey();
    }
}