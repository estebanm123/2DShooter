package ui.gameplay;

import model.GameModel;
import model.Player;
import model.items.FiringSpeedBoost;
import model.items.HealthBoost;
import model.items.SpeedBoost;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ScorePanel extends JPanel implements PropertyChangeListener {

    public static final String SPEED_CIRCLE_IMAGE = "/images/SpeedCircle.png";
    public static final String HEART_IMAGE = "/images/HeartContainer.png";
    public static final String FIRING_SPEED_IMAGE = "/images/FiringSpeedCircle.png";
    public static final int WIDTH = GameModel.WIDTH;
    public static final int HEIGHT = GameModel.HEIGHT / 10;
    public static final int LABEL_WIDTH = GameModel.WIDTH / 6;
    public static final int LABEL_HEIGHT = HEIGHT / 3;
    public static final String KILLS_DESCRIPTION = "KILLS   ";


    public static final Color COLOR = Color.WHITE;

    private JLabel healthLabel;
    private JLabel killsLabel;
    private JLabel movementSpeedLabel;
    private JLabel firingSpeedLabel;

    public ScorePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(COLOR);
        initIndicators();
    }

    private void initIndicators() {
        setLayout(new FlowLayout());
        initKillsLabel();
        initHealthLabel();
        initFiringSpeedLabel();
        initSpeedLabel();
    }

    private void initKillsLabel(){
        killsLabel = new JLabel(KILLS_DESCRIPTION + 0);
        killsLabel.setBackground(Color.WHITE);
        killsLabel.setOpaque(true);
        killsLabel.setPreferredSize(new Dimension(LABEL_WIDTH / 2, LABEL_HEIGHT));
        killsLabel.setFont(new Font("Impact", Font.PLAIN, 12));
        add(killsLabel);
    }

    private void initSpeedLabel() {
        movementSpeedLabel = new JLabel();
        createDynamicLabel(movementSpeedLabel, "MOVEMENT");
        displaySpeedCircles(Player.MOVEMENT_SPEED_INITIAL);
    }

    private void initFiringSpeedLabel() {
        firingSpeedLabel = new JLabel();
        createDynamicLabel(firingSpeedLabel, "FIRING");
        displayFiringSpeedCircles(Player.FIRE_DELAY_INITIAL);
    }

    private void initHealthLabel() {
        healthLabel = new JLabel();
        createDynamicLabel(healthLabel, "");
        displayHearts(Player.HEALTH_MAX);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(GameModel.KILL)) {
            killsLabel.setText(KILLS_DESCRIPTION + evt.getNewValue());
        } else if (evt.getPropertyName().equals(GameModel.PLAYER_HIT)) {
            displayHearts((Integer) evt.getNewValue());
        } else if (evt.getPropertyName().equals(HealthBoost.TYPE)){
            displayHearts((Integer) evt.getNewValue());
        } else if (evt.getPropertyName().equals(FiringSpeedBoost.TYPE)) {
            displayFiringSpeedCircles((Integer) evt.getNewValue());
        } else if (evt.getPropertyName().equals(SpeedBoost.TYPE)) {
            displaySpeedCircles((Integer) evt.getNewValue());
        }
        repaint();
    }

    // calculates the number of circles to display based on the player's firing speed stat
    private void displaySpeedCircles(int n) {
        int count = 0;
        if (n <= Player.MOVEMENT_SPEED_MAX) {
            for (int i = Player.MOVEMENT_SPEED_INITIAL; i <= n; i++) {
               count += 1;
            }
        }
        displayLabelImages(count, SPEED_CIRCLE_IMAGE, movementSpeedLabel);

    }

    // calculates the number of circles to display based on the player's firing speed stat
    private void displayFiringSpeedCircles(int n) {
        int count = 0;
        if (n >= Player.FIRE_DELAY_MIN) {
            for (int i = Player.FIRE_DELAY_INITIAL; i >= n; i = i - FiringSpeedBoost.STATBOOST) {
                count++;
            }
        }
        displayLabelImages(count, FIRING_SPEED_IMAGE, firingSpeedLabel);
    }

    private void displayHearts(int numImages) {
        displayLabelImages(numImages, HEART_IMAGE, healthLabel);
    }

    private void displayLabelImages(int numImages, String image, JLabel label) {
        label.removeAll();
        for (int i = 1; i <= numImages; i++) {
            JLabel imageLabel = new JLabel(new ImageIcon(this.getClass().getResource(image)));
            imageLabel.setOpaque(true);
            label.add(imageLabel);
        }
        label.validate();
    }

    private void createDynamicLabel(JLabel label, String text) {
        label.setBackground(Color.WHITE);
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
        label.setLayout(new FlowLayout());
        if (!text.equals("")) {
            JLabel textLabel = new JLabel(text);
            textLabel.setBackground(Color.WHITE);
            textLabel.setOpaque(true);
            textLabel.setFont(new Font("Impact", Font.PLAIN, 12));
            add(textLabel);
        }
        add(label);
    }
}
