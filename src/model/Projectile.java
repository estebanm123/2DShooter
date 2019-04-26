package model;

import java.awt.*;

public class Projectile extends Moveable {

    public static final int INITIAL_DX = 10;
    public static final int SIZE_X = 15;
    public static final int SIZE_Y = 6;
    public static final Color COLOR = Player.COLOR;
    public static final boolean LEFT = false;
    public static final boolean RIGHT = true;

    private boolean direction;

    public Projectile(int x, int y, boolean direction) {
        super(x, y);
        sizeX = SIZE_X;
        sizeY = SIZE_Y;
        this.direction = direction;
    }

    @Override
    public void move() {
        if (direction == LEFT) {
            x -= INITIAL_DX;
        } else {
            x += INITIAL_DX;
        }
    }


}
