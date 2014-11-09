package me.ketian.handcalc;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Ketian on 11/8/14.
 */
public class PaintFrame extends JFrame{
    static final int BUTTON_SIZE = 10;
    static final int LEFT_BRACKET = 1;
    static final int RIGHT_BRACKET = 2;
    static final int PLUS = 3;
    static final int MINUS = 4;
    static final int TIMES = 5;
    static final int DIVIDE = 6;

    private int totalLeftBracket, totalRightBracket, totalPlus, totalMinus, totalTimes, totalDivide;
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

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // System.out.println("Key pressed!");
                // System.out.println(e.getKeyChar());
                if (e.getKeyChar() == 's') {
                    float[][] data = inputPanel.getConvData();
                    for (int i = 0; i < inputPanel.ROW; ++i) {
                        for (int j = 0; j < inputPanel.COL; ++j)
                            System.out.print(Integer.toString((int)data[j][i]) + "\t");
                        System.out.println();
                    }

                    inputPanel.resetData();
                }
            }
        });

    }

    static public void main(String[] args) {
        PaintFrame paintFrame = new PaintFrame();
        paintFrame.setTitle("Training Sample Generator");
        paintFrame.setSize(250, 250);
        paintFrame.setLocationRelativeTo(null);
        paintFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        paintFrame.setResizable(false);
        paintFrame.setVisible(true);
    }
}
