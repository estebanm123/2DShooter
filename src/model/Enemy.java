package model;

import model.items.FiringSpeedBoost;
import model.items.HealthBoost;
import model.items.PowerUpItem;
import model.items.SpeedBoost;
import model.items.exceptions.InvalidNumberOfPowerUpItemTypes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy extends MoveableObject {

    public static final int DROP_ITEM_CHANCE_MAX = 100;
    public static final int DROP_ITEM_CHANCE = 30;
    public static final int NORMAL_SPEED = 5;
    public static final int EASY_SPEED = 3;
    public static final int MOVEMENT_TIMER_DELAY = 500;

    public static final int SIZE_X = 50;
    public static final int SIZE_Y = 40;
    public static final Color COLOR = new Color(198, 156, 158);
    public static final Color DEAD_COLOR = new Color(122, 24, 23);
    public static final int EYE_SIZE_X = 7;
    public static final int EYE_SIZE_Y = 7;
    public static final Color EYE_COLOR = new Color(250, 100, 100);

    private static int speed;
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

    public static void setEnemySpeed(int s) {
        speed = s;
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

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public void setProjectiles(List<Projectile> projectiles) {
        this.projectiles = projectiles;
    }

    public void fireProjectile() {
        projectiles.add(new Projectile(x, y + SIZE_Y / 2, Projectile.RIGHT));
    }

    //REQUIRES setSpeed to be called, so the speed of all enemies have been set (this is for setting the game's difficulty)
    @Override
    public void move() { // always moves in a random direction
        if (!isDead) {
            x += dirX ? -speed : speed;
            y += dirY ? -speed : speed;
            checkBoundary();
        }
    }

    // isDead changes to true and has a random chance of dropping (returning) a PowerUpItem. Return null if no item is dropped
    public void die(){
        isDead = true;
    }

    // randomly drops (returns) an item. Returns true if it does, false otherwise.
    public PowerUpItem dropItem() {
        int randInt = random.nextInt(DROP_ITEM_CHANCE_MAX + 1);
        if (randInt <= DROP_ITEM_CHANCE) {
            return randomPowerUp();
        } else {
            return null;
        }
    }

    // returns a random power up at the center of the enemy's (death) location
    private PowerUpItem randomPowerUp() {
        int type = random.nextInt(PowerUpItem.NUM_OF_TYPES);
        switch (type) {
            case(0): return new HealthBoost(x + (sizeX / 2), y - (sizeY / 2));
            case(1): return new SpeedBoost(x + (sizeX / 2), y - (sizeY / 2));
            case(2): return new FiringSpeedBoost(x + (sizeX / 2), y - (sizeY / 2));
        }
        throw new InvalidNumberOfPowerUpItemTypes();
    }


}
