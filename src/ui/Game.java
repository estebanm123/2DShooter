package ui;

import com.sun.tools.javac.Main;
import ui.gameplay.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class Game extends JFrame implements PropertyChangeListener {

    private MainPanel mainPanel;
    private InstructionsPanel instructionsPanel;
    private HomePanel homePanel;

    public Game() {
        super("Deep Sea Shooter");
        initMenus();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CardLayout cl = new CardLayout();
        setLayout(cl);
        setVisible(true);
        pack();
    }

    private void initMenus() {
        homePanel = new HomePanel();
        instructionsPanel = new InstructionsPanel();
        instructionsPanel.addObserver(this);
        homePanel.addObserver(this);
        instructionsPanel.setVisible(false);
        homePanel.setVisible(true);
        add(instructionsPanel);
        add(homePanel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(HomePanel.HOW_TO_PLAY)) {
            homePanel.setVisible(false);
            instructionsPanel.setVisible(true);
        } else if (evt.getPropertyName().equals(HomePanel.GAME_START)) {
            remove(homePanel);
            remove(instructionsPanel);
            mainPanel = new MainPanel((Integer) evt.getNewValue());
            add(mainPanel);
            validate();
            mainPanel.getGamePanel().grabFocus();
        } else if (evt.getPropertyName().equals(InstructionsPanel.INSTRUCTIONS_BACK)) {
            instructionsPanel.setVisible(false);
            homePanel.setVisible(true);
        }
    }

    public static void main(String[] args) {
        new Game();
    }

}
