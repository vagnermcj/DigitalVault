package gui.setup;

import gui.LoginFrame;
import util.QRCodeGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TOTPSetupDialog extends JDialog {

    public TOTPSetupDialog(String email, String totpSecret, boolean Admin) {

        setLayout(new BorderLayout(10, 10));
        setSize(500, 600);

        // Título
        JLabel title = new JLabel("Cadastro realizado com sucesso!", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        // Painel central
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Instruções
        JLabel instructions = new JLabel("<html><body style='width: 400px'>" +
                "<b>Configure o Google Authenticator:</b><br><br>" +
                "1. Abra o Google Authenticator no seu celular<br>" +
                "2. Toque em '+' para adicionar conta<br>" +
                "3. Escolha 'Escanear código QR' e escaneie o código abaixo<br><br>" +
                "<i>Ou insira manualmente a chave:</i>" +
                "</body></html>");
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(instructions);

        centerPanel.add(Box.createVerticalStrut(20));

        // QR Code
        try {
            String uri = QRCodeGenerator.generateOTPAuthURI("Cofre Digital", email, totpSecret);
            BufferedImage qrImage = QRCodeGenerator.generateQRCode(uri, 300, 300);

            JLabel qrLabel = new JLabel(new ImageIcon(qrImage));
            qrLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            qrLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            centerPanel.add(qrLabel);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Erro ao gerar QR Code");
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPanel.add(errorLabel);
        }

        centerPanel.add(Box.createVerticalStrut(20));

        // Chave manual
        JPanel keyPanel = new JPanel(new BorderLayout(5, 5));
        keyPanel.setMaximumSize(new Dimension(450, 80));

        JLabel keyLabel = new JLabel("Chave secreta:");
        keyLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JTextArea keyTextArea = new JTextArea(totpSecret);
        keyTextArea.setEditable(false);
        keyTextArea.setLineWrap(true);
        keyTextArea.setWrapStyleWord(true);
        keyTextArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        keyTextArea.setBackground(new Color(240, 240, 240));
        keyTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JButton copyButton = new JButton("Copiar");
        copyButton.addActionListener(e -> {
            java.awt.datatransfer.StringSelection stringSelection =
                    new java.awt.datatransfer.StringSelection(totpSecret);
            java.awt.Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(stringSelection, null);
            JOptionPane.showMessageDialog(this, "Chave copiada!");
        });

        keyPanel.add(keyLabel, BorderLayout.NORTH);
        keyPanel.add(keyTextArea, BorderLayout.CENTER);
        keyPanel.add(copyButton, BorderLayout.EAST);

        centerPanel.add(keyPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Botão OK
        JButton okButton = new JButton("OK, já configurei");
        okButton.addActionListener(e ->
        {
            if(Admin)
            {
                LoginFrame frame = new LoginFrame();
                frame.setVisible(true);
            }

            dispose();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(okButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}