package model.enemies;

import java.awt.*;

// class that represents enemy eyes with width, height and color
public class Eye {

    private int sizeX;
    private int sizeY;
    private Color color;

    public Eye(int sizeX, int sizeY, Color color) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.color = color;
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
