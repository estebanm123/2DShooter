import model.Enemy;
import model.GameModel;
import model.Player;
import model.Projectile;

import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel {

    public static final int ONE_PIXEL_OFFSET = 1;
    private GameModel gameModel;

    public GamePanel(GameModel gameModel) {
        this.gameModel = gameModel;
        setPreferredSize(new Dimension(GameModel.WIDTH, GameModel.HEIGHT));
        setBackground(Color.GRAY);
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g);
    }

    private void drawGame(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        drawPlayer(g2d);
        drawEnemies(g2d);
        drawProjectiles(g2d);
    }

    private void drawPlayer(Graphics2D g2d) {
        Player p = gameModel.getPlayer();
        if (!p.isHit()) {
            g2d.setColor(Player.COLOR);
            Color c = g2d.getColor();
        }
        g2d.fillOval(p.getX(), p.getY(), Player.SIZE_X, Player.SIZE_Y);
        drawGun(g2d, p);
    }

    //fils a rectangle at the middle edge of the player depending on direction
    private void drawGun(Graphics2D g2d, Player p) {
        if (p.getFireDirection() == Projectile.LEFT) {
            g2d.setColor(Player.COLOR);
            g2d.fillRect(p.getX() - (Player.SIZE_X / 3) + ONE_PIXEL_OFFSET, p.getY() + (Player.SIZE_Y / 2) - (Player.GUN_SIZE_Y / 2),
                    Player.GUN_SIZE_X, Player.GUN_SIZE_Y);
        } else {
            g2d.setColor(Player.COLOR);
            g2d.fillRect(p.getX() + Player.SIZE_X, p.getY() + (Player.SIZE_Y / 2) - (Player.GUN_SIZE_Y / 2),
                    Player.GUN_SIZE_X, Player.GUN_SIZE_Y);
        }
    }



    private void drawEnemies(Graphics2D g2d) {
        for (Enemy e : gameModel.getEnemies()) {
            if (e.isDead()) {
                g2d.setColor(Enemy.DEAD_COLOR);
                g2d.fillOval(e.getX(), e.getY(), Enemy.SIZE_X, Enemy.SIZE_Y);
            } else {
                g2d.setColor(Enemy.COLOR);
                g2d.fillOval(e.getX(), e.getY(), Enemy.SIZE_X, Enemy.SIZE_Y);
            }
        }
    }

    private void drawProjectiles(Graphics2D g2d) {
        for (Projectile p : gameModel.getPlayer().getProjectiles()) {
            g2d.setColor(Projectile.COLOR);
            g2d.fillOval(p.getX(), p.getY(), p.SIZE_X, p.SIZE_Y);
        }
    }

}
