package model;


import model.items.PowerUpItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameModel {

    public static final int WIDTH = 700;
    public static final int HEIGHT = 700;
    public static final int INITIAL_PLAYER_X = 250;
    public static final int INITIAL_PLAYER_Y = 250;
    public static final int MAX_ENEMIES_INITIAL = 4;
    public static final int DEAD_ENEMY_DELAY = 500;
    public static final int NEW_ENEMY_DELAY = DEAD_ENEMY_DELAY + 3000;

    private Player player;
    private List<Enemy> enemies;
    private List<Enemy> deadEnemies;
    private List<PowerUpItem> powerUpItems;
    private int maxEnemies;
    private Random random; // used for generating random enemy spawn

    public GameModel() {
        player = new Player(INITIAL_PLAYER_X, INITIAL_PLAYER_Y);
        enemies = new ArrayList<>();
        deadEnemies = new ArrayList<>();
        powerUpItems = new ArrayList<>();
        maxEnemies = MAX_ENEMIES_INITIAL;
        random = new Random();
        generateEnemies();
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
        player.move();
        handlePlayerProjectiles();
        checkPlayerCollisions();
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
                System.out.println(player.getProjectiles()); // FOR TESTING
            }
        }
    }

    private void handleEnemies() {
        List<Enemy> enemiesCopy = new ArrayList<>(enemies);
        List<Projectile> playerProjectilesCopy = new ArrayList<>(player.getProjectiles());
        for (Enemy e : enemiesCopy) {
            e.move();
            checkEnemyCollisions(e, playerProjectilesCopy);
            handleDeadEnemies(e);
        }
    }

    private void checkEnemyCollisions(Enemy e, List<Projectile> playerProjectilesCopy) {
        checkEnemyHit(e, playerProjectilesCopy);
        checkEnemyPlayerCollision(e);
    }

    private void checkEnemyHit(Enemy e, List<Projectile> playerProjectilesCopy) {
        for (Projectile p : playerProjectilesCopy) { // check if each player projectile has hit an enemy
            if (coordinatesOverlap(e, p)) {
                // UPDATE SCORE PANEL OR WHATEVER
                e.die();
                player.getProjectiles().remove(p);
            }
        }

    }

    private void checkEnemyPlayerCollision(Enemy e) {
        if (coordinatesOverlap(player, e) && !player.isHit()) {
            player.getHit();
            //UPDATE SCORE PANEL
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
        int x = random.nextInt(GameModel.WIDTH - (GameModel.WIDTH / 2)) + GameModel.WIDTH / 2;  // enemies can only spawn in half the screen
        int y = random.nextInt(GameModel.HEIGHT);
        return new Enemy(x, y);
    }

    // returns true if the object's coordinates are not used by a player, any missiles, and any enemies
    private boolean coordinatesNotOccupied(GameObject g) {
        //checks overlap with player
        if (coordinatesOverlap(player, g)) return false;

        List<Projectile> cumulativeProjectiles = player.getProjectiles();
        //checks each enemies' position and adds their projectiles to a cumulative list
        for (Enemy e : enemies) {
            if (coordinatesOverlap(e, g)) return false;
            cumulativeProjectiles.addAll(e.getProjectiles());
        }
        //check each projectiles' overlap
        for (Projectile p : cumulativeProjectiles) {
            if (coordinatesOverlap(p, g)) return false;
        }
        return true;
    }

    //public for testing
    public static boolean coordinatesOverlap(GameObject g1, GameObject g2) {
        return (xCoordinatesOverlap(g1, g2) && yCoordinatesOverlap(g1, g2));
    }

    private static boolean xCoordinatesOverlap(GameObject g1, GameObject g2) {
        return g1.getX() <= g2.getX() + g2.getSizeX() && g1.getX() + g1.getSizeX() >= g2.getX();
    }

    private static boolean yCoordinatesOverlap(GameObject g1, GameObject g2) {
        return g1.getY() <= g2.getY() + g2.getSizeY() && g1.getY() + g1.getSizeY() >= g2.getY();
    }

    private void checkItemPlayerCollision() {
        List<PowerUpItem> powerUpItemsCopy = new ArrayList<>(powerUpItems);
        for (PowerUpItem pui : powerUpItemsCopy) {
            if (coordinatesOverlap(player, pui)) {
                boolean pickUp = player.takePowerUp(pui);
                if (pickUp) {
                    powerUpItems.remove(pui);
                    // UPDATE STATS
                }
            }
        }
    }
}





