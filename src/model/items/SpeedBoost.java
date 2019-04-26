package model.items;

import java.awt.*;

public class SpeedBoost extends PowerUpItem {

    public static final Color COLOR = new Color(200, 250, 200);
    public static final int STAT_BOOST = 1;
    public static final String TYPE = "MovementSpeed";

    public SpeedBoost(int x, int y) {
        super(x, y, STAT_BOOST, TYPE, COLOR);
    }
}
