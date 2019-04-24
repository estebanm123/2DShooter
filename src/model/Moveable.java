package model;


// this abstract class was created to reduce code duplication and ease managing the different moveable objects on screen
// such as by enabling the creation of a unified method that checks object location overlap (GameModel.coordinatesOverlap(Moveable m1, Moveable m2))
// update to extend more general gameObject class ??????
public abstract class Moveable {

    protected int x;
    protected int y;
    protected int sizeX;
    protected int sizeY;

    public Moveable(int x, int y) {
        this.x = x;
        this.y = y;
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

    abstract void move();

    public void checkBoundary() {
        if (x > GameModel.WIDTH - sizeX) { // checking if moving past screen boundaries
            x = GameModel.WIDTH - sizeX;
        } else if (x < 0) {
            x = 0;
        }
        if (y >= GameModel.HEIGHT - sizeY) {
            y = GameModel.HEIGHT - sizeY;
        } else if (y < 0) {
            y = 0;
        }
    }


}
