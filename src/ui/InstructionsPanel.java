package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class InstructionsPanel extends JPanel {

    public static final String INSTRUCTIONS_BACK = "back";

    public static final int WIDTH = HomePanel.WIDTH;
    public static final int HEIGHT = HomePanel.HEIGHT;

    private PropertyChangeSupport support;
    private JLabel titleLabel;
    private JTextArea instructionsArea;
    private JButton backButton;
    private GridBagConstraints c;

    public InstructionsPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        c = new GridBagConstraints();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        initTitleLabel();
        initInstructionsArea();
        initBackButton();
        support = new PropertyChangeSupport(this);
    }

    private void initTitleLabel() {
        titleLabel = new JLabel("HOW TO PLAY");
        titleLabel.setFont(new Font("Impact", Font.PLAIN, 30));
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(50, 0, 0, 0);
        add(titleLabel, c);
    }

    private void initInstructionsArea() {
        instructionsArea = new JTextArea();
        instructionsArea.setText("Controls: \n\n\nW - up\nA - left\nS - down\nD - right\n" +
                        "\nF - switch direction\nSPACE - fire\nESC - pause");
        instructionsArea.setFont(new Font("Impact", Font.PLAIN, 20));
        c.weighty = 1;
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridx = 0;
        c.gridy = 2;
        add(instructionsArea, c);
    }

    private void initBackButton() {
        backButton = new JButton("BACK");
        backButton.setFont(new Font("Impact", Font.PLAIN, 15));
        backButton.setFocusPainted(false);
        backButton.setBackground(HomePanel.COLOR_BUTTON);
        c.insets = new Insets(0, 0, 0, 500);
        c.weighty = .2;
        c.gridx = 0;
        c.gridy = 1;
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                support.firePropertyChange(INSTRUCTIONS_BACK, null, null);
            }
        });
        add(backButton, c);
    }

    public void addObserver(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }
}
