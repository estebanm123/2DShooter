package ui.gameplay;

import model.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

public class MainPanel extends JPanel {

    public static final int DELAY = 1000 / 60;

    private GameModel gameModel;
    private GamePanel gamePanel;
    private ScorePanel scorePanel;
    private Timer gameTimer;

    //Starts gameplay when an instance is created
    public MainPanel(int difficulty) {
        gameModel = new GameModel(difficulty);
        initPanels();
        initKeyBindings();
        addTimer();
    }

    public int getDifficulty() {
        return gameModel.getDifficulty();
    }

    private void initPanels() {
        setLayout(new GridBagLayout());
        scorePanel = new ScorePanel();
        gameModel.addObserver(scorePanel);
        gamePanel = new GamePanel(gameModel);
        gamePanel.setFocusable(true);
        gamePanel.setRequestFocusEnabled(true);
        gameModel.addObserver(gamePanel);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        add(gamePanel, c);
        c.gridx = 0;
        c.gridy = 0;
        add(scorePanel, c);
    }

    private void addTimer() {
        gameTimer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameModel.update();
                gamePanel.repaint();
            }
        });
        gameTimer.start();

    }

    public void pauseTimer() {
        gameTimer.stop();
    }

    public void unPauseTimer() {
        gameTimer.start();
    }

    private void initKeyBindings() {
        KeyBindings kb = new KeyBindings(gamePanel);
        kb.addMoveAction("W", KeyBindings.NORTH);
        kb.addMoveAction("A", KeyBindings.WEST);
        kb.addMoveAction("S", KeyBindings.SOUTH);
        kb.addMoveAction("D", KeyBindings.EAST);
        kb.addFireAction("SPACE");
        kb.addSwitchFireDirectionAction("F");
        kb.addPauseAction("ESCAPE");
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}
