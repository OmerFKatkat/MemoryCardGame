package Project2;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public abstract class GameLogic extends JFrame {
    protected int triesLeft;
    protected int match;
    protected int HighScore;
    protected JLabel highScoreLabel;
    protected JLabel triesLeftLabel;
    protected JLabel scoreLabel;
    protected List<JButton> cards;
    protected List<String> cardImages;
    private JButton firstCard;
    private JButton secondCard;
    private Timer timer;
    protected String cardBackImagePath;
    protected int level;
    protected int accumulatedHighScore = 0;
    protected String playerName;

    
    public GameLogic(int triesLeft, int score, String cardBackImagePath, int level, String playerName) {
        this.playerName = playerName;
        this.triesLeft = triesLeft;
        this.match = score;
        this.firstCard = null;
        this.secondCard = null;
        this.cardBackImagePath = cardBackImagePath;
        this.level = level;

        setTitle("Memory Card Game - Level " + level);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel();
        if (level == 1) {        	
        	topPanel.setBackground(new Color(85, 130, 163));
        }
        else if(level == 2) {
        	topPanel.setBackground(new Color(127, 73, 161));
        }
        else {
        	topPanel.setBackground(new Color(158, 32, 20));
        }
        topPanel.setLayout(new GridLayout(0,2));
        
        
        Font labelFont = new Font("Arial", Font.BOLD,60); 
        triesLeftLabel = new JLabel("Tries Left: " + triesLeft,SwingConstants.CENTER);
        triesLeftLabel.setFont(labelFont);
        triesLeftLabel.setForeground(Color.WHITE);
        
        scoreLabel = new JLabel("   Level: " + level); 
        scoreLabel.setFont(labelFont); 
        scoreLabel.setForeground(Color.WHITE);
        
        topPanel.setLayout(new BorderLayout()); 
        topPanel.add(triesLeftLabel, BorderLayout.CENTER);
        topPanel.add(scoreLabel, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);


        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(4, 4));
        cards = createCards();
        for (JButton card : cards) {
            gamePanel.add(card);
        }
        add(gamePanel, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new GridBagLayout()); //ortalamak için
        JMenu gameMenu = new JMenu("Game");

        JMenuItem restartItem = new JMenuItem("Restart");
        restartItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartLevel();
            }
        });

        JMenuItem highScoresItem = new JMenuItem("High Scores");
        highScoresItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	displayLastTenScores();
            }
        });

        gameMenu.setMnemonic(KeyEvent.VK_G);
        restartItem.setMnemonic(KeyEvent.VK_R);

        gameMenu.add(restartItem);
        gameMenu.add(highScoresItem);
        menuBar.add(gameMenu);
        
        
        JMenu aboutMenu = new JMenu("About");

        JMenuItem aboutGameItem = new JMenuItem("About the Game");
        aboutGameItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(GameLogic.this, "The Memory Cards game is a single-player game with three levels\nwhere players match themed card pairs within a limited number of tries to progress.", "About the Game", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JMenuItem aboutDeveloperItem = new JMenuItem("About the Developer");
        aboutDeveloperItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(GameLogic.this, "Student Name: Ömer Faruk Katkat\nStudent Number: 20210702067", "About the Developer", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        aboutMenu.add(aboutGameItem);
        aboutMenu.add(aboutDeveloperItem);
        menuBar.add(aboutMenu);

        JMenu exitMenu = new JMenu("Exit");
        exitMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                System.exit(0);
            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuCanceled(MenuEvent e) {
            }
        });
        menuBar.add(exitMenu);

        setJMenuBar(menuBar);

        timer = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flipBackCards();
            }
        });
        timer.setRepeats(false);
    }

    protected abstract List<JButton> createCards();

    protected abstract void restartLevel();

    protected abstract void nextLevel();
    
    protected abstract void startPreviousLevel();
    
    protected void flipBackCards() {
        if (firstCard != null && secondCard != null) {
            firstCard.setIcon(new ImageIcon(cardBackImagePath));
            secondCard.setIcon(new ImageIcon(cardBackImagePath));
            firstCard = null;
            secondCard = null;
            triesLeft--;
            triesLeftLabel.setText("Tries Left: " + triesLeft);
            if (triesLeft == 0) {
                endGame(false);
            }
        }
    }

    protected void endGame(boolean won) {
        String message = won ? "Congratulations! You won!!" : "Game Over! You lost!";
        JOptionPane.showMessageDialog(GameLogic.this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        if (won) {
        	saveHighScore(level);
            nextLevel();
        } else {
        	saveHighScore(level);
        	startPreviousLevel();
        }
    }

    protected void checkForMatch() {
        if (firstCard != null && secondCard != null) {
            String firstIcon = ((ImageIcon) firstCard.getIcon()).getDescription();
            String secondIcon = ((ImageIcon) secondCard.getIcon()).getDescription();

            if (firstIcon.equals(secondIcon)) {
                match += 1;
                updateHighScore(true);
                firstCard.removeActionListener(firstCard.getActionListeners()[0]); 
				secondCard.removeActionListener(secondCard.getActionListeners()[0]); 
                firstCard = null;
                secondCard = null;
                if (match == (cards.size() / 2)) {
                    endGame(true);
                }
            } else {
                timer.start();
                updateHighScore(false);
            }
        }
    }

    protected ActionListener createCardActionListener(JButton card, String iconPath) {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (firstCard == null) {
                    firstCard = card;
                    card.setIcon(new ImageIcon(iconPath));
                } else if (secondCard == null && card != firstCard) {
                    secondCard = card;
                    card.setIcon(new ImageIcon(iconPath));
                    checkForMatch();
                }
            }
        };
    }
    protected int getMatchPoints() {
        switch (level) {
            case 1:
                return 5;
            case 2:
                return 4;
            case 3:
                return 3;
            default:
                return 0;
        }
    }

    protected int getPenaltyPoints() {
        switch (level) {
            case 1:
                return -1;
            case 2:
                return -2;
            case 3:
                return -3;
            default:
                return 0;
        }
    }

    protected void updateHighScore(boolean correctMatch) {
        if (correctMatch) {
            accumulatedHighScore += getMatchPoints();
        } else {
            accumulatedHighScore += getPenaltyPoints();
        }
    }

    protected void saveHighScore(int level) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("HighScore.txt", true))) {
            writer.write(playerName + " achieved " + accumulatedHighScore + " points at Level " + level + "\n");
            accumulatedHighScore = 0; 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void displayHighScores() {
        StringBuilder highScores = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("HighScore.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                highScores.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, highScores.toString(), "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void displayLastTenScores() {
        StringBuilder highScores = new StringBuilder();
        ArrayList<String> lastTenLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("HighScore.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lastTenLines.add(line);
                if (lastTenLines.size() > 10) {
                    lastTenLines.remove(0); 
                }
            }
            for (String s : lastTenLines) {
                highScores.append(s+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(this, highScores.toString(), "Last 10 High Scores", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public JButton getSecondCard() {
        return secondCard;
    }

    public void setSecondCard(JButton secondCard) {
        this.secondCard = secondCard;
    }

    public int getTriesLeft() {
        return triesLeft;
    }

    public JLabel getTriesLeftLabel() {
        return triesLeftLabel;
    }

    public JLabel getScoreLabel() {
        return scoreLabel;
    }

    public List<JButton> getCards() {
        return cards;
    }

    public void setFirstCard(JButton firstCard) {
        this.firstCard = firstCard;
    }

    public List<String> getCardImages() {
        return cardImages;
    }

    public JButton getFirstCard() {
        return firstCard;
    }

    public Timer getTimer() {
        return timer;
    }

    public String getCardBackImagePath() {
        return cardBackImagePath;
    }
}
