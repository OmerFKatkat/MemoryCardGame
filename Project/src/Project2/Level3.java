package Project2;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
@SuppressWarnings("serial")
public class Level3 extends GameLogic {

    private static final String IMAGE_DIRECTORY = "Resources/Level3";
    private static final String CARD_BACK_IMAGE = "Resources/Level3no_image.png";
    private List<String> cardImages; 
    private List<JButton> matchedCards; 

    public Level3(String playerName) {
        super(12, 0, CARD_BACK_IMAGE,3,playerName);
        setTitle("Memory Card Game - Level 3");
        matchedCards = new ArrayList<>();
    }

    @Override
    protected List<JButton> createCards() {
        List<JButton> cards = new ArrayList<>();
        List<String> icons = loadIcons(IMAGE_DIRECTORY);

        if (icons.size() > 8) {
            icons = icons.subList(0, 8);
        }

        cardImages = new ArrayList<>(icons); 
        cardImages.addAll(icons);

        Collections.shuffle(cardImages);

        for (String iconPath : cardImages) {
            JButton card = new JButton(new ImageIcon(CARD_BACK_IMAGE));
            card.addActionListener(createCardActionListener(card, iconPath));
            cards.add(card);
        }
        return cards;
    }

    private List<String> loadIcons(String directoryPath) {
        List<String> icons = new ArrayList<>();
        File dir = new File(directoryPath);
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".png");
            }
        });

        if (files != null) {
            for (File file : files) {
                icons.add(file.getAbsolutePath());
            }
        }
        return icons;
    }

    @Override
    protected void restartLevel() {
        dispose();
        new Level3(playerName).setVisible(true);
    }
    
    @Override
    protected void startPreviousLevel() {
		dispose();
		new Level2(playerName).setVisible(true);
		
	}

    @Override
    protected void nextLevel() {
        dispose();
        StringBuilder playerScores = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("HighScore.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" achieved ");
                if (parts.length == 2) {
                    String playerNameInFile = parts[0];
                    String scoreInfo = parts[1];
                    if (playerName.equals(playerNameInFile)) {
                        playerScores.append(playerNameInFile).append(" achieved ").append(scoreInfo).append("\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(this, "Congratulations! You won!\n" + playerScores.toString(), "High Scores", JOptionPane.INFORMATION_MESSAGE);

        new MainMenu().setVisible(true);
    }


    @Override
    protected void flipBackCards() {
        if (getFirstCard() != null && getSecondCard() != null) {
            getFirstCard().setIcon(new ImageIcon(cardBackImagePath));
            getSecondCard().setIcon(new ImageIcon(cardBackImagePath));
            setFirstCard(null);
            setSecondCard(null);
            triesLeft--;
            triesLeftLabel.setText("Tries Left: " + triesLeft);
            if (triesLeft == 0) {
                endGame(false);
            } else {
                shuffleCards();
            }
        }
    }

    @Override
    protected void checkForMatch() {
        if (getFirstCard() != null && getSecondCard() != null) {
            String firstIcon = ((ImageIcon) getFirstCard().getIcon()).getDescription();
            String secondIcon = ((ImageIcon) getSecondCard().getIcon()).getDescription();

            if (firstIcon.equals(secondIcon)) {
            	match += 1;
                updateHighScore(true);
                matchedCards.add(getFirstCard());
                matchedCards.add(getSecondCard());
                getFirstCard().removeActionListener(getFirstCard().getActionListeners()[0]); 
                getSecondCard().removeActionListener(getSecondCard().getActionListeners()[0]);
                setFirstCard(null);
                setSecondCard(null);
                if (match == (cards.size() / 2)) {
                    endGame(true);
                }
            } else {
                getTimer().start();
                updateHighScore(false);
            }
        }
    }
    private void shuffleCards() {
        Thread shuffleThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> unmatchedCardImages = new ArrayList<>();
                List<JButton> unmatchedCards = new ArrayList<>();

                for (int i = 0; i < cards.size(); i++) {
                    JButton card = cards.get(i);
                    if (!matchedCards.contains(card)) {
                        unmatchedCards.add(card);
                        unmatchedCardImages.add(cardImages.get(i));
                    }
                }

                Collections.shuffle(unmatchedCardImages);

                for (int i = 0; i < unmatchedCards.size(); i++) {
                    JButton card = unmatchedCards.get(i);
                    card.setIcon(new ImageIcon(cardBackImagePath)); 
                    String iconPath = unmatchedCardImages.get(i);
                    card.removeActionListener(card.getActionListeners()[0]); 
                    card.addActionListener(createCardActionListener(card, iconPath)); 
                }

                for (int i = 0, j = 0; i < cards.size(); i++) {
                    JButton card = cards.get(i);
                    if (!matchedCards.contains(card)) {
                        cardImages.set(i, unmatchedCardImages.get(j++));
                    }
                }
            }
        });

        shuffleThread.start();
    }


// Thread olmadan
//    private void shuffleCards() {
//        List<String> unmatchedCardImages = new ArrayList<>();
//        List<JButton> unmatchedCards = new ArrayList<>();
//
//        for (int i = 0; i < cards.size(); i++) {
//            JButton card = cards.get(i);
//            if (!matchedCards.contains(card)) {
//                unmatchedCards.add(card);
//                unmatchedCardImages.add(cardImages.get(i));
//            }
//        }
//
//        Collections.shuffle(unmatchedCardImages);
//
//        for (int i = 0; i < unmatchedCards.size(); i++) {
//            JButton card = unmatchedCards.get(i);
//            card.setIcon(new ImageIcon(cardBackImagePath)); 
//            String iconPath = unmatchedCardImages.get(i);
//            card.removeActionListener(card.getActionListeners()[0]); 
//            card.addActionListener(createCardActionListener(card, iconPath)); 
//        }
//
//        for (int i = 0, j = 0; i < cards.size(); i++) {
//            JButton card = cards.get(i);
//            if (!matchedCards.contains(card)) {
//                cardImages.set(i, unmatchedCardImages.get(j++));
//            }
//        }
//    }

   
}
