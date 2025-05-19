package forms;

import javax.swing.*;

public class MemoryGame extends JFrame {

    public JPanel panelMain = new JPanel();

    public static void main(String[] args) {

        JFrame frame = new JFrame("MemoryGame");
        frame.setVisible(true);
        frame.setContentPane(new MemoryGame());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}