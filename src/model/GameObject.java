package model;

// This abstract class dictates the general attributes of all gameplay objects, Moveable or not
// such as by enabling the creation of a unified method that checks object location overlap (GameModel.coordinatesOverlap(MoveableObject m1, MoveableObject m2))
public abstract class GameObject {

    protected int x;
    protected int y;
    protected int sizeX;
    protected int sizeY;

    public GameObject(int x, int y, int sizeX, int sizeY) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
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
}
