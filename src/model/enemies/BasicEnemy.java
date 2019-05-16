package model.enemies;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class BasicEnemy extends Enemy {

    public static final int SIZE_X = 50;
    public static final int SIZE_Y = 40;
    public static final Color COLOR = new Color(198, 156, 158);
    public static final Color DEAD_COLOR = new Color(122, 24, 23);
    public static final int EYE_SIZE_X = 7;
    public static final int EYE_SIZE_Y = 7;
    public static final Color EYE_COLOR = new Color(250, 100, 100);

    public static final int MOVEMENT_TIMER_DELAY = 500;
    public static final int NORMAL_SPEED = 5;
    public static final int EASY_SPEED = 3;

    private Timer movementTimer;
    private boolean dirX; // these booleans determine which direction the enemy is moving in on each axis
    private boolean dirY;
    private static int basicEnemySpeed = NORMAL_SPEED;

    public BasicEnemy(int x, int y) {
        super(x, y, SIZE_X, SIZE_Y, COLOR, DEAD_COLOR, new Eye(EYE_SIZE_X, EYE_SIZE_Y, EYE_COLOR));
        random = new Random();
        addMovementTimer();
    }

    public static int getBasicEnemySpeed() {
        return basicEnemySpeed;
    }

    public static void setBasicEnemySpeed(int speed) {
        basicEnemySpeed = speed;
    }

    private void addMovementTimer() {
        movementTimer = new Timer(MOVEMENT_TIMER_DELAY, new ActionListener() { // the direction booleans are randomized every few seconds
            @Override
            public void actionPerformed(ActionEvent e) {
                dirX = random.nextBoolean();
                dirY = random.nextBoolean();
            }
        });
        movementTimer.start();
    }

    @Override
    public void move() { // always moves in a random direction
        x += dirX ? -basicEnemySpeed : basicEnemySpeed;
        y += dirY ? -basicEnemySpeed : basicEnemySpeed;
        checkBoundary();
    }
}
