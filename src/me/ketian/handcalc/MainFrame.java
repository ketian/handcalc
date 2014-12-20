package me.ketian.handcalc;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Created by Ketian on 12/19/14.
 */
public class MainFrame extends JFrame {

    private boolean leftPanelFocused = true;
    private StringBuffer eq = new StringBuffer();
    private String answer;
    private NeuralNetwork nn = new NeuralNetwork();

    private void compute() {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        try {
            Object result = engine.eval(eq.toString());
            answer = result.toString();
        } catch (Exception ex) {
            answer = "ERROR";
        }
    }

    public MainFrame() {

        JPanel inputPanels = new JPanel(new GridLayout(1, 2, 5, 5));
        final InputPanel inputPanelLeft = new InputPanel();
        final InputPanel inputPanelRight = new InputPanel();
        final JTextField eqText = new JTextField(20);
        final JTextField ansText = new JTextField(27);
        final JButton eqButton = new JButton("=");

        inputPanelRight.setBorder(new LineBorder(Color.GRAY, 2));

        inputPanelLeft.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                inputPanelRight.setBorder(new LineBorder(Color.GRAY, 2));
                inputPanelLeft.setBorder(new LineBorder(Color.BLUE, 2));
                leftPanelFocused = true;
                if (inputPanelRight.isModified()) {
                    char op = nn.predict(inputPanelRight.getConvData());
                    eq.append(op);
                    eqText.setText(eq.toString());
                    inputPanelRight.resetData();
                    inputPanelRight.resetModified();
                }
            }
        });

        inputPanelRight.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                inputPanelLeft.setBorder(new LineBorder(Color.GRAY, 2));
                inputPanelRight.setBorder(new LineBorder(Color.BLUE, 2));
                leftPanelFocused = false;
                if (inputPanelLeft.isModified()) {
                    char op = nn.predict(inputPanelLeft.getConvData());
                    eq.append(op);
                    eqText.setText(eq.toString());
                    inputPanelLeft.resetData();
                    inputPanelLeft.resetModified();
                }
            }
        });

        inputPanels.add(inputPanelLeft);
        inputPanels.add(inputPanelRight);

        /// Space
        inputPanels.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"),
                "read");
        inputPanels.getActionMap().put("read", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InputPanel inputPanel;
                if (leftPanelFocused) inputPanel = inputPanelLeft;
                else inputPanel = inputPanelRight;
                // System.out.println("Key S pressed!");
                float[][] data = inputPanel.getConvData();
                char op = nn.predict(data);
                eq.append(op);
                eqText.setText(eq.toString());

                /*
                for (int i = 0; i < InputPanel.LETTER_WIDTH; ++i) {
                    for (int j = 0; j < InputPanel.LETTER_HEIGHT; ++j)
                        System.out.print(Integer.toString((int) data[j][i]) + "\t");
                    System.out.println();
                }
                */

                inputPanel.resetModified();
                inputPanel.resetData();
            }
        });

        /// Enter
        inputPanels.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"),
                "compute");
        inputPanels.getActionMap().put("compute", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compute();
                ansText.setText(answer);
            }
        });

        eqButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compute();
                ansText.setText(answer);
            }
        });

        eqText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                eq = new StringBuffer(eqText.getText());
            }
        });

        setLayout(new FlowLayout(FlowLayout.CENTER));
        JPanel eqPanel = new JPanel(new BorderLayout());
        add(inputPanels);
        eqPanel.add(eqText, BorderLayout.WEST);
        eqPanel.add(eqButton, BorderLayout.EAST);
        eqPanel.add(ansText, BorderLayout.SOUTH);
        add(eqPanel);

    }

    static public void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setTitle("Handcalc");
        mainFrame.setSize(350, 240);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }

}
