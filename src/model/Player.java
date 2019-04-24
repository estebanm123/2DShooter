package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player extends Moveable {

    public static final int FIRE_DELAY = 500;
    public static final int SPEED = 7; // is used to change both dx and dy
    public static final int SIZE_X = 30;
    public static final int SIZE_Y = 30;
    public static final Color COLOR = new Color(13, 41, 2);

    private int dx;
    private int dy;
    private boolean isHit;
    private List<Projectile> projectiles;


    public Player(int x, int y) {
        super(x, y);
        sizeX = SIZE_X;
        sizeY = SIZE_Y;
        dx = 0;
        dy = 0;
        projectiles = new ArrayList<>();
        isHit = false;

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

    public void fireProjectile() {
        projectiles.add(new Projectile(x, y + SIZE_Y / 2, Projectile.RIGHT)); // fires missile in middle of player
    }

    @Override
    public void move() {
        x += dx;
        y += dy;
        checkBoundary();
    }

}
