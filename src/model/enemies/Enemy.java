package model.enemies;

import model.MoveableObject;
import model.items.FiringSpeedBoost;
import model.items.HealthBoost;
import model.items.PowerUpItem;
import model.items.SpeedBoost;
import model.items.exceptions.InvalidNumberOfPowerUpItemTypesException;

import java.awt.*;
import java.util.Random;

public abstract class Enemy extends MoveableObject {

    public static final int DROP_ITEM_CHANCE_MAX = 100;
    public static final int DROP_ITEM_CHANCE = 30;

    protected Random random;
    protected boolean isDead;
    protected Color deadColor;
    protected Eye eye;

    public Enemy(int x, int y, int sizex, int sizey, Color bodyColor, Color deadColor, Eye eye) {
        super(x, y, sizex, sizey, bodyColor);
        random = new Random();
        isDead = false;
        this.deadColor = deadColor;
        this.eye = eye;

    }

    public boolean isDead() {
        return isDead;
    }

    // isDead changes to true and has a random chance of dropping (returning) a PowerUpItem. Return null if no item is dropped
    public void die(){
        isDead = true;
    }

    // randomly drops (returns) an item; returns null if nothing dropped
    public PowerUpItem dropItem() {
        int randInt = random.nextInt(DROP_ITEM_CHANCE_MAX + 1);
        if (randInt <= DROP_ITEM_CHANCE) {
            return randomPowerUp();
        } else {
            return null;
        }
    }

    // returns a random power up at the center of the enemy's (death) location
    // throws InvalidNumberOfPowerUpItemTypesException if number of power up item types is invalid
    private PowerUpItem randomPowerUp() {
        int type = random.nextInt(PowerUpItem.NUM_OF_TYPES);
        switch (type) {
            case(0): return new HealthBoost(x + (sizeX / 2), y - (sizeY / 2));
            case(1): return new SpeedBoost(x + (sizeX / 2), y - (sizeY / 2));
            case(2): return new FiringSpeedBoost(x + (sizeX / 2), y - (sizeY / 2));
        }
        throw new InvalidNumberOfPowerUpItemTypesException();
    }

    public Color getDeadColor() {
        return deadColor;
    }

    public void setDeadColor(Color deadColor) {
        this.deadColor = deadColor;
    }

    public Eye getEye() {
        return eye;
    }

    public void setEye(Eye eye) {
        this.eye = eye;
    }

}
