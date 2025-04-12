import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu extends JPanel {
    // panel dimensions
    int panelWidth = 360;
    int panelHeight = 640;

    // UI components
    JButton startButton;
    JLabel titleLabel;

    // images
    Image backgroundImage;

    public StartMenu() {
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setLayout(null); // Use absolute positioning

        // load background image
        backgroundImage = new ImageIcon("src/assets/background2.jpg").getImage();

        // create title
        titleLabel = new JLabel("Flappy Bird");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(panelWidth/2 - 100, 150, 200, 50);
        add(titleLabel);

        // create start button
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setBounds(panelWidth/2 - 75, 250, 150, 40);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        add(startButton);
    }

    private void startGame() {
        // remove menu panel and show game
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().remove(this);

        // add FlappyBird panel to frame
        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack();
        flappyBird.requestFocus();
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // draw background
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, panelWidth, panelHeight, null);
        }
    }
}