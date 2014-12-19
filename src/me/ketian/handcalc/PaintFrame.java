package me.ketian.handcalc;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;

/**
 * Created by Ketian on 11/8/14.
 */
public class PaintFrame extends JFrame{
    static final int BUTTON_SIZE = 10;
    static final int OP_OFFSET = 9;
    static final int LEFT_BRACKET = 1;
    static final int RIGHT_BRACKET = 2;
    static final int PLUS = 3;
    static final int MINUS = 4;
    static final int TIMES = 5;
    static final int DIVIDE = 6;
    static final int TOTAL_OPERATOR = 7;

    static final String DIR_PATH = "/Users/Summer/Desktop/handcalc/samples";

    private int[] total = new int[TOTAL_OPERATOR];
    private int operator;

    public PaintFrame() {
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 5));
        JButton leftBracketButton = new JButton("(");
        JButton rightBracketButton = new JButton(")");
        JButton plusButton = new JButton("+");
        JButton minusButton = new JButton("-");
        JButton timesButton = new JButton("x");
        JButton divideButton = new JButton("÷");

        leftBracketButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // System.out.println("Left_bracket clicked!");
                operator = LEFT_BRACKET;
            }
        });
        rightBracketButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                operator = RIGHT_BRACKET;
            }
        });
        plusButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                operator = PLUS;
            }
        });
        minusButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                operator = MINUS;
            }
        });
        timesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                operator = TIMES;
            }
        });
        divideButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                operator = DIVIDE;
            }
        });

        buttonsPanel.add(leftBracketButton);
        buttonsPanel.add(rightBracketButton);
        buttonsPanel.add(plusButton);
        buttonsPanel.add(minusButton);
        buttonsPanel.add(timesButton);
        buttonsPanel.add(divideButton);
        buttonsPanel.setBorder(new TitledBorder("Choose an operator: "));

        final InputPanel inputPanel = new InputPanel(new GridLayout(1, 2, 5, 5));

        buttonsPanel.setPreferredSize(new Dimension(230, 50));

        setLayout(new FlowLayout());
        add(buttonsPanel);
        add(inputPanel);

        buttonsPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"),
                "pressed");
        buttonsPanel.getActionMap().put("pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // System.out.println("Key S pressed!");
                float[][] data = inputPanel.getConvData();
/*
                for (int i = 0; i < inputPanel.LETTER_WIDTH; ++i) {
                    for (int j = 0; j < inputPanel.LETTER_HEIGHT; ++j)
                        System.out.print(Integer.toString((int) data[j][i]) + "\t");
                    System.out.println();
                }
*/
                do {
                    String filename = DIR_PATH + "/" +
                            Integer.toString(OP_OFFSET + operator) + "-" +
                            Integer.toString(total[operator]++) + ".txt";
                    File file = new File(filename);
                    if (!file.exists()) {
                        try {
                            PrintWriter output = new PrintWriter(file);
                            for (int i = 0; i < inputPanel.LETTER_WIDTH; ++i) {
                                for (int j = 0; j < inputPanel.LETTER_HEIGHT; ++j)
                                    output.print(Integer.toString((int) data[j][i]) + " ");
                                output.println();
                            }
                            output.close();
                            break;
                        } catch (Exception ex) {
                            // pass
                        }
                    }
                } while (true);

                inputPanel.resetData();
            }
        });

    }

    static public void main(String[] args) {
        if (!(new File(DIR_PATH).exists())) {
            boolean mkdirSuccess = (new File(DIR_PATH)).mkdirs();
            if (!mkdirSuccess) {
                System.out.println("Failed: mkdirs");
                return;
            }
        }
        PaintFrame paintFrame = new PaintFrame();
        paintFrame.setTitle("Training Sample Generator");
        paintFrame.setSize(250, 250);
        paintFrame.setLocationRelativeTo(null);
        paintFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        paintFrame.setResizable(false);
        paintFrame.setVisible(true);
    }
}
