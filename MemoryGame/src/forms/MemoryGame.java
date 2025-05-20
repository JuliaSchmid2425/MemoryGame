package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MemoryGame{

    public JPanel panelMain;
    private JPanel panelInfo;
    private JPanel panelGame;
    private JLabel labelTime;
    private JLabel labelPairs;
    private JLabel labelErrors;
    private JLabel labelPoints;
    private JLabel labelUser;


    private static final int CARDS_ROW_COLUMN = 4;
    private static final int TOTAL_PAIRS = 8;
    private static final String[] CARD_VALUES = {"A", "B", "C", "D", "E", "F", "G", "H"};

    private int foundPairs = 0;
    private int pointsCounter= 0;
    private int errorPoints = 0;
    private int seconds = 0;
    private String userName;

    private int firstCardIndex = -1;
    private int secondCardIndex = -1;
    private boolean isProcessing = false;

    //array de 16 jbuttons (cartes)
    private JButton[] cards = new JButton[CARDS_ROW_COLUMN * CARDS_ROW_COLUMN];

    //array de 16 cartes amb disseny (8 parelles)
    private String[] gameCards = new String[CARDS_ROW_COLUMN * CARDS_ROW_COLUMN];

    public static void main(String[] args){
        MemoryGame game = new MemoryGame(); // crea nou joc
    }

    //constructor de JPanel
    public MemoryGame() {

        panelMain = new JPanel(new BorderLayout());
        panelInfo = new JPanel(new GridLayout(1, 4)); //1 fila 4 columnes
        panelGame = new JPanel(new GridLayout(CARDS_ROW_COLUMN, CARDS_ROW_COLUMN, 5, 5)); //gap entre cartes de 5

        //inicialitzar labekls
        labelPairs = new JLabel("Parelles trobades: " + foundPairs + "/8", SwingConstants.CENTER);
        labelPoints = new JLabel("Punts: " + pointsCounter, SwingConstants.CENTER);
        labelErrors = new JLabel("Errors: " + errorPoints, SwingConstants.CENTER);
        labelTime = new JLabel("Temps: " + seconds + "s", SwingConstants.CENTER);
        labelUser = new JLabel("Juagdor: " + userName, SwingConstants.CENTER);

        //afegir al panelInfo
        panelInfo.add(labelPairs);
        panelInfo.add(labelPoints);
        panelInfo.add(labelErrors);
        panelInfo.add(labelTime);
        panelInfo.add(labelUser);

        JFrame frame = new JFrame("MemoryGame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 750);
        frame.setLayout(new BorderLayout());
        frame.setContentPane(panelMain);

        //potser cal posar final constructor
        frame.setVisible(true);

        showPanelInfo();
        showPanelGame();

        createCardPairs();
        createCardsDesign();
    }


    private void showPanelInfo() {
        //afegeix espais dalt i baix
        panelInfo.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panelMain.add(panelInfo, BorderLayout.NORTH);
    }

    private void showPanelGame() {
        panelMain.add(panelGame, BorderLayout.CENTER);
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

    private void createCardsDesign() {

        for (int i = 0; i < cards.length; i++) {
            cards[i] = new JButton();
            cards[i].setFont(new Font("Arial", Font.BOLD, 50));
            cards[i].setForeground(Color.BLACK);
            cards[i].setBackground(Color.LIGHT_GRAY);
            cards[i].setOpaque(true);
            cards[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            cards[i].addActionListener(new CardClickListener(i));
            panelGame.add(cards[i]);
        }
    }

    private void updateInfoLabels() {
        //actualitza dades panelInfo
        labelPairs.setText("Parelles trobades: " + foundPairs + "/8");
        labelPoints.setText("Punts: " + pointsCounter);
        labelErrors.setText("Errors: " + errorPoints);
    }

    private void resetValues() {
        //torna a valors inicials
        firstCardIndex = -1;
        secondCardIndex = -1;
        isProcessing = false;
    }

    private class CardClickListener implements ActionListener {
        private final int cardIndex;

        public CardClickListener(int index) {
            this.cardIndex = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            //no es pot girar una carta si ja esta girada o encara no hem acabat la ronda anterior
            if (!cards[cardIndex].getText().isEmpty() || isProcessing)
                { return; }

            cards[cardIndex].setText(gameCards[cardIndex]);
            cards[cardIndex].setBackground(Color.WHITE);


            if (firstCardIndex == -1) { //només es compleix quan cap carta esta girada
                firstCardIndex = cardIndex; //guarda el i de la carta
            } else {
                secondCardIndex = cardIndex; //guarda el i de la segona carta
                isProcessing = true; //evita que es pugui girar una tercer carta

                if (gameCards[firstCardIndex].equals(gameCards[secondCardIndex])) {
                    foundPairs++;
                    pointsCounter++;
                    cards[firstCardIndex].setEnabled(false);
                    cards[secondCardIndex].setEnabled(false);
                    cards[firstCardIndex].setBackground(Color.GREEN);
                    cards[secondCardIndex].setBackground(Color.GREEN);
                    updateInfoLabels();


                    if (foundPairs == TOTAL_PAIRS) {
                        JOptionPane.showMessageDialog(panelMain,
                                "Felicitats! Has guanyat amb: " + pointsCounter + " punts!!",
                                "Game Over",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                    resetValues();

                }else {
                    errorPoints++;
                    updateInfoLabels();

                    Timer timer = new Timer(1000, event-> {
                        cards[firstCardIndex].setText("");
                        cards[secondCardIndex].setText("");
                        cards[firstCardIndex].setBackground(Color.LIGHT_GRAY);
                        cards[secondCardIndex].setBackground(Color.LIGHT_GRAY);


                        resetValues();
                    });

                    timer.setRepeats(false);
                    timer.start();
                }
            }
        }
    }
}
