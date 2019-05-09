package ui;

import model.GameModel;
import ui.gameplay.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class HomePanel extends JPanel {

    // constasnts used for PropertyChangeEvent names
    public static final String GAME_START = "Game Start";
    public static final String HOW_TO_PLAY = "How to play";

    public static final int WIDTH = GameModel.WIDTH;
    public static final int HEIGHT = GameModel.HEIGHT + GameModel.HEIGHT / 10;
    public static final Color COLOR_BUTTON = new Color(135, 185, 255);
    public static final Dimension TITLE_SIZE = new Dimension(300, 70);
    public static final Dimension DIFFICULTY_LABEL_SIZE = new Dimension(165, 30);
    public static final Color COLOR_START_BUTTON = new Color(250, 100, 100);
    public static final Color COLOR_BUTTON_SELECTED = new Color(97, 130, 176, 211);

    public static final int UNSELECTED = 0;
    public static final int EASY = 1;
    public static final int NORMAL = 2;

    private PropertyChangeSupport support; // used to notify jframe when to replace displayed jpanel
    private JButton instructionsButton;
    private JLabel gameTitleLabel;
    private JLabel difficultyLabel;
    private JButton easyButton;
    private JButton normalButton;
    private JButton startButton;
    private JLabel startLabel;
    private int difficultySelected;
    private GridBagConstraints c;

    public HomePanel() {
        difficultySelected = 0;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        initSubComponents();
        support = new PropertyChangeSupport(this);
    }

    private void initSubComponents() {
        initGameTitleLabel();
        initInstructionsButton();
        initDifficultyButtons();
        initStartButton();
    }

    private void initGameTitleLabel() {
        gameTitleLabel = new JLabel("Deep Sea Shooter");
        gameTitleLabel.setFont(new Font("Impact", Font.PLAIN, 40));
        gameTitleLabel.setPreferredSize(TITLE_SIZE);
        c.weighty = .5;
        c.gridx = 2;
        c.gridy = 0;
        add(gameTitleLabel, c);
    }

    private void initInstructionsButton() {
        instructionsButton = new JButton();
        createButton("HOW TO PLAY", 20, instructionsButton, COLOR_BUTTON);
        instructionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                support.firePropertyChange(HOW_TO_PLAY, null, null);
            }
        });
        c.weighty = .3;
        c.gridy = 1;
        c.gridx = 2;
        add(instructionsButton, c);

    }

    private void initDifficultyButtons() {
        difficultyLabel = new JLabel("CHOOSE YOUR DIFFICULTY: ");
        difficultyLabel.setPreferredSize(DIFFICULTY_LABEL_SIZE);
        difficultyLabel.setFont(new Font("Impact", Font.PLAIN, 15));
        c.gridy = 2;
        c.gridx = 2;
        c.anchor = GridBagConstraints.PAGE_END;
        add(difficultyLabel, c);

        easyButton = new JButton();
        createButton("EASY", 12, easyButton, COLOR_BUTTON);
        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int prevDifficulty = difficultySelected;
                difficultySelected = EASY;
                easyButton.setBackground(COLOR_BUTTON_SELECTED);
                if (prevDifficulty == NORMAL) {
                    normalButton.setBackground(COLOR_BUTTON);
                }
            }
        });
        c.gridy = 3;
        c.gridx = 2;
        c.anchor = GridBagConstraints.LINE_START;
        add(easyButton, c);

        normalButton = new JButton();
        createButton("NORMAL", 12, normalButton, COLOR_BUTTON);
        normalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int prevDifficulty = difficultySelected;
                difficultySelected = NORMAL;
                normalButton.setBackground(COLOR_BUTTON_SELECTED);
                if (prevDifficulty == EASY) {
                    easyButton.setBackground(COLOR_BUTTON);
                }
            }
        });
        c.gridy = 3;
        c.gridx = 2;
        c.anchor = GridBagConstraints.LINE_END;
        add(normalButton, c);
    }

    private void createButton(String text, int fontSize, JButton button, Color color) {
        button.setText(text);
        button.setFont(new Font("Impact", Font.PLAIN, fontSize));
        button.setFocusPainted(false);
        button.setBackground(color);
    }

    private void initStartButton() {
        startLabel = new JLabel();
        startLabel.setFont(new Font("Times", Font.BOLD, 12));
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 2;
        c.gridy = 4;
        add(startLabel, c);

        startButton = new JButton();
        createButton("START", 30, startButton, COLOR_START_BUTTON);
        c.gridx = 2;
        c.gridy = 5;
        c.weighty = .5;
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (difficultySelected != UNSELECTED) {
                    support.firePropertyChange(GAME_START, null, difficultySelected);
                } else {
                    startLabel.setText("Error: Select a difficulty");
                }

            }
        });
        add(startButton, c);
    }

    public void addObserver(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }
}
