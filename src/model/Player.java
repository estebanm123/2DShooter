package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player extends Moveable {

    public static final int FIRE_DELAY = 500;
    public static final int SPEED = 5; // is used to change both dx and dy
    public static final int SIZE_X = 30;
    public static final int SIZE_Y = 30;
    public static final int GUN_SIZE_X = SIZE_X / 3;
    public static final int GUN_SIZE_Y = SIZE_Y / 3;
    public static final Color COLOR = new Color(13, 41, 2);
    public static final Color HIT_COLOR = new Color(250, 100, 100);

    private int dx;
    private int dy;
    private boolean isHit;
    private boolean fireDirection;
    private List<Projectile> projectiles;


    public Player(int x, int y) {
        super(x, y);
        sizeX = SIZE_X;
        sizeY = SIZE_Y;
        dx = 0;
        dy = 0;
        projectiles = new ArrayList<>();
        isHit = false;
        fireDirection = Projectile.RIGHT; // initially fires to the right
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
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
}
