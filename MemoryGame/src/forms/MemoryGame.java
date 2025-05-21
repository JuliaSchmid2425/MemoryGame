package forms;

import database.DatabaseManager;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class MemoryGame{

    private JFrame frame;
    private JPanel panelMain;
    private JPanel panelInfo;
    private JPanel panelGame;
    private JLabel labelTime;
    private JLabel labelPairs;
    private JLabel labelErrors;
    private JLabel labelPoints;
    private JLabel labelUser;

    private static final int CARDS_ROW_COLUMN = 4;
    private static final int TOTAL_PAIRS = 8;
    private static final String[] CARD_VALUES = {":)", ":p", "<3", ":D", ":v", "uwu", "o.0", ":3"};


    private int foundPairs = 0;
    private int pointsCounter= 0;
    private int errorPoints = 0;
    private int seconds = 0;
    private Timer gameTimer;

    private int firstCardIndex = -1;
    private int secondCardIndex = -1;
    private boolean isProcessing = false;

    // Key: index de la primera carta de la parella
    // Value: index de la primera carta de la parella
    private Map<Integer, Integer> cardPairs = new HashMap<>();

    // Tracks which card positions have been revealed (matched or not)
    private Set<Integer> revealedCards = new HashSet<>();

    //guarda el num de vegades que s'ha girat cardIndex.
    // key: index carta. value: num vegades s'ha girat
    private Map<Integer, Integer> cardRevealCount = new HashMap<>();

    //array de 16 jbuttons (cartes)
    private JButton[] cards = new JButton[CARDS_ROW_COLUMN * CARDS_ROW_COLUMN];

    //array de 16 cartes amb disseny (8 parelles)
    private String[] gameCards = new String[CARDS_ROW_COLUMN * CARDS_ROW_COLUMN];

    //guardar les dades per insertar-les a la bd
    private final String username;
    private int pointsDB;
    private int errorsDB;
    private int durationSecondsDB;

    //constructor de JPanel
    public MemoryGame(String username) {

        this.username = username; //guarda username de frontpage


        panelMain = new JPanel(new BorderLayout());
        panelInfo = new JPanel(new GridLayout(1, 5)); //1 fila 4 columnes
        panelGame = new JPanel(new GridLayout(CARDS_ROW_COLUMN, CARDS_ROW_COLUMN, 5, 5)); //gap entre cartes de 5

        //inicialitzar labekls
        labelPairs = new JLabel("Parelles trobades: " + foundPairs + "/8", SwingConstants.CENTER);
        labelPoints = new JLabel("Punts: " + pointsCounter, SwingConstants.CENTER);
        labelErrors = new JLabel("Errors: " + errorPoints, SwingConstants.CENTER);
        labelTime = new JLabel("Temps: " + seconds + "s", SwingConstants.CENTER);
        labelUser = new JLabel("Usuari: " + username, SwingConstants.CENTER);


        //afegir al panelInfo


        frame = new JFrame("MemoryGame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 750);
        frame.setLayout(new BorderLayout());
        frame.setContentPane(panelMain);
        frame.setBackground(Color.white);


        frame.setVisible(true);

        showPanelInfo();
        showPanelGame();

        createCardPairs();
        createCardsDesign();
        startGameTimer();
    }

    //compta l'estona que dura el joc
    private void startGameTimer() {
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seconds++;
                labelTime.setText("Temps: " + seconds + "s");
            }
        });
        gameTimer.start();
    }


    private void showPanelInfo() {
        //afegeix espais dalt i baix
        panelInfo.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panelInfo.add(labelPairs);
        panelInfo.add(labelPoints);
        panelInfo.add(labelErrors);
        panelInfo.add(labelTime);
        panelInfo.add(labelUser);

        panelMain.add(panelInfo, BorderLayout.NORTH);
    }

    private void showPanelGame() {
        panelMain.add(panelGame, BorderLayout.CENTER);
    }

    private void initializePairs() {
        // Create a map to group card positions by their values (A, B, C, etc.)
        Map<String, List<Integer>> valueToPositions = new HashMap<>();

        for (int i = 0; i < gameCards.length; i++) {
            String value = gameCards[i];
            valueToPositions.computeIfAbsent(value, k -> new ArrayList<>()).add(i);
        }

        // Pair up positions for each value
        for (List<Integer> positions : valueToPositions.values()) {
            if (positions.size() == 2) {
                int firstPos = positions.get(0);
                int secondPos = positions.get(1);
                cardPairs.put(firstPos, secondPos); // Store pair
            }
        }
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
        initializePairs();
    }

    private void createCardsDesign() {

        for (int i = 0; i < cards.length; i++) {
            cards[i] = new JButton();
            cards[i].setFont(new Font("Arial", Font.BOLD, 50));
            cards[i].setForeground(Color.BLACK);
            cards[i].setBackground(Color.LIGHT_GRAY);
            cards[i].setOpaque(true);
            cards[i].setBorder(BorderFactory.createLineBorder(Color.gray, 2));
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
            if (!cards[cardIndex].isEnabled() || isProcessing) return;


            //guarda la carta com a ja revelada
            revealedCards.add(cardIndex);

            //cada cop que es clica una carta es +1 al comptador (value del map)
            //valor default és 0 perque encara no s'ha clicat
            cardRevealCount.put(cardIndex, cardRevealCount.getOrDefault(cardIndex, 0) + 1);

            //mostra la carta i canvia color fons
            cards[cardIndex].setText(gameCards[cardIndex]);
            cards[cardIndex].setBackground(Color.WHITE);

            if (firstCardIndex == -1) { //només es compleix quan cap carta esta girada
                firstCardIndex = cardIndex; //guarda el i de la carta

            } else {
                secondCardIndex = cardIndex; //guarda el i de la segona carta
                isProcessing = true; //evita que es pugui girar una tercer carta

                //comprova els dos index corresponguin a la mateixa lletra
                if (gameCards[firstCardIndex].equals(gameCards[secondCardIndex])) {
                    // comprova si es la 1a vegada, per les dues cartes, que s'han girat
                    boolean firstTimePair = cardRevealCount.get(firstCardIndex) == 1 &&
                            cardRevealCount.get(secondCardIndex) == 1;

                    if (firstTimePair) {
                        pointsCounter = pointsCounter + 5;
                    } else {
                        pointsCounter++;
                    }

                    foundPairs++;
                    cards[firstCardIndex].setEnabled(false);
                    cards[secondCardIndex].setEnabled(false);
                    cards[firstCardIndex].setBackground(Color.GREEN);
                    cards[secondCardIndex].setBackground(Color.GREEN);

                    if (foundPairs == TOTAL_PAIRS) {
                        gameTimer.stop();
                        MemoryGame.this.pointsDB = pointsCounter;
                        MemoryGame.this.errorsDB = errorPoints;
                        MemoryGame.this.durationSecondsDB = seconds;

                        JOptionPane.showMessageDialog(panelMain,
                                "Felicitats! Has guanyat amb: " + pointsCounter + " punts!! \n" +
                                        "Durada del joc: " + seconds + "s",
                                "Game Over",
                                JOptionPane.INFORMATION_MESSAGE);

                        saveGameStats();
                    }

                    resetValues();

                } else {
                    errorPoints++;
                    // tarda 1s en tornar a girar les cartes
                    Timer flipBackTimer = new Timer(1000, event -> {
                        cards[firstCardIndex].setText("");
                        cards[secondCardIndex].setText("");
                        cards[firstCardIndex].setBackground(Color.LIGHT_GRAY);
                        cards[secondCardIndex].setBackground(Color.LIGHT_GRAY);
                        resetValues();
                        isProcessing = false;
                    });
                    flipBackTimer.setRepeats(false);
                    flipBackTimer.start();
                }
                updateInfoLabels();
            }
        }
    }

    private void saveGameStats() {
        try {
            DatabaseManager.saveGame(
                    this.username,      // Use the stored username
                    this.pointsDB,     // Use the stored points
                    this.errorsDB,     // Use the stored errors
                    this.durationSecondsDB  // Use the stored duration
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "No s'han pogut desar les estadístiques: " + e.getMessage(),
                    "Error de base de dades",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}

