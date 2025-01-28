package Project2;

import javax.swing.*;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@SuppressWarnings("serial")
public class Level2 extends GameLogic {

    private static final String IMAGE_DIRECTORY = "Resources/Level2";
    private static final String CARD_BACK_IMAGE = "Resources/Level2no_image.png";

    public Level2(String playerName) {
        super(15, 0, CARD_BACK_IMAGE,2,playerName);
        setTitle("Memory Card Game - Level 2");
    }

    @Override
    protected List<JButton> createCards() {
        List<JButton> cards = new ArrayList<>();
        List<String> icons = loadIcons(IMAGE_DIRECTORY);
        
        if (icons.size() > 8) {
            icons = icons.subList(0, 8);
        }
        
        
        List<String> cardImages = new ArrayList<>(icons);
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
        new Level2(playerName).setVisible(true);
    }
    @Override
    protected void nextLevel() {
        dispose();
        new Level3(playerName).setVisible(true);
    }
    @Override
    protected void startPreviousLevel() {
		dispose();
		new Level1(playerName).setVisible(true);
		
	}
}


