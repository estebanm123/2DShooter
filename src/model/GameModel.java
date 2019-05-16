package model;


import model.enemies.AdvancedEnemy;
import model.enemies.BasicEnemy;
import model.enemies.Enemy;
import model.items.FiringSpeedBoost;
import model.items.HealthBoost;
import model.items.PowerUpItem;
import model.items.SpeedBoost;
import ui.HomePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameModel {

    // these constants are PropertyChangeEvent names
    public static final String KILL = "kill";
    public static final String PLAYER_HIT = "player hit";
    public static final String GAME_OVER = "game over";

    public static final int WIDTH = 700;
    public static final int HEIGHT = 700;
    public static final int INITIAL_PLAYER_X = 250;
    public static final int INITIAL_PLAYER_Y = 250;
    public static final int MAX_ENEMIES_EASY = 3;
    public static final int MAX_ENEMIES_NORMAL = 4;
    public static final int DEAD_ENEMY_DELAY = 500;
    public static final int NEW_ENEMY_DELAY = DEAD_ENEMY_DELAY + 3000;
    public static final int RELEASE_AE_KILL_COUNT_MIN = 5;
    public static final int MAX_CHANCE_AE_SPAWN = 35; // max chance for an advanced enemy to spawn is 75%
    public static final int INITIAL_CHANCE_AE_SPAWN = 85; // initial chance for an advanced enemy to spawn is 15%

    private Player player;
    private int killCount;
    private int difficulty;
    private List<AdvancedEnemy> movedAdvancedEnemies; // this and the cancelledMovedAdvancedEnemies list are for advanced enemy collision detection (see handleAdvancedEnemyMovement(AdvancedEnemy e) method)
    private List<AdvancedEnemy> cancelledMovedAdvancedEnemies;
    private List<Enemy> enemies;
    private List<Enemy> deadEnemies;
    private List<PowerUpItem> powerUpItems;
    private int maxEnemies;
    private Random random;
    private PropertyChangeSupport support; // used to update scorePanel and other observers

    public GameModel(int difficulty) {
        player = new Player(INITIAL_PLAYER_X, INITIAL_PLAYER_Y);
        enemies = new ArrayList<>();
        deadEnemies = new ArrayList<>();
        powerUpItems = new ArrayList<>();
        movedAdvancedEnemies = new ArrayList<>();
        cancelledMovedAdvancedEnemies = new ArrayList<>();
        maxEnemies = MAX_ENEMIES_NORMAL;
        random = new Random();
        generateEnemies();
        support = new PropertyChangeSupport(this);
        killCount = 0;
        initDifficulty(difficulty);
    }

    public boolean isGameOver() {
        return player.isDead();
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void addObserver(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public Player getPlayer() {
        return player;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Enemy> getDeadEnemies() {return deadEnemies;}

    public List<PowerUpItem> getPowerUpItems() {
        return powerUpItems;
    }

    public void update() {
        handleEnemies();
        handlePlayer();
    }

    private void handlePlayer() {
        if (!player.isDead()) {
            player.move();
            handlePlayerProjectiles();
            checkPlayerCollisions();
        }
    }

    private void checkPlayerCollisions() {
        checkItemPlayerCollision();
    }

    // moves all player projectiles and removes the ones that are out of bounds
    private void handlePlayerProjectiles() {
        List<Projectile> playerProjectilesCopy = new ArrayList<>(player.getProjectiles());
        for (Projectile p : playerProjectilesCopy) {
            p.move();
            if (p.getX() > GameModel.WIDTH || p.getY() < 0) { // a projectile can only move on the x axis
                player.getProjectiles().remove(p);
            }
        }
    }

    private void handleEnemies() {
        List<Enemy> enemiesCopy = new ArrayList<>(enemies);
        List<Projectile> playerProjectilesCopy = new ArrayList<>(player.getProjectiles());
        for (Enemy e : enemiesCopy) {
            handleEnemyMovement(e);
            checkEnemyCollisions(e, playerProjectilesCopy);
            handleDeadEnemies(e);
        }
        movedAdvancedEnemies.clear();
        cancelledMovedAdvancedEnemies.clear();
    }

    private void handleEnemyMovement(Enemy e) {
        e.move();
        if (AdvancedEnemy.class.equals(e.getClass())) {
            AdvancedEnemy cur = (AdvancedEnemy) e;
            handleAdvancedEnemyMovement(cur);
        }
    }

    // 2 factors affect advanced enemy movement in case of collision :
    //      if one of the pair's moves has been cancelled
    //      and which of the two is closer to player
    private void handleAdvancedEnemyMovement(AdvancedEnemy cur) {
        movedAdvancedEnemies.add(cur);
        AdvancedEnemy other = enemyOverlapCheck(cur);
        if (other != null && movedAdvancedEnemies.contains(other)) {
            if (!cancelledMovedAdvancedEnemies.contains(other) || !cancelledMovedAdvancedEnemies.contains(cur)) {
                moveCloserAdvancedEnemy(cur, other);
            }
        }
    }

    private void checkEnemyCollisions(Enemy e, List<Projectile> playerProjectilesCopy) {
        checkEnemyHit(e, playerProjectilesCopy);
        checkEnemyPlayerCollision(e);
    }

    private void checkEnemyHit(Enemy e, List<Projectile> playerProjectilesCopy) {
        for (Projectile p : playerProjectilesCopy) { // check if each player projectile has hit an enemy
            if (e.coordinatesOverlap(p)) {
                support.firePropertyChange(KILL, killCount, ++killCount);
                e.die();
                player.getProjectiles().remove(p);
            }
        }

    }

    private void checkEnemyPlayerCollision(Enemy e) {
        if (e.coordinatesOverlap(player) && !player.isHit()) {
            int prevHealth = player.getHealth();
            player.getHit();
            if (player.isDead()) support.firePropertyChange(GAME_OVER, null, null);
            else support.firePropertyChange(PLAYER_HIT, prevHealth, player.getHealth());
        }
    }

    private void handleDeadEnemies(Enemy e) {
        if (e.isDead() && !deadEnemies.contains(e)) {
            removeEnemy(e);
            generateNewEnemy();
        }

    }

    // generates a new enemy with delay
    private void generateNewEnemy() {
        Timer generateEnemyTimer = new Timer(NEW_ENEMY_DELAY, null);
        generateEnemyTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (enemies.size() < maxEnemies) addEnemy();
                generateEnemyTimer.stop();
            }
        });
        generateEnemyTimer.setInitialDelay(NEW_ENEMY_DELAY);
        generateEnemyTimer.setRepeats(false);
        generateEnemyTimer.start();
    }

    private void addEnemy() {
        Enemy enemy = randomEnemy();
        while (!coordinatesNotOccupied(enemy)) { // loops until unused random spawn location is found for enemy
            enemy = randomEnemy();
        }
        enemies.add(enemy);
    }

    // used during initialization
    private void generateEnemies() {
        while (enemies.size() < maxEnemies) {
            addEnemy();
        }
    }

    // adds a timer to delay enemy removal by DEAD_ENEMY_DELAY ms
    private void removeEnemy(Enemy enemy) {
        deadEnemies.add(enemy); // enemy added to a separate list to avoid constantly looping over dead enemies
        enemies.remove(enemy);
        Timer disappearTimer = new Timer(DEAD_ENEMY_DELAY, null);
        disappearTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // timer is used to delay enemy removal
                PowerUpItem pui = enemy.dropItem();
                if (pui != null) powerUpItems.add(pui);
                deadEnemies.remove(enemy);
                disappearTimer.stop();
            }
        });
        disappearTimer.setInitialDelay(DEAD_ENEMY_DELAY);
        disappearTimer.setRepeats(false);
        disappearTimer.start();
    }

    private Enemy randomEnemy() {
        int x = random.nextInt(GameModel.WIDTH - (GameModel.WIDTH / 2)) + GameModel.WIDTH / 2;  // spawns enemies in right side of screen; assume player spawns in left side
        int y = random.nextInt(GameModel.HEIGHT);
        return selectEnemy(x, y);
    }

    // returns an advanced enemy if kill count is >= RELEASE_ADVANCED_ENEMIES
    private Enemy selectEnemy(int x, int y) {
        Enemy e = new BasicEnemy(x, y);
        if (killCount >= RELEASE_AE_KILL_COUNT_MIN) {
            Enemy a = generateAdvancedEnemy(x, y);
            if (a != null) return a;
        }
        return e;
    }

    // dynamically returns an advanced enemy based on current kill count at x and y points
    // Chance to spawn an advanced enemy increases with kill count until it reaches a limit of MAX_CHANCE_AE_SPAWN
    // returns null if chance to spawn advanced enemy was too low
    private Enemy generateAdvancedEnemy(int x, int y) {
        int n = random.nextInt(100);
        int lim = calculateAdvancedEnemySpawnMaxChance();
        AdvancedEnemy a = null;
        if (n >= lim) {
            a = new AdvancedEnemy(x, y, player);
        }
        return a;
    }

    private int calculateAdvancedEnemySpawnMaxChance() {
        int lim = INITIAL_CHANCE_AE_SPAWN - killCount + RELEASE_AE_KILL_COUNT_MIN;
        if (lim < MAX_CHANCE_AE_SPAWN) return MAX_CHANCE_AE_SPAWN;
        return lim;
    }

    // returns true if the object's coordinates are not used by a player, any missiles, and any enemies
    private boolean coordinatesNotOccupied(GameObject g) {
        //checks overlap with player
        if (player.coordinatesOverlap(g)) return false;

        //check each projectiles' overlap
        for (Projectile p : player.getProjectiles()) {
            if (player.coordinatesOverlap(g)) return false;
        }
        return true;
    }

    // returns an advancedEnemy the e1 is overlapping
    private AdvancedEnemy enemyOverlapCheck(AdvancedEnemy e1) {
        for (AdvancedEnemy e : movedAdvancedEnemies) { // switch with advanced enemy list
            if (!e.equals(e1) && e.coordinatesOverlap(e1)) {
                return e;
            }
        }
        return null;
    }

    // calculates which advanced enemies is closer and cancels the other's move depending on if its been already cancelled
    private void moveCloserAdvancedEnemy(AdvancedEnemy e1, AdvancedEnemy e2) {
        int difM1x = Math.abs(e1.getX() - player.getX());
        int difM2x = Math.abs(e2.getX() - player.getX());
        int difM1y = Math.abs(e1.getY() - player.getY());
        int difM2y = Math.abs(e2.getY() - player.getY());
        int totalDif1 = difM1x + difM1y;
        int totalDif2 = difM2x + difM2y;
        if (totalDif1 < totalDif2 && !cancelledMovedAdvancedEnemies.contains(e2)) { // e1 is closer
            e2.cancelMove();
            cancelledMovedAdvancedEnemies.add(e2);
        } else if (!cancelledMovedAdvancedEnemies.contains(e1)) {
            e1.cancelMove();
            cancelledMovedAdvancedEnemies.add(e1);
        }
    }

    private void checkItemPlayerCollision() {
        List<PowerUpItem> powerUpItemsCopy = new ArrayList<>(powerUpItems);
        for (PowerUpItem pui : powerUpItemsCopy) {
            if (player.coordinatesOverlap(pui)) {
                boolean pickUp = player.takePowerUp(pui);
                if (pickUp) {
                    powerUpItems.remove(pui);
                    notifyObserversPowerUpPickedUp(pui);
                }
            }
        }
    }

    private void notifyObserversPowerUpPickedUp(PowerUpItem pui) {
        if (pui.getType().equals(HealthBoost.TYPE)) {
            support.firePropertyChange(HealthBoost.TYPE, null, player.getHealth());
        } else if (pui.getType().equals(FiringSpeedBoost.TYPE)) {
            support.firePropertyChange(FiringSpeedBoost.TYPE, null, player.getFiringSpeedDelay());
        } else if (pui.getType().equals(SpeedBoost.TYPE)) {
            support.firePropertyChange(SpeedBoost.TYPE, null, player.getMovementSpeed());
        }
    }

    private void initDifficulty(int difficulty) {
        this.difficulty = difficulty;
        if (difficulty == HomePanel.EASY) {
            BasicEnemy.setBasicEnemySpeed(BasicEnemy.EASY_SPEED);
            AdvancedEnemy.setAdvancedEnemySpeed(AdvancedEnemy.EASY_SPEED);
            maxEnemies = MAX_ENEMIES_EASY;
        } else {
            BasicEnemy.setBasicEnemySpeed(BasicEnemy.NORMAL_SPEED);
            AdvancedEnemy.setAdvancedEnemySpeed(AdvancedEnemy.NORMAL_SPEED);
            maxEnemies = MAX_ENEMIES_NORMAL;
        }
    }
}





