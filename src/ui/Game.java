package ui;

import model.GameModel;
import ui.gameplay.GamePanel;
import ui.gameplay.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class Game extends JFrame implements PropertyChangeListener {

    public static final int WIDTH = GameModel.WIDTH;
    public static final int HEIGHT = GameModel.HEIGHT + GameModel.HEIGHT / 10;

    private MainPanel mainPanel;
    private InstructionsPanel instructionsPanel;
    private HomePanel homePanel;

    public Game() {
        super("Deep Sea Shooter");
        initMenus();
//        mainPanel = new MainPanel(NORMAL);
//        add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CardLayout cl = new CardLayout();
        setLayout(cl);
        setUndecorated(true);
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

    // receives alerts from the following classes:
    //  - HomePanel for switching to how to play display and starting game
    // - InstructionsPanel for switching back to HomePanel
    // - MainPanel's GamePanel for restarting game and going back to menu from game over
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case (HomePanel.HOW_TO_PLAY):
                homePanel.setVisible(false);
                instructionsPanel.setVisible(true);
                break;
            case (HomePanel.GAME_START):
                startGame(evt);
                break;
            case (HomePanel.QUIT):
                dispose();
                break;
            case (InstructionsPanel.INSTRUCTIONS_BACK):
                instructionsPanel.setVisible(false);
                homePanel.setVisible(true);
                break;
            case (GamePanel.BACK_TO_MENU):
                remove(mainPanel);
                add(homePanel);
                add(instructionsPanel);
                break;
            case (GamePanel.RESTART):
                removeMainPanelObservers();
                remove(mainPanel);
                mainPanel = new MainPanel(mainPanel.getDifficulty());
                addMainPanelObservers();
                add(mainPanel);
                mainPanel.getGamePanel().grabFocus();
                validate();
                break;
            case (GamePanel.PAUSE_GAME):
                mainPanel.pauseTimer();
                break;
            case (GamePanel.UN_PAUSE_GAME):
                mainPanel.unPauseTimer();
                break;
//            case (PausePanel.RESTART):
//
        }
    }

    private void startGame(PropertyChangeEvent evt) {
        remove(homePanel);
        remove(instructionsPanel);
        mainPanel = new MainPanel((Integer) evt.getNewValue());
        addMainPanelObservers();
        add(mainPanel);
        mainPanel.getGamePanel().grabFocus();
        validate();
    }

    private void removeMainPanelObservers() {
        mainPanel.getGamePanel().removeObserver(this);
        mainPanel.getGamePanel().getPausePanel().removeObserver(this);
    }

    private void addMainPanelObservers() {
        mainPanel.getGamePanel().addObserver(this);
        mainPanel.getGamePanel().getPausePanel().addObserver(this);
    }

    public static void main(String[] args) {
        new Game();
    }

}
