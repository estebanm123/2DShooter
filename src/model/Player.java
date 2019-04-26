package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player extends MoveableObject {

    public static final int INITIAL_FIRE_DELAY = 800;
    public static final int INITIAL_MOVEMENT_SPEED = 4;
    public static final int FIRE_DELAY_MIN = 200;
    public static final int MOVEMENT_SPEED_MAX = 10;
    public static final int HEALTH_MAX = 3;
    public static final int HEALTH_DECREMENT_AMOUNT = 1;


    public static final int SIZE_X = 30;
    public static final int SIZE_Y = 30;
    public static final int GUN_SIZE_X = SIZE_X / 3;
    public static final int GUN_SIZE_Y = SIZE_Y / 3;
    public static final Color COLOR = new Color(13, 41, 2);
    public static final Color HIT_COLOR = new Color(250, 100, 100);

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

    // Decreases health by HEALTH_DECREMENT_AMOUNT. If player is player health is 0,
    // player changes status to isDead, else changes status to isHit
    public void getHit() {
        isHit = true;
        health -= HEALTH_DECREMENT_AMOUNT;
        // deal with dead status
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

    public void fireProjectile() {
        projectiles.add(new Projectile(x, y + SIZE_Y / 2, fireDirection)); // fires missile from middle of player
    }

    @Override
    public void move() {
        x += dx;
        y += dy;
        checkBoundary();
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
}