package forms;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MemoryGame extends JFrame {

    public JPanel panelMain = new JPanel();

    private static final int CARDS_ROW_COLUMN = 4;
    private static final int TOTAL_PAIRS = 8;
    private static final String[] CARD_VALUES = {"A", "B", "C", "D", "E", "F", "G", "H"};

    //array de 16 jbuttons (cartes)
    private JButton[] cards = new JButton[CARDS_ROW_COLUMN * CARDS_ROW_COLUMN];

    //array de 16 cartes amb disseny (8 parelles)
    private String[] gameCards = new String[CARDS_ROW_COLUMN * CARDS_ROW_COLUMN];

    public static void main(String[] args) {
        JFrame frame = new JFrame("MemoryGame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);
        frame.setLayout(new BorderLayout());
        MemoryGame game = new MemoryGame(); // crea nou joc
        frame.setContentPane(game.panelMain);
        frame.setVisible(true);
    }

    //constructor de JPanel
    public MemoryGame() {
        panelMain = new JPanel(new GridLayout(CARDS_ROW_COLUMN, CARDS_ROW_COLUMN)); // panelMain is now used
        initializeGame();
    }

    private void initializeGame() {
        String[] cardPairs = new String[TOTAL_PAIRS * 2];
        for (int i = 0; i < TOTAL_PAIRS; i++) {
            cardPairs[i * 2] = CARD_VALUES[i];
            cardPairs[i * 2 + 1] = CARD_VALUES[i];
        }

        List<String> cardList = Arrays.asList(cardPairs);
        Collections.shuffle(cardList);
        gameCards = cardList.toArray(new String[0]);
    }
}