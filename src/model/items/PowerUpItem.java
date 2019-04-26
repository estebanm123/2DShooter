package model.items;

import model.GameObject;

import java.awt.*;

public abstract class PowerUpItem extends GameObject {

    public static final int SIZE_X = 5;
    public static final int SIZE_Y = 7;

    private String type;
    private int statBoost;

    public PowerUpItem(int x, int y, int statBoost, String type) {
        super(x, y, SIZE_X, SIZE_Y);
        this.statBoost = statBoost;
        this.type = type;
    }

    public int getStatBoost() {
        return statBoost;
    }

    public String getType() {
        return type;
    }

}
