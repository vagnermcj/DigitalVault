package gui.panels;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class VirtualKeyboardPanel extends JPanel {

    private List<int[]> clickedPairs;
    private JButton[] buttons;
    private JLabel displayLabel;
    private int[][] currentLayout;

    public VirtualKeyboardPanel() {
        clickedPairs = new ArrayList<>();
        buttons = new JButton[5];

        setLayout(new BorderLayout(10, 10));

        displayLabel = new JLabel("Senha: ", SwingConstants.CENTER);
        displayLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        add(displayLabel, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 5, 10, 10));

        for (int i = 0; i < 5; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font("Arial", Font.BOLD, 24));
            int index = i;
            buttons[i].addActionListener(e -> handleButtonClick(index));
            buttonsPanel.add(buttons[i]);
        }

        add(buttonsPanel, BorderLayout.CENTER);

        JButton clearButton = new JButton("Limpar");
        clearButton.addActionListener(e -> clear());
        add(clearButton, BorderLayout.SOUTH);

        generateRandomLayout();
    }

    private void generateRandomLayout() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        currentLayout = new int[5][2];
        for (int i = 0; i < 5; i++) {
            currentLayout[i][0] = numbers.get(i * 2);
            currentLayout[i][1] = numbers.get(i * 2 + 1);

            buttons[i].setText(currentLayout[i][0] + "/" + currentLayout[i][1]);
        }
    }

    private void handleButtonClick(int buttonIndex) {
        if (clickedPairs.size() >= 10) {
            JOptionPane.showMessageDialog(this,
                    "Senha deve ter no máximo 10 dígitos");
            return;
        }

        int[] pair = currentLayout[buttonIndex].clone();
        clickedPairs.add(pair);

        updateDisplay();

        generateRandomLayout();
    }

    private void updateDisplay() {
        StringBuilder display = new StringBuilder("Senha: ");
        for (int i = 0; i < clickedPairs.size(); i++) {
            display.append("*");
        }
        displayLabel.setText(display.toString());
    }

    private void clear() {
        clickedPairs.clear();
        updateDisplay();
        generateRandomLayout();
    }

    public List<int[]> getClickedPairs() {
        return clickedPairs;
    }

    public boolean isComplete() {
        return clickedPairs.size() >= 8 && clickedPairs.size() <= 10;
    }
}