package model;

import model.items.FiringSpeedBoost;
import model.items.HealthBoost;
import model.items.PowerUpItem;
import model.items.SpeedBoost;
import model.items.exceptions.UnrecognizedPowerUpTypeException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Player extends MoveableObject {

    public static final int FIRE_DELAY_INITIAL = 800;
    public static final int MOVEMENT_SPEED_INITIAL = 4;
    public static final int FIRE_DELAY_MIN = 200;
    public static final int MOVEMENT_SPEED_MAX = 10;
    public static final int HEALTH_MAX = 3;
    public static final int HEALTH_MIN = 0;
    public static final int HEALTH_DECREMENT_AMOUNT = 1;

    public static final int SIZE_X = 30;
    public static final int SIZE_Y = 40;
    public static final int GUN_SIZE_X = SIZE_X / 4;
    public static final int GUN_SIZE_Y = SIZE_Y / 5;
    public static final int BOTTOM_HEIGHT = SIZE_Y;
    public static final int BOTTOM_WIDTH = SIZE_X;

    public static final Color COLOR = new Color(217, 206, 210);
    public static final Color HIT_COLOR = new Color(250, 100, 100);
    public static final int HIT_STATUS_DELAY = 900;

    private int dx;
    private int dy;
    private boolean isHit;
    private int health;
    private int firingSpeedDelay;
    private int movementSpeed;
    private boolean fireDirection;
    private List<Projectile> projectiles;

    public Player(int x, int y) {
        super(x, y, SIZE_X, SIZE_Y);
        projectiles = new ArrayList<>();
        isHit = false;
        fireDirection = Projectile.RIGHT; // initially fires to the right
        firingSpeedDelay = FIRE_DELAY_INITIAL;
        health = HEALTH_MAX;
        movementSpeed = MOVEMENT_SPEED_INITIAL;
        dx = 0;
        dy = 0;
    }

    public boolean isHit() {
        return isHit;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public int getFiringSpeedDelay() {
        return firingSpeedDelay;
    }

    public void setFiringSpeedDelay(int firingSpeedDelay) {
        this.firingSpeedDelay = firingSpeedDelay;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public boolean getFireDirection() {
        return fireDirection;
    }

    public void setFireDirection(boolean fireDirection) {
        this.fireDirection = fireDirection;
    }

    public void switchFireDirection() {
        this.fireDirection = !fireDirection;
    }

    // fires a projectile if player is not hit
    public void fireProjectile() {
        if (!isHit) {
            projectiles.add(new Projectile(x, y + SIZE_Y / 2, fireDirection)); // fires missile from middle of player
        }
    }

    @Override
    public void move() {
        x += dx;
        y += dy;
        checkBoundary();
    }

    // Decreases health by HEALTH_DECREMENT_AMOUNT. If player is player health is 0,
    // player changes status to isDead, else changes status to isHit
    public void getHit() {
        isHit = true;
        health -= HEALTH_DECREMENT_AMOUNT;
        if (health > HEALTH_MIN) {
            addHitTimer();
        }
        // deal with dead status
    }

    private void addHitTimer() {
        Timer hitTimer = new Timer(HIT_STATUS_DELAY, null);
        hitTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isHit = false;
                hitTimer.stop();
            }
        });
        hitTimer.setRepeats(false);
        hitTimer.setInitialDelay(HIT_STATUS_DELAY);
        hitTimer.start();
    }

    // Simulates player picking up power up item. If stat corresponding to pick up is within limit,
    // adjust stats and returns true, otherwise return false
    // throws new UnrecognizedPowerUpTypeException if power up type is not matched
    public boolean takePowerUp(PowerUpItem pui) {
        String itemType = pui.getType();
        int boost = pui.getStatBoost();
        if (isHit) return false; // also add dead case
        switch(itemType) {
            case (HealthBoost.TYPE ):
                if (health + boost > HEALTH_MAX) return false;
                health += boost;
                return true;
            case (FiringSpeedBoost.TYPE):
                if (firingSpeedDelay - boost < FIRE_DELAY_MIN) return false;
                firingSpeedDelay -= pui.getStatBoost();
                return true;
            case (SpeedBoost.TYPE):
                if (movementSpeed + boost > MOVEMENT_SPEED_MAX) return false;
                movementSpeed += pui.getStatBoost();
                return true;
        }
        throw new UnrecognizedPowerUpTypeException();

    }


}