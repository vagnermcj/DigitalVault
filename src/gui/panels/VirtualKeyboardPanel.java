package gui.panels;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VirtualKeyboardPanel extends JPanel {

    private JButton[] buttons = new JButton[5];

    private JTextField target;

    public VirtualKeyboardPanel(JTextField target) {

        this.target = target;

        setLayout(new GridLayout(1,5,5,5));

        generateButtons();
    }

    private void generateButtons() {

        removeAll();

        List<Integer> numbers = new ArrayList<>();

        for (int i = 0; i <= 9; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        for (int i = 0; i < 5; i++) {

            int n1 = numbers.get(i * 2);
            int n2 = numbers.get(i * 2 + 1);

            JButton button = new JButton(n1 + " ou " + n2);

            int finalN1 = n1;
            int finalN2 = n2;

            button.addActionListener(e -> {

                String value = JOptionPane.showInputDialog(
                        "Digite qual número deseja usar:"
                );

                if (value == null)
                    return;

                if (value.equals(String.valueOf(finalN1)) ||
                        value.equals(String.valueOf(finalN2))) {

                    target.setText(target.getText() + value);

                    generateButtons();
                }
            });

            buttons[i] = button;

            add(button);
        }

        revalidate();
        repaint();
    }
}