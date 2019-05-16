package ui;

import model.GameModel;
import ui.gameplay.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class PausePanel extends JPanel {

    public static final String RESTART = "Restart PausePanel";

    private GridBagConstraints c;
    private PropertyChangeSupport support;

    public PausePanel() {
        setPreferredSize(new Dimension(GameModel.WIDTH, GameModel.HEIGHT));
        setOpaque(false);
        setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        support = new PropertyChangeSupport(this);
        initComponents();
    }

    public void addObserver(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removeObserver(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }


    private void initComponents() {
        createPauseLabel();
        createRestartButton();
        createMenuButton();
    }

    private void createRestartButton() {
        JButton restartButton = new JButton("RESTART");
        restartButton.setFont(new Font("Impact", Font.PLAIN, 15));
        restartButton.setFocusPainted(false);
        restartButton.setBackground(Color.WHITE);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                support.firePropertyChange(GamePanel.RESTART, null, null);
            }
        });
        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 1;
        add(restartButton, c);
        validate();
    }

    private void createMenuButton() {
        JButton menuButton = new JButton("BACK TO MENU");
        menuButton.setFont(new Font("Impact", Font.PLAIN, 15));
        menuButton.setFocusPainted(false);
        menuButton.setBackground(Color.WHITE);
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                support.firePropertyChange(GamePanel.BACK_TO_MENU, null, null);
            }
        });
        c.gridy = 1;
        c.gridx = 1;
        c.gridwidth = 1;
        add(menuButton, c);
        validate();
    }


    private void createPauseLabel() {
        JLabel pauseLabel = new JLabel("PAUSED");
        pauseLabel.setBackground(GamePanel.COLOR);
        pauseLabel.setForeground(Color.WHITE);
        pauseLabel.setFont(new Font("Impact", Font.PLAIN, 25));
        pauseLabel.setOpaque(true);
        c.gridy = 0;
        c.gridwidth = 10;
        add(pauseLabel, c);
        validate();
    }
}
