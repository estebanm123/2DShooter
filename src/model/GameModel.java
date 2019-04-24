package model;


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
    private int maxEnemies;
    private Random random; // used for generating random enemy spawn

    public GameModel() {
        player = new Player(INITIAL_PLAYER_X, INITIAL_PLAYER_Y);
        enemies = new ArrayList<>();
        deadEnemies = new ArrayList<>();
        maxEnemies = MAX_ENEMIES_INITIAL;
        random = new Random();
        generateNewEnemies();
    }

    public Player getPlayer() {
        return player;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void update() {
        moveEntities();
        checkCollisions();
        handleDeadEnemies();
        removeProjectilesBeyondBounds();
    }

    private void moveEntities() {
        player.move();
        moveProjectiles();
        moveEnemies();
    }

    private void moveEnemies() {
        for (Enemy e : enemies) {
            e.move();
        }
    }

    private void moveProjectiles() {
        for (Projectile p : player.getProjectiles()) {
            p.move();
        }
        moveEnemyProjectiles();
    }

    private void moveEnemyProjectiles() {
        for (Enemy e : enemies) {
            for (Projectile p : e.getProjectiles()) {
                p.move();
            }
        }
    }

    private Enemy randomEnemy() {
        int x = random.nextInt(GameModel.WIDTH - (GameModel.WIDTH / 2)) + GameModel.WIDTH / 2;  // enemies can only spawn in half the screen
        int y = random.nextInt(GameModel.HEIGHT);
        return new Enemy(x, y);
    }

    // returns true if the object's coordinates are not used by a player, any missiles, and any enemies
    private boolean coordinatesNotOccupied(Moveable m) {
        List<Projectile> cumulativeProjectiles = player.getProjectiles();
        //checks each enemies' position and adds their projectiles to a cumulative list
        for (Enemy e : enemies) {
            if (coordinatesOverlap(e, m)) return false;
            cumulativeProjectiles.addAll(e.getProjectiles());
        }
        //check each projectiles' overlap
        for (Projectile p : cumulativeProjectiles) {
            if (coordinatesOverlap(p, m)) return false;
        }
        return true;
    }


    //public for testing
    public boolean coordinatesOverlap(Moveable m1, Moveable m2) {
        return (xCoordinatesOverlap(m1, m2) && yCoordinatesOverlap(m1, m2));
    }

    private boolean xCoordinatesOverlap(Moveable m1, Moveable m2) {
        return m1.getX() <= m2.getX() + m2.getSizeX() && m1.getX() + m1.getSizeX() >= m2.getX();
    }

    private boolean yCoordinatesOverlap(Moveable m1, Moveable m2) {
        return m1.getY() <= m2.getY() + m2.getSizeY() && m1.getY() + m1.getSizeY() >= m2.getY();
    }

    private void removeProjectilesBeyondBounds() {
        removePlayerProjectiles();
        removeEnemyProjectiles();

    }

    private void removeEnemyProjectiles() {
        for (Enemy e : enemies) {
            List<Projectile> enemyProjectilesCopy = new ArrayList<>(e.getProjectiles());
            for (Projectile p : enemyProjectilesCopy) {
                if (p.getX() > GameModel.WIDTH || p.getY() < 0) { // a projectile can only move on the x axis
                    e.getProjectiles().remove(p);
                }
            }
        }

    }

    private void removePlayerProjectiles() {
        List<Projectile> playerProjectilesCopy = new ArrayList<>(player.getProjectiles());
        for (Projectile p : playerProjectilesCopy) {
            if (p.getX() > GameModel.WIDTH || p.getY() < 0) { // a projectile can only move on the x axis
                player.getProjectiles().remove(p);
            }
        }
    }

    private void fireEnemyProjectiles() {
    }

    private void checkCollisions() {
        checkEnemyHit();
        checkPlayerEnemyCollision();
    }

    private void checkPlayerEnemyCollision() {
        for (Enemy e : enemies) {
            if (coordinatesOverlap(player, e)) {

                player.setHit(true);
            }
        }
    }

    private void checkEnemyHit() {
        List<Projectile> playerProjectilesCopy = new ArrayList<>(player.getProjectiles());
        for (Enemy e : enemies) {
            for (Projectile p : playerProjectilesCopy) { // check if each projectile has hit an enemy
                if (coordinatesOverlap(e, p)) {
                    // UPDATE SCORE PANEL OR WHATEVER
                    e.setDead(true);
                    player.getProjectiles().remove(p);
                }
            }
        }

    }

    private void handleDeadEnemies() {
        List<Enemy> enemiesCopy = new ArrayList<>(enemies);
        for (Enemy enemy : enemiesCopy) {
            if (enemy.isDead() && !deadEnemies.contains(enemy)) {
                removeEnemy(enemy);
                generateNewEnemy();
            }
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
    private void generateNewEnemies() {
        while (enemies.size() < maxEnemies) {
            addEnemy();
        }
    }

    // adds a timer to delay enemy removal by DEAD_ENEMY_DELAY ms
    private void removeEnemy(Enemy enemy) {
        deadEnemies.add(enemy);
        Timer disappearTimer = new Timer(DEAD_ENEMY_DELAY, null);
        disappearTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // timer is used to delay enemy removal
                enemies.remove(enemy);
                disappearTimer.stop();
            }
        });
        disappearTimer.setInitialDelay(DEAD_ENEMY_DELAY);
        disappearTimer.setRepeats(false);
        disappearTimer.start();
    }
}





