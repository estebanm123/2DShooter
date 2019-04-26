package model;


// this abstract class was created to reduce code duplication and ease managing the different moveable objects on screen
public abstract class MoveableObject extends GameObject {

    public MoveableObject(int x, int y, int sizeX, int sizeY) {
        super(x, y, sizeX, sizeY);
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
