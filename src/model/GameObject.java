package model;

import java.awt.*;

// This abstract class dictates the general attributes of all gameplay objects, Moveable or not
// such as by enabling the creation of a unified method that checks object location overlap (GameModel.coordinatesOverlap(MoveableObject m1, MoveableObject m2))
public abstract class GameObject {

    protected int x;
    protected int y;
    protected int sizeX;
    protected int sizeY;
    protected Color color;

    public GameObject(int x, int y, int sizeX, int sizeY, Color color) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean coordinatesOverlap(GameObject g) {
        return (xCoordinatesOverlap(g) && yCoordinatesOverlap(g));
    }

    private boolean xCoordinatesOverlap(GameObject g) {
        return getX() <= g.getX() + g.getSizeX() && getX() + getSizeX() >= g.getX();
    }

    private boolean yCoordinatesOverlap(GameObject g) {
        return getY() <= g.getY() + g.getSizeY() && getY() + getSizeY() >= g.getY();
    }


}
