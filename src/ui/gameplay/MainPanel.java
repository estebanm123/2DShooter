package ui.gameplay;

import model.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JPanel {

    public static final int DELAY = 1000 / 60;

    private GameModel gameModel;
    private GamePanel gamePanel;
    private ScorePanel scorePanel;


    //Starts gameplay when an instance is created
    public MainPanel(int difficulty) {
        gameModel = new GameModel(difficulty);
        initPanels();
        initKeyBindings();
        addTimer();
    }

    private void initPanels() {
        setLayout(new GridBagLayout());
        scorePanel = new ScorePanel();
        gameModel.addObserver(scorePanel);
        gamePanel = new GamePanel(gameModel);
        gamePanel.setFocusable(true);
        gamePanel.setRequestFocusEnabled(true);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        add(gamePanel, c);
        c.gridx = 0;
        c.gridy = 0; // the score panel is set to be on top of the gameplay panel
        add(scorePanel, c);
       // gamePanel.grabFocus();
    }

    private void addTimer() {
        Timer t = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameModel.update();
                gamePanel.repaint();
            }
        });
        t.start();
    }

    private void initKeyBindings() {
        KeyBindings kb = new KeyBindings(gamePanel);
        kb.addMoveAction("W", KeyBindings.NORTH);
        kb.addMoveAction("A", KeyBindings.WEST);
        kb.addMoveAction("S", KeyBindings.SOUTH);
        kb.addMoveAction("D", KeyBindings.EAST);
        kb.addFireAction("SPACE");
        kb.addSwitchFireDirectionAction("F");
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}
