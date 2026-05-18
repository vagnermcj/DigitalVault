package gui;

import gui.panels.*;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JLabel lblLogin;
    private JLabel lblGroup;
    private JLabel lblName;

    private JLabel lblInfo;

    private JPanel body2Container;
    private JPanel currentBody2Panel;

    private String userLogin;
    private String userGroup;
    private String userName;
    private int totalAccesses;
    private int totalUsers;

    public MainFrame(String login, String group, String name, int accesses) {
        this.userLogin = login;
        this.userGroup = group;
        this.userName = name;
        this.totalAccesses = accesses;

        initComponents();
        showMainMenu();
    }

    private void initComponents() {
        setTitle("Cofre Digital");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(createHeader(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(createBody1(), BorderLayout.NORTH);

        body2Container = new JPanel(new BorderLayout());
        centerPanel.add(body2Container, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createTitledBorder("Informações do Usuário")
        ));
        panel.setBackground(new Color(240, 240, 240));

        lblLogin = new JLabel("Login: " + userLogin);
        lblGroup = new JLabel("Grupo: " + userGroup);
        lblName = new JLabel("Nome: " + userName);

        Font font = new Font("Arial", Font.PLAIN, 14);
        lblLogin.setFont(font);
        lblGroup.setFont(font);
        lblName.setFont(font);

        panel.add(lblLogin);
        panel.add(lblGroup);
        panel.add(lblName);

        return panel;
    }

    private JPanel createBody1() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        lblInfo = new JLabel("Total de acessos do usuário: " + totalAccesses);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(lblInfo);

        return panel;
    }

    public void updateBody1Info(String info) {
        lblInfo.setText(info);
    }

    public void switchBody2Panel(JPanel newPanel) {
        body2Container.removeAll();
        body2Container.add(newPanel, BorderLayout.CENTER);
        currentBody2Panel = newPanel;
        body2Container.revalidate();
        body2Container.repaint();
    }

    public void showMainMenu() {
        updateBody1Info("Total de acessos do usuário: " + totalAccesses);
        MainMenuPanel panel = new MainMenuPanel(this, userGroup);
        switchBody2Panel(panel);
    }

    public void showRegisterPanel() {
        updateBody1Info("Total de usuários do sistema: "); // Mock data
        RegisterPanel panel = new RegisterPanel(this);
        switchBody2Panel(panel);
    }

    public void showConsultPanel() {
        updateBody1Info("Total de consultas do usuário: " + totalAccesses);
        ConsultPanel panel = new ConsultPanel(this);
        switchBody2Panel(panel);
    }

    public void showExitPanel() {
        updateBody1Info("Total de acessos do usuário: " + totalAccesses);
        ExitPanel panel = new ExitPanel(this);
        switchBody2Panel(panel);
    }

    public String getUserLogin() { return userLogin; }
    public String getUserGroup() { return userGroup; }
    public String getUserName() { return userName; }
}