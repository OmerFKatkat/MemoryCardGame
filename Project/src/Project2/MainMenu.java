package Project2;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
@SuppressWarnings("serial")
public class MainMenu extends JFrame {
	private BufferedImage backgroundImage;

    public MainMenu() {
        setTitle("Memory Card Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            backgroundImage = ImageIO.read(new File("Resources/background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel backgroundPanel = new BackgroundPanel();
        setContentPane(backgroundPanel);
        backgroundPanel.setLayout(new GridBagLayout());

        JLabel title = new JLabel("Memory Card Game");
        title.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 24));
        title.setForeground(new Color(0, 200, 255));

        JButton startGameButton = new JButton("Start Game");
        JButton selectLevelButton = new JButton("Select Level");
        JButton instructionsButton = new JButton("Instructions");
        JButton exitButton = new JButton("Exit");

        startGameButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                String playerName = JOptionPane.showInputDialog(MainMenu.this, "Enter your name:", "Player Name", JOptionPane.PLAIN_MESSAGE);
                if (playerName != null && !playerName.trim().isEmpty()) {
                    new Level1(playerName).setVisible(true);
//                    dispose();
                }
            }
        });

        selectLevelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Point parentLocation = getLocationOnScreen();
                int x = (int) (parentLocation.getX() + getWidth() / 2);
                int y = (int) (parentLocation.getY() + getHeight() / 2);

                String[] options = {"Level 1", "Level 2", "Level 3"};
                int selection = JOptionPane.showOptionDialog(MainMenu.this, "Select Level", "Level Selection",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                Point dialogLocation = new Point(x, y);
                JOptionPane.getRootFrame().setLocation(dialogLocation);
                
                String playerName = JOptionPane.showInputDialog(MainMenu.this, "Enter your name:", "Player Name", JOptionPane.PLAIN_MESSAGE);
                if (playerName != null && !playerName.trim().isEmpty()) {
                    switch (selection) {
                        case 0: new Level1(playerName).setVisible(true); break;
                        case 1: new Level2(playerName).setVisible(true); break;
                        case 2: new Level3(playerName).setVisible(true); break;
                    }
//                    dispose();
                }
            }
        });

        instructionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainMenu.this, "Instructions: \nThere are 3 levels in this game.It gets gradually harder!\nMatch all pairs of cards to win!","Instructions", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        backgroundPanel.add(title, gbc);
        gbc.gridy++;
        backgroundPanel.add(startGameButton, gbc);
        gbc.gridy++;
        backgroundPanel.add(selectLevelButton, gbc);
        gbc.gridy++;
        backgroundPanel.add(instructionsButton, gbc);
        gbc.gridy++;
        backgroundPanel.add(exitButton, gbc);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
	private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
