package gui.panels;

import gui.MainFrame;
import javax.swing.*;
import java.awt.*;

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
            btnRegister.addActionListener(e -> mainFrame.showRegisterPanel());
            menuPanel.add(btnRegister);
            menuPanel.add(Box.createVerticalStrut(10));
        }

        JButton btnConsult = createMenuButton("2 - Consultar pasta de arquivos secretos do usuário");
        btnConsult.addActionListener(e -> mainFrame.showConsultPanel());
        menuPanel.add(btnConsult);
        menuPanel.add(Box.createVerticalStrut(10));

        JButton btnExit = createMenuButton("3 - Sair do Sistema");
        btnExit.addActionListener(e -> mainFrame.showExitPanel());
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