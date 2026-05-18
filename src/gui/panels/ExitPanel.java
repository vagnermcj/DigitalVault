package gui.panels;

import gui.MainFrame;
import session.RuntimeSession;
import gui.LoginFrame;
import service.LogService;
import javax.swing.*;
import java.awt.*;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class ExitPanel extends JPanel {

    private MainFrame mainFrame;

    public ExitPanel(MainFrame frame) {
        this.mainFrame = frame;
        initComponents();
    }

    private void initComponents() {
        LogService.registrar(8001, RuntimeSession.getCurrentUser().getUid(), null);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Saída do sistema:");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel message = new JLabel("Pressione o botão Encerrar Sessão ou o botão Encerrar Sistema para confirmar.");
        message.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(title);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(message);
        centerPanel.add(Box.createVerticalStrut(30));

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnEndSession = new JButton("Encerrar Sessão");
        btnEndSession.addActionListener(e -> handleEndSession());

        JButton btnEndSystem = new JButton("Encerrar Sistema");
        btnEndSystem.addActionListener(e -> handleEndSystem());

        JButton btnBack = new JButton("Voltar");
        btnBack.addActionListener(e -> {
            LogService.registrar(8004, RuntimeSession.getCurrentUser().getUid(), null);
            mainFrame.showMainMenu();
        });

        buttonsPanel.add(btnEndSession);
        buttonsPanel.add(btnEndSystem);
        buttonsPanel.add(btnBack);

        centerPanel.add(buttonsPanel);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void disposeWindow() {

        Window window =
                SwingUtilities.getWindowAncestor(this);

        if (window != null) {
            window.dispose();
        }
    }

    private void handleEndSession() {

        try {

            LogService.registrar(
                    1004,
                    RuntimeSession
                            .getCurrentUser()
                            .getUid(),
                    null
            );

            RuntimeSession.clearUserSession();

            JOptionPane.showMessageDialog(
                    this,
                    "Sessão encerrada."
            );

            disposeWindow();

            LoginFrame frame =
                    new LoginFrame();

            frame.setVisible(true);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );
        }
    }

    private void handleEndSystem() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja encerrar o sistema?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            LogService.registrar(8003, RuntimeSession.getCurrentUser().getUid(), null);
            LogService.registrar(1002, null, null);
            RuntimeSession.clearAll();
            System.exit(0);
        }
    }
}