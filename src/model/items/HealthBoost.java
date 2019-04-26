package model.items;

import java.awt.*;

public class HealthBoost extends PowerUpItem {

    public static final Color COLOR = new Color(250, 56, 53, 195);
    public static final int STAT_BOOST = 1;
    public static final String TYPE = "Health";

    public HealthBoost(int x, int y) {
        super(x, y, STAT_BOOST, TYPE);
    }


}
