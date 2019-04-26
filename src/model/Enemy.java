package model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy extends MoveableObject {

    public static final int FIRING_DELAY = 1000;
    public static final int SPEED = 5;
    public static final int SIZE_X = 50;
    public static final int SIZE_Y = 40;
    public static final Color COLOR = new Color(250, 250, 250);
    public static final Color DEAD_COLOR = new Color(206, 28, 55);
    public static final int MOVEMENT_TIMER_DELAY = 400;

    private boolean isDead;
    private List<Projectile> projectiles;
    private Timer movementTimer;
    private Random random;
    private boolean dirX; // these booleans determine which direction the enemy is moving in on each axis
    private boolean dirY;

    public Enemy(int x, int y) {
        super(x, y, SIZE_X, SIZE_Y);
        isDead = false;
        projectiles = new ArrayList<>();
        random = new Random();
        addMovementTimer();
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

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public void setProjectiles(List<Projectile> projectiles) {
        this.projectiles = projectiles;
    }

    public void fireProjectile() {
        projectiles.add(new Projectile(x, y + SIZE_Y / 2, Projectile.RIGHT));
    }

    @Override
    public void move() { // always moves in a random direction
        if (!isDead) {
            x += dirX ? -SPEED : SPEED;
            y += dirY ? -SPEED : SPEED;
            checkBoundary();
        }
    }
}
