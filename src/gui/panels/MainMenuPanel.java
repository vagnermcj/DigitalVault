package gui.panels;

import gui.MainFrame;
import service.LogService;
import session.RuntimeSession;

import javax.swing.*;
import java.awt.*;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class MainMenuPanel extends JPanel {

    private MainFrame mainFrame;
    private String userGroup;

    public MainMenuPanel(MainFrame frame, String group) {
        this.mainFrame = frame;
        this.userGroup = group;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        // Title
        JLabel title = new JLabel("Menu Principal:");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(title);
        menuPanel.add(Box.createVerticalStrut(20));

        // Buttons
        if (userGroup.equals("Administrador")) {
            JButton btnRegister = createMenuButton("1 - Cadastrar um novo usuário");
            btnRegister.addActionListener(e -> {
                mainFrame.showRegisterPanel();
                LogService.registrar(5002, RuntimeSession.getCurrentUser().getUid(), null);
            });
            menuPanel.add(btnRegister);
            menuPanel.add(Box.createVerticalStrut(10));
        }

        JButton btnConsult = createMenuButton("2 - Consultar pasta de arquivos secretos do usuário");
        btnConsult.addActionListener(e -> {
            mainFrame.showConsultPanel();
            LogService.registrar(5003, RuntimeSession.getCurrentUser().getUid(), null);
        });
        menuPanel.add(btnConsult);
        menuPanel.add(Box.createVerticalStrut(10));

        JButton btnExit = createMenuButton("3 - Sair do Sistema");
        btnExit.addActionListener(e -> {
            mainFrame.showExitPanel();
            LogService.registrar(5004, RuntimeSession.getCurrentUser().getUid(), null);
        });
        menuPanel.add(btnExit);

        add(menuPanel, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(500, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
}