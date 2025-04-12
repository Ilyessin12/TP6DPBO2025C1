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

    // Menu panel
    private MenuPanel menuPanel;

    public StartMenu() {
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setLayout(null); // Use absolute positioning

        // load background image
        backgroundImage = new ImageIcon("src/assets/background2.jpg").getImage();

        // Setup menu panel
        menuPanel = new MenuPanel();
        menuPanel.setBounds(panelWidth/2 - 120, panelHeight/2 - 100, 240, 200);
        add(menuPanel);
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

    // Inner class for Menu Panel
    private class MenuPanel extends JPanel {
        private JLabel gameTitleLabel;
        private JLabel instructionLabel;

        public MenuPanel() {
            setLayout(null);
            setOpaque(false);

            // Game title text
            gameTitleLabel = new JLabel("FLAPPY BIRD");
            //white colored text
            gameTitleLabel.setForeground(Color.WHITE);
            gameTitleLabel.setFont(new Font("Arial", Font.BOLD, 28));
            gameTitleLabel.setBounds(30, 20, 200, 30);
            add(gameTitleLabel);

            // Instruction text
            instructionLabel = new JLabel("Click to Start:");
            //white colored text
            instructionLabel.setForeground(Color.WHITE);
            instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            instructionLabel.setBounds(70, 70, 150, 20);
            add(instructionLabel);

            // Start button - mouse click only
            startButton = new JButton("Start Game");
            // button color
            startButton.setBackground(new Color(110, 238, 234)); // blue ish
            // color for when the button is pressed
            startButton.setForeground(Color.YELLOW);
            startButton.setFont(new Font("Arial", Font.BOLD, 20));
            startButton.setBounds(45, 110, 150, 40);
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    startGame();
                }
            });
            add(startButton);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw semi-transparent light green background
            g.setColor(new Color(144, 238, 144, 180)); // Light green with some transparency
            g.fillRect(0, 0, getWidth(), getHeight());

            // Draw panel border
            g.setColor(Color.WHITE);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            g.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
        }
    }
}