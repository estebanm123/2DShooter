package ui.gameplay;

import model.*;
import model.enemies.Enemy;
import model.items.PowerUpItem;
import ui.PausePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class GamePanel extends JPanel implements PropertyChangeListener {

    public static final String RESTART = "Restart GamePanel";
    public static final String BACK_TO_MENU = "Back to menu";
    public static final String PAUSE_GAME = "Pause";
    public static final String UN_PAUSE_GAME = "Unpause game";

    public static final int ONE_PIXEL_OFFSET = 1; // used when adjusting the position of player gun visual
    public static final Color COLOR = new Color(22, 35, 123);
  //  public static final Color COLOR = new Color(134, 199, 255);

    private GameModel gameModel;
    private GridBagConstraints c;
    private PropertyChangeSupport support;
    private PausePanel pausePanel;

    public GamePanel(GameModel gameModel) {
        setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        this.gameModel = gameModel;
        pausePanel = new PausePanel();
        add(pausePanel);
        pausePanel.setEnabled(false);
        pausePanel.setVisible(false);
        setPreferredSize(new Dimension(GameModel.WIDTH, GameModel.HEIGHT));
        setBackground(COLOR);
        support = new PropertyChangeSupport(this);
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
            g2d.setColor(e.getColor());
            g2d.fillOval(e.getX(), e.getY(), e.getSizeX(), e.getSizeY());
            //eyes
            if (e.getEye() != null) {
                g2d.setColor(e.getEye().getColor());
                g2d.fillOval(e.getX() + e.getSizeX() / 4, e.getY() + e.getSizeY() / 4, e.getEye().getSizeX(), e.getEye().getSizeY());
                g2d.fillOval(e.getX() + e.getSizeX() / 2, e.getY() + e.getSizeY() / 4, e.getEye().getSizeX(), e.getEye().getSizeY());
            }
        }
        for (Enemy e : gameModel.getDeadEnemies()) {
            g2d.setColor(e.getDeadColor());
            g2d.fillOval(e.getX(), e.getY(), e.getSizeX(), e.getSizeY());
        }
    }

    private void drawProjectiles(Graphics2D g2d) {
        for (Projectile p : gameModel.getPlayer().getProjectiles()) {
            g2d.setColor(Projectile.COLOR);
            g2d.fillOval(p.getX(), p.getY(), Projectile.SIZE_X, Projectile.SIZE_Y);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(GameModel.GAME_OVER)) {
            remove(pausePanel);
            createGameOverLabel();
            createRestartButton();
            createMenuButton();
        }
    }

    private void createMenuButton() {
        JButton menuButton = new JButton("BACK TO MENU");
        menuButton.setFont(new Font("Impact", Font.PLAIN, 15));
        menuButton.setFocusPainted(false);
        menuButton.setBackground(Color.WHITE);
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                support.firePropertyChange(BACK_TO_MENU, null, null);
            }
        });
        c.gridy = 1;
        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        add(menuButton, c);
        validate();
    }

    private void createRestartButton() {
        JButton restartButton = new JButton("RESTART");
        restartButton.setFont(new Font("Impact", Font.PLAIN, 15));
        restartButton.setFocusPainted(false);
        restartButton.setBackground(Color.WHITE);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                support.firePropertyChange(RESTART, null, null);
            }
        });
        c.gridy = 1;
        c.gridx = 0;
        c.anchor = GridBagConstraints.LINE_END;
        add(restartButton, c);
        validate();
    }

    private void createGameOverLabel() { // can refactor to be initialized like this then added and removed (same with above)
        JLabel gameOverLabel = new JLabel("GAME OVER");
        gameOverLabel.setBackground(COLOR);
        gameOverLabel.setForeground(Color.WHITE);
        gameOverLabel.setFont(new Font("Impact", Font.PLAIN, 50));
        gameOverLabel.setOpaque(false);
        c.gridy = 0;
        c.gridwidth = 10;
        c.anchor = GridBagConstraints.LINE_END;
        add(gameOverLabel, c);
        validate();
    }

    public void addObserver(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void pauseGame() {
        support.firePropertyChange(PAUSE_GAME, null, null);
        pausePanel.setVisible(true);
        pausePanel.setEnabled(true);
    }

    public void unPauseGame() {
        support.firePropertyChange(UN_PAUSE_GAME, null, null);
        pausePanel.setVisible(false);
        pausePanel.setEnabled(false);
    }

    public PausePanel getPausePanel() {
        return pausePanel;
    }

    public void removeObserver(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

}
