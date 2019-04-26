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

    public static final int INITIAL_FIRE_DELAY = 800;
    public static final int INITIAL_MOVEMENT_SPEED = 4;
    public static final int FIRE_DELAY_MIN = 200;
    public static final int MOVEMENT_SPEED_MAX = 10;
    public static final int HEALTH_MAX = 3;
    public static final int HEALTH_MIN = 0;
    public static final int HEALTH_DECREMENT_AMOUNT = 1;

    public static final int SIZE_X = 30;
    public static final int SIZE_Y = 30;
    public static final int GUN_SIZE_X = SIZE_X / 3;
    public static final int GUN_SIZE_Y = SIZE_Y / 3;

    public static final Color COLOR = new Color(13, 41, 2);
    public static final Color HIT_COLOR = new Color(250, 100, 100);
    public static final int HIT_STATUS_DELAY = 300;

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
        firingSpeedDelay = INITIAL_FIRE_DELAY;
        health = HEALTH_MAX;
        movementSpeed = INITIAL_MOVEMENT_SPEED;
        dx = 0;
        dy = 0;
    }

    public boolean isHit() {
        return isHit;
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
    // adjust stats and return true, otherwise return false
    // throws new exceptions if power up type is not matched
    public boolean takePowerUp(PowerUpItem pui) {
        String itemType = pui.getType();
        int boost = pui.getStatBoost();
        switch(itemType) {
            case (HealthBoost.TYPE):
                if (health + boost > HEALTH_MAX) return false;
                health += boost;
                return true;
            case (FiringSpeedBoost.TYPE):
                if (firingSpeedDelay - boost < 200) return false;
                firingSpeedDelay -= pui.getStatBoost();
                return true;
            case (SpeedBoost.TYPE):
                if (movementSpeed + boost > 10) return false;
                movementSpeed += pui.getStatBoost();
                return true;
        }
        throw new UnrecognizedPowerUpTypeException();

    }


}