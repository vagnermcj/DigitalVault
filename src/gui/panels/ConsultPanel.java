package gui.panels;

import crypto.CertificateService;
import crypto.RSAService;

import database.entity.SecretFileRecord;
import database.entity.Usuario;

import gui.MainFrame;

import service.LogService;
import service.SecretFolderService;
import service.UserPrivateKeyValidationService;
import service.ValidatedUserKeys;

import session.RuntimeSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.nio.file.Files;
import java.nio.file.Path;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import java.util.List;

public class ConsultPanel extends JPanel {

    private MainFrame mainFrame;

    private JTextField txtFolderPath;

    private JPasswordField txtPassphrase;

    private JTable fileTable;

    private SecretFolderService service =
            new SecretFolderService();

    private List<SecretFileRecord> currentFiles;

    public ConsultPanel(MainFrame frame) {

        this.mainFrame = frame;

        initComponents();

        registerScreenLog();
    }

    private void initComponents() {

        setLayout(new BorderLayout(10,10));

        setBorder(
                BorderFactory.createEmptyBorder(
                        20,30,20,30
                )
        );

        JPanel inputPanel =
                new JPanel(new GridBagLayout());

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx = 0;
        gbc.gridy = 0;

        inputPanel.add(
                new JLabel("Caminho da pasta:"),
                gbc
        );

        gbc.gridx = 1;

        txtFolderPath = new JTextField(30);

        inputPanel.add(txtFolderPath, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;

        inputPanel.add(
                new JLabel("Frase secreta:"),
                gbc
        );

        gbc.gridx = 1;

        txtPassphrase =
                new JPasswordField(30);

        inputPanel.add(txtPassphrase, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;

        JButton btnList =
                new JButton("Listar");

        btnList.addActionListener(
                e -> handleList()
        );

        inputPanel.add(btnList, gbc);

        add(inputPanel, BorderLayout.NORTH);

        String[] columns = {
                "Nome Código",
                "Nome Secreto",
                "Dono",
                "Grupo"
        };

        fileTable = new JTable(
                new DefaultTableModel(columns, 0)
        );

        fileTable.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            MouseEvent e
                    ) {

                        if (e.getClickCount() == 2) {

                            int row =
                                    fileTable.getSelectedRow();

                            if (row >= 0) {

                                openFile(row);
                            }
                        }
                    }
                }
        );

        JScrollPane scrollPane =
                new JScrollPane(fileTable);

        add(scrollPane, BorderLayout.CENTER);

        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER
                        )
                );

        JButton btnBack =
                new JButton("Voltar");

        btnBack.addActionListener(e -> {

            try {

                Usuario u =
                        RuntimeSession.getCurrentUser();

                LogService.registrar(
                        7002,
                        u.getUid(),
                        null
                );

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

            Usuario u =
                    RuntimeSession.getCurrentUser();

            LogService.registrar(
                    7001,
                    u.getUid(),
                    null
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void handleList() {

        Usuario current =
                RuntimeSession.getCurrentUser();

        try {

            LogService.registrar(
                    7003,
                    current.getUid(),
                    null
            );

            String folder =
                    txtFolderPath.getText();

            if (!Files.exists(Path.of(folder))) {

                LogService.registrar(
                        7004,
                        current.getUid(),
                        null
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Pasta inválida"
                );

                return;
            }

            // VALIDAÇÃO OBRIGATÓRIA

            ValidatedUserKeys keys =
                    UserPrivateKeyValidationService
                            .validate();

            PrivateKey privateKey =
                    keys.getPrivateKey();

            PublicKey publicKey =
                    keys.getPublicKey();

            // DECRIPTA ÍNDICE

            currentFiles =
                    service.loadIndex(
                            folder,
                            privateKey,
                            publicKey
                    );

            LogService.registrar(
                    7005,
                    current.getUid(),
                    null
            );

            LogService.registrar(
                    7006,
                    current.getUid(),
                    null
            );

            populateTable();

            LogService.registrar(
                    7009,
                    current.getUid(),
                    null
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Índice descriptografado com sucesso"
            );

        } catch (Exception e) {

            e.printStackTrace();

            try {

                LogService.registrar(
                        7007,
                        current.getUid(),
                        null
                );

            } catch (Exception ignored) {
            }

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );
        }
    }

    private void populateTable() {

        DefaultTableModel model =
                (DefaultTableModel)
                        fileTable.getModel();

        model.setRowCount(0);

        for (SecretFileRecord r : currentFiles) {

            model.addRow(
                    new Object[]{
                            r.getCodigo(),
                            r.getNome(),
                            r.getDono(),
                            r.getGrupo()
                    }
            );
        }
    }

    private void openFile(int row) {

        Usuario current =
                RuntimeSession.getCurrentUser();

        SecretFileRecord record =
                currentFiles.get(row);

        try {

            LogService.registrar(
                    7010,
                    current.getUid(),
                    record.getNome()
            );

            boolean allowed =
                    service.canAccess(
                            record,
                            current
                    );

            if (!allowed) {

                LogService.registrar(
                        7012,
                        current.getUid(),
                        record.getNome()
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Acesso negado"
                );

                return;
            }

            LogService.registrar(
                    7011,
                    current.getUid(),
                    record.getNome()
            );

            X509Certificate cert =
                    CertificateService.loadCertificate(
                            "admin.pem"
                    );

            PublicKey publicKey =
                    CertificateService.getPublicKey(cert);

            PrivateKey privateKey =
                    RSAService.loadPrivateKey(
                            "admin.bin",
                            RuntimeSession
                                    .getAdminSecretPhrase()
                    );

            service.decryptSecretFile(
                    txtFolderPath.getText(),
                    record,
                    privateKey,
                    publicKey
            );

            LogService.registrar(
                    7013,
                    current.getUid(),
                    record.getNome()
            );

            LogService.registrar(
                    7014,
                    current.getUid(),
                    record.getNome()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Arquivo restaurado com sucesso"
            );

        } catch (Exception e) {

            e.printStackTrace();

            try {

                LogService.registrar(
                        7015,
                        current.getUid(),
                        record.getNome()
                );

            } catch (Exception ignored) {
            }

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );
        }
    }
}