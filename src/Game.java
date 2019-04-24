import model.GameModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Game extends JFrame {

    public static final int DELAY = 1000 / 60;
    private GameModel gameModel;
    private GamePanel gamePanel;

    public Game() {
        super("TBD");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameModel = new GameModel();
        gamePanel = new GamePanel(gameModel);
        initKeyBindings();
        add(gamePanel);
        pack();
        setVisible(true);
        addTimer();
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

    }

    public static void main(String[] args) {
        new Game();
    }
}
