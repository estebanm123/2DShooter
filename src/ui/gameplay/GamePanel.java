package ui.gameplay;

import model.Enemy;
import model.GameModel;
import model.Player;
import model.Projectile;
import model.items.PowerUpItem;

import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel {

    public static final int ONE_PIXEL_OFFSET = 1; // used when adjusting the position of player gun visual
    public static final Color COLOR = new Color(22, 35, 123);
  //  public static final Color COLOR = new Color(134, 199, 255);



    private GameModel gameModel;

    public GamePanel(GameModel gameModel) {
        this.gameModel = gameModel;
        setPreferredSize(new Dimension(GameModel.WIDTH, GameModel.HEIGHT));
        setBackground(COLOR);
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
        drawItems(g2d);
    }

    private void drawItems(Graphics2D g2d) {
        for (PowerUpItem pui : gameModel.getPowerUpItems()) {
            g2d.setColor(pui.getColor());
            g2d.fillOval(pui.getX(), pui.getY(), PowerUpItem.SIZE_X, PowerUpItem.SIZE_Y);
        }
    }

    private void drawPlayer(Graphics2D g2d) {
        Player p = gameModel.getPlayer();

        if (!p.isHit()) {
            g2d.setColor(Player.COLOR);
        } else {
            g2d.setColor(Player.HIT_COLOR);
        }

        g2d.fillRoundRect(p.getX(), p.getY(), Player.SIZE_X, Player.SIZE_Y, 10, 10);

        drawPlayerBottom(g2d, p);
        drawGun(g2d, p);
    }

    //fills a traingle at the bottom of player model
    private void drawPlayerBottom(Graphics2D g2d, Player p) {
        int lowestY = p.getY() + Player.SIZE_Y + Player.BOTTOM_HEIGHT / 3;
        int midX = p.getX() + Player.SIZE_X / 2;
        int xPoints[] = {p.getX(), midX, p.getX() + Player.BOTTOM_WIDTH};
        int yPoints[] = {lowestY, lowestY - Player.BOTTOM_HEIGHT, lowestY};
        Polygon bottom = new Polygon(xPoints, yPoints, 3);
        g2d.fillPolygon(bottom);
    }

    //fils a rectangle at the middle edge of the player depending on direction
    private void drawGun(Graphics2D g2d, Player p) {
      //  g2d.setColor(g2d.getColor());
        if (p.getFireDirection() == Projectile.LEFT) {
            g2d.fillRect(p.getX() - Player.GUN_SIZE_X + ONE_PIXEL_OFFSET, p.getY() + (Player.SIZE_Y / 2) - (Player.GUN_SIZE_Y / 2),
                    Player.GUN_SIZE_X, Player.GUN_SIZE_Y);
        } else {
            g2d.fillRect(p.getX() + Player.SIZE_X, p.getY() + (Player.SIZE_Y / 2) - (Player.GUN_SIZE_Y / 2),
                    Player.GUN_SIZE_X, Player.GUN_SIZE_Y);
        }
    }



    private void drawEnemies(Graphics2D g2d) {
        for (Enemy e : gameModel.getEnemies()) {
            g2d.setColor(Enemy.COLOR);
            g2d.fillOval(e.getX(), e.getY(), Enemy.SIZE_X, Enemy.SIZE_Y);
            g2d.setColor(Enemy.EYE_COLOR);
            //eyes
            g2d.fillOval(e.getX() + Enemy.SIZE_X / 4, e.getY() + Enemy.SIZE_Y / 4, Enemy.EYE_SIZE_X, Enemy.EYE_SIZE_Y);
            g2d.fillOval(e.getX() + Enemy.SIZE_X / 2, e.getY() + Enemy.SIZE_Y / 4, Enemy.EYE_SIZE_X, Enemy.EYE_SIZE_Y);
        }
        for (Enemy e : gameModel.getDeadEnemies()) {
            g2d.setColor(Enemy.DEAD_COLOR);
            g2d.fillOval(e.getX(), e.getY(), Enemy.SIZE_X, Enemy.SIZE_Y);
        }
    }

    private void drawProjectiles(Graphics2D g2d) {
        for (Projectile p : gameModel.getPlayer().getProjectiles()) {
            g2d.setColor(Projectile.COLOR);
            g2d.fillOval(p.getX(), p.getY(), Projectile.SIZE_X, Projectile.SIZE_Y);
        }
    }

}
