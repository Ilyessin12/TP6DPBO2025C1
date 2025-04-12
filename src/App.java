import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args){
        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Start with menu instead of game
        StartMenu startMenu = new StartMenu();
        frame.add(startMenu);
        frame.pack();
        frame.setVisible(true);
    }
}