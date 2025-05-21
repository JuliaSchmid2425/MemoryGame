package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrontPage {

    JPanel panelMain;
    JPanel panelUser;
    JPanel panelTop;
    JLabel labelUser;
    JTextField fieldUserName;
    JButton buttonStart;


    String usernameInput = "";

    public static void main(String[] args) {
        FrontPage front = new FrontPage();


    }

    public FrontPage() {
        panelMain = new JPanel(new BorderLayout());
        panelTop = new JPanel(new BorderLayout());
        panelUser = new JPanel(new GridLayout(1,2,5,5));
        labelUser = new JLabel("Nom d'usuari: ");
        fieldUserName = new JTextField(50);
        buttonStart= new JButton("Comença el joc!");
        buttonStart.addActionListener(new UsernameSave(usernameInput));








        JFrame frame = new JFrame("FrontPage");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 750);
        frame.setLayout(new BorderLayout());
        frame.setContentPane(panelMain);
        frame.setVisible(true);


        showPanelTop();
        showPanelUser();
    }

    private void showPanelUser(){
        panelMain.add(panelUser, BorderLayout.CENTER);
        panelUser.add(labelUser);
        panelUser.add(fieldUserName);
    }

    private void showPanelTop(){
        panelMain.add(panelTop, BorderLayout.NORTH);
    }

    private void createPageDesign(){

    }


    protected class UsernameSave implements ActionListener {

        //mirar com arreglar aixo
        String username;
        public UsernameSave(String username) {this.username = usernameInput;}

        @Override
        public void actionPerformed(ActionEvent e) {
            usernameInput = fieldUserName.getText();
        }
    }
}