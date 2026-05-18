package gui.panels;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class VirtualKeyboardPanel extends JPanel {

    private List<int[]> clickedPairs; // Armazena pares clicados
    private JButton[] buttons;
    private JLabel displayLabel;
    private int[][] currentLayout; // Layout atual dos números

    public VirtualKeyboardPanel() {
        clickedPairs = new ArrayList<>();
        buttons = new JButton[5];

        setLayout(new BorderLayout(10, 10));

        // Display da senha (asteriscos)
        displayLabel = new JLabel("Senha: ", SwingConstants.CENTER);
        displayLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        add(displayLabel, BorderLayout.NORTH);

        // Painel de botões
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 5, 10, 10));

        for (int i = 0; i < 5; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font("Arial", Font.BOLD, 24));
            int index = i;
            buttons[i].addActionListener(e -> handleButtonClick(index));
            buttonsPanel.add(buttons[i]);
        }

        add(buttonsPanel, BorderLayout.CENTER);

        // Botão limpar
        JButton clearButton = new JButton("Limpar");
        clearButton.addActionListener(e -> clear());
        add(clearButton, BorderLayout.SOUTH);

        // Gerar layout inicial
        generateRandomLayout();
    }

    private void generateRandomLayout() {
        // Criar lista com números 0-9
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        // Distribuir em 5 botões (2 números cada)
        currentLayout = new int[5][2];
        for (int i = 0; i < 5; i++) {
            currentLayout[i][0] = numbers.get(i * 2);
            currentLayout[i][1] = numbers.get(i * 2 + 1);

            // Atualizar texto do botão
            buttons[i].setText(currentLayout[i][0] + "/" + currentLayout[i][1]);
        }
    }

    private void handleButtonClick(int buttonIndex) {
        // Verificar tamanho máximo (10 dígitos)
        if (clickedPairs.size() >= 10) {
            JOptionPane.showMessageDialog(this,
                    "Senha deve ter no máximo 10 dígitos");
            return;
        }

        // Registrar o par de números
        int[] pair = currentLayout[buttonIndex].clone();
        clickedPairs.add(pair);

        // Atualizar display com asteriscos
        updateDisplay();

        // Redistribuir números
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

    public List<String> getAllPossiblePasswords() {
        if (clickedPairs.size() < 8) {
            return Collections.emptyList();
        }

        List<String> combinations = new ArrayList<>();
        generateCombinations(clickedPairs, 0, "", combinations);
        return combinations;
    }

    private void generateCombinations(List<int[]> pairs, int index,
                                      String current, List<String> results) {
        if (index == pairs.size()) {
            results.add(current);
            return;
        }

        int[] pair = pairs.get(index);
        generateCombinations(pairs, index + 1, current + pair[0], results);
        generateCombinations(pairs, index + 1, current + pair[1], results);
    }

    public boolean isComplete() {
        return clickedPairs.size() >= 8 && clickedPairs.size() <= 10;
    }
}