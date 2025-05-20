package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MemoryGame extends JFrame {

    public JPanel panelMain;
    private JPanel panelInfo;
    private JPanel panelGame;
    private JLabel labelTime;
    private JLabel labelPoints;
    private JLabel labelErrors;
    private JLabel labelUser;

    private static final int CARDS_ROW_COLUMN = 4;
    private static final int TOTAL_PAIRS = 8;
    private static final String[] CARD_VALUES = {"A", "B", "C", "D", "E", "F", "G", "H"};

    private int points = 0;
    private int errorPoints = 0;
    private int seconds = 0;
    private String nomUsuari;

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
        panelMain.setLayout(null);
        panelInfo.setLayout(new GridLayout());
        panelGame.setLayout(new GridLayout(CARDS_ROW_COLUMN, CARDS_ROW_COLUMN));

        showPanelInfo();
        showPanelGame();
        createCardPairs();
        setupCards();
    }



    private void showPanelInfo(){
        panelInfo.setSize(panelMain.getWidth(), 80);
        panelMain.add(panelInfo);
        labelPoints.setText("Parelles trobades: " + points + "/8");
        labelErrors.setText("Errors comesos: " + errorPoints);

    }

    private void showPanelGame(){
        panelGame.setSize(panelMain.getWidth(), panelMain.getHeight()-panelGame.getHeight());
        panelMain.add(panelGame);
    }

    //crea parelles de cartes aleatòries
    private void createCardPairs() {
        String[] cardPairs = new String[TOTAL_PAIRS * 2];
        for (int i = 0; i < TOTAL_PAIRS; i++) {
            cardPairs[i * 2] = CARD_VALUES[i];
            cardPairs[i * 2 + 1] = CARD_VALUES[i];
        }

        List<String> cardList = Arrays.asList(cardPairs);
        Collections.shuffle(cardList);
        gameCards = cardList.toArray(new String[0]);
    }

    private void setupCards() {

        for (int i = 0; i < cards.length; i++) {
            cards[i] = new JButton();
            cards[i].setFont(new Font("Arial", Font.BOLD, 50));
            cards[i].setBackground(Color.LIGHT_GRAY);
            cards[i].setOpaque(true);
            //   cards[i].addActionListener(new CardClickListener(i));
            panelGame.add(cards[i]);
        }
    }
}