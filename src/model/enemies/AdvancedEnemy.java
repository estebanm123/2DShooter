package model.enemies;

import model.MoveableObject;

import java.awt.*;


// has a MoveableObject field which it
public class AdvancedEnemy extends Enemy {

    public static final int SIZE_X = 50;
    public static final int SIZE_Y = 40;
    public static final Color COLOR = new Color(141, 148, 188, 116);
    public static final Color DEAD_COLOR = new Color(115, 118, 147);
    public static final int EYE_SIZE_X = 7;
    public static final int EYE_SIZE_Y = 7;
    public static final Color EYE_COLOR = new Color(250, 100, 100);

    public static final int EASY_SPEED = 2;
    public static final int NORMAL_SPEED = 3;

    private static int advancedEnemySpeed = NORMAL_SPEED;
    private MoveableObject target;

    public AdvancedEnemy(int x, int y, MoveableObject target) {
        super(x, y, SIZE_X, SIZE_Y, COLOR, DEAD_COLOR, new Eye(EYE_SIZE_X, EYE_SIZE_Y, EYE_COLOR));
        this.target = target;
    }

    public MoveableObject getTarget() {
        return target;
    }

    public void setTarget(MoveableObject target) {
        this.target = target;
    }

    public static void setAdvancedEnemySpeed(int speed) {
        advancedEnemySpeed = speed;
    }

    public static int getAdvancedEnemySpeed() {
        return advancedEnemySpeed;
    }

    // attempts to match target's coordinates based on speed
    @Override
    public void move() {
        if (target.getX() > x) {
            x += advancedEnemySpeed;
        } else if (target.getX() < x) {
            x -= advancedEnemySpeed;
        }
        if (target.getY() > y) {
            y += advancedEnemySpeed;
        } else if (target.getY() < y) {
            y -= advancedEnemySpeed;
        }
    }

    public void cancelMove() {
        if (target.getX() > x) {
            x -= advancedEnemySpeed;
        } else if (target.getX() < x) {
            x += advancedEnemySpeed;
        }
        if (target.getY() > y) {
            y -= advancedEnemySpeed;
        } else if (target.getY() < y) {
            y += advancedEnemySpeed;
        }
    }

    public static void increaseEnemySpeed() {
        advancedEnemySpeed++;
    }
}
