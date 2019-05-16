package model.items;

import model.GameObject;

import java.awt.*;

public abstract class PowerUpItem extends GameObject {

    public static final int SIZE_X = 10;
    public static final int SIZE_Y = 20;
    public static final int NUM_OF_TYPES = 3;

    protected String type;
    protected int statBoost;

    public PowerUpItem(int x, int y, int statBoost, String type, Color color) {
        super(x, y, SIZE_X, SIZE_Y, color);
        this.statBoost = statBoost;
        this.type = type;
    }

    public int getStatBoost() {
        return statBoost;
    }

    public String getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }
}
