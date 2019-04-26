import model.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class KeyBindings {

    public static final int NORTH = 0;
    public static final int WEST = 1;
    public static final int SOUTH = 2;
    public static final int EAST = 3;

    private Player player;
    private GamePanel gamePanel;
    private boolean[] keysPressed; // used to track movement keys pressed: for repeated opposite same-axis movement

    public KeyBindings(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        player = gamePanel.getGameModel().getPlayer();
        keysPressed = new boolean[4]; // used for movement keys only right now
    }

    // Requires valid key name; getKeyStroke(String s) has parsing documentation
    public void addMoveAction(String name, int direction) {
        gamePanel.getInputMap().put(KeyStroke.getKeyStroke("pressed " + name), name);
        gamePanel.getActionMap().put(name, new MoveAction(direction, true));

        gamePanel.getInputMap().put(KeyStroke.getKeyStroke("released " + name),
                "released " + name);
        gamePanel.getActionMap().put("released " + name, new MoveAction(direction, false));
    }

    // Requires valid key name; getKeyStroke(String s) has parsing documentation
    public void addFireAction(String name) {
        gamePanel.getInputMap().put(KeyStroke.getKeyStroke("pressed " + name), name);
        gamePanel.getActionMap().put(name, new FireAction());
    }

    // Requires valid key name; getKeyStroke(String s) has parsing documentation
    public void addSwitchFireDirectionAction(String name) {
        gamePanel.getInputMap().put(KeyStroke.getKeyStroke("pressed " + name), name);
        gamePanel.getActionMap().put(name, new SwitchFireDirectionAction());
    }

    private class MoveAction extends AbstractAction {

        private int direction;
        private boolean pressed;


        public MoveAction(int direction, boolean pressed) {
            this.direction = direction;
            this.pressed = pressed;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            keysPressed[direction] = pressed;
            int speed = player.getMovementSpeed();
            switch (direction) {
                case NORTH:
                    if (pressed) {
                        player.setDy(-speed);
                    } else if (!keysPressed[SOUTH]) { // when the key is released and if player hasn't pressed the opposite direction at the same time
                        player.setDy(0);
                    }
                    break;
                case WEST:
                    if (pressed) {
                        player.setDx(-speed);
                    } else if (!keysPressed[EAST]) {
                        player.setDx(0);
                    }
                    break;
                case SOUTH:
                    if (pressed) {
                        player.setDy(speed);
                    } else if (!keysPressed[NORTH]) {
                        player.setDy(0);
                    }
                    break;
                case EAST:
                    if (pressed) {
                        player.setDx(speed);
                    } else if (!keysPressed[WEST]) {
                        player.setDx(0);
                    }
                    break;
            }
        }
    }

    private class FireAction extends AbstractAction {

        private Timer fireTimer;
        private boolean hasFired;

        public FireAction() {
            fireTimer = new Timer(player.getFiringSpeedDelay(), new ActionListener() { // sets up a timer to add delay between shots fired
                @Override
                public void actionPerformed(ActionEvent e) {
                    hasFired = false;
                }
            });
            fireTimer.setInitialDelay(player.getFiringSpeedDelay());
            fireTimer.start();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!hasFired) {
                player.fireProjectile();
                hasFired = true;
            }

        }
    }

    private class SwitchFireDirectionAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            player.switchFireDirection();
        }
    }

}

