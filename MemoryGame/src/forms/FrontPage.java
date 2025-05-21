package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrontPage {

    private JFrame frame;
    private JPanel panelMain;
    private JPanel panelUser;
    private JPanel panelTop;
    private JPanel panelBottom;
    private JLabel labelUser;
    private JTextField fieldUserName;
    private JButton buttonStart;
    private JLabel labelTitle;
    private JLabel labelInstructions1;
    private JLabel labelInstructions2;
    protected String username;


    public FrontPage() {
        initializeComponents();
        setupFrame();
        showPanelTop();
        showPanelUser();
        showPanelBottom();

    }


    private void initializeComponents() {
        panelMain = new JPanel(new BorderLayout());
        panelTop = new JPanel(new GridLayout(3, 1, 5, 5));
        panelUser = new JPanel(new GridLayout(1,2,5,5));
        panelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));

        panelMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    }

    private void showPanelTop(){
        panelTop.setSize(new Dimension(frame.getWidth(), 150));
        panelTop.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        labelTitle = new JLabel("Benvinguda o benvigut al joc del memory!");
        labelTitle.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitle.setHorizontalAlignment(SwingConstants.CENTER);

        labelInstructions1 = new JLabel("- per cada parella que aconsegueixis, se't sumarà 1 punt.");
        labelInstructions2 = new JLabel( "- si aconsegueixes una parella sense haver-ne girat cap de les dues prèviament, se t'atorgaran 5 punts.");
        labelInstructions1.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 50));
        labelInstructions2.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 50));
        labelInstructions1.setHorizontalAlignment(SwingConstants.LEFT);
        labelInstructions2.setHorizontalAlignment(SwingConstants.LEFT);



        panelTop.add(labelTitle);
        panelTop.add(labelInstructions1);
        panelTop.add(labelInstructions2);

        panelMain.add(panelTop, BorderLayout.NORTH);
    }

    private void showPanelUser(){

        panelUser.setMaximumSize(new Dimension(frame.getWidth(), 60));
        panelUser.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 200));
        //posar label i field un al costat de l'altra, en eix x
        panelUser.setLayout(new BoxLayout(panelUser, BoxLayout.X_AXIS));

        labelUser = new JLabel("Nom d'usuari: ");
        fieldUserName = new JTextField(25);
        fieldUserName.setMaximumSize(new Dimension(panelUser.getWidth()/2, 20));

        panelUser.add(labelUser);
        panelUser.add(fieldUserName);


        panelMain.add(panelUser, BorderLayout.CENTER);
    }

    private void showPanelBottom(){
        panelBottom.setSize(new Dimension(frame.getWidth(), 80));
        buttonStart= new JButton("Comença el joc!");

        panelBottom.add(buttonStart);
        panelBottom.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        buttonStart.setPreferredSize(new Dimension(frame.getWidth()/2, 30));

        panelMain.add(panelBottom, BorderLayout.SOUTH);

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = fieldUserName.getText().trim();
                if (!username.isEmpty()) {
                    startGame(username);
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Si us plau, introdueix un nom d'usuari",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void setupFrame() {
        frame = new JFrame("Pàgina inicial");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 500);
        frame.setMaximumSize(new Dimension(frame.getWidth(), frame.getHeight()));

        frame.setLayout(new BorderLayout());
        frame.setContentPane(panelMain);
        frame.setVisible(true);
    }

    private void startGame(String username) {
        // Close the front page
        frame.dispose();

        // Start the game with the username
        // You might want to pass the username to your MemoryGame constructor
        new MemoryGame(username); // crea nou joc
    }
}