package model.items;

import java.awt.*;

public class FiringSpeedBoost extends PowerUpItem {

    public static final Color COLOR = new Color(250, 244, 91, 180);
    public static final int STATBOOST = 100;
    public static final String TYPE = "FiringSpeed";

    public FiringSpeedBoost(int x, int y) {
        super(x, y, STATBOOST, TYPE, COLOR);
    }
}
