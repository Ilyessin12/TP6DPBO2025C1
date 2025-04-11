import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Objects;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    //frames for jpanel
    int frameWidth = 360;
    int frameHeight = 640;

    //image attributes
    Image backgroundImage;
    Image birdImage;
    Image lowerPipeImage;
    Image upperPipeImage;

    //player attributes
    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;
    Player player;

    //pipe attributes
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    ArrayList<Pipe> pipes;

    //timer
    Timer gameLoop;
    Timer pipesCooldown;
    //gravity
    int gravity = 1;

    //game states
    boolean gameOver = false;
    boolean gameStarted = false;

    //score systems
    int score = 0;
    private JLabel scoreLabel;
    
    // Game over panel
    private GameOverPanel gameOverPanel;

    //constructor
    public FlappyBird(){
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setFocusable(true);
        addKeyListener(this);
        setLayout(null); // Use absolute positioning

        //load images
        backgroundImage = new ImageIcon("src/assets/background.png").getImage();
        birdImage = new ImageIcon("src/assets/bird.png").getImage();
        lowerPipeImage = new ImageIcon("src/assets/lowerPipe.png").getImage();
        upperPipeImage = new ImageIcon("src/assets/upperPipe.png").getImage();

        //create player
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage, 0);

        //create pipes
        pipes = new ArrayList<Pipe>();
        
        // Setup score label
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scoreLabel.setBounds(10, 10, 150, 30);
        add(scoreLabel);
        
        // Setup game over panel
        gameOverPanel = new GameOverPanel(score);
        gameOverPanel.setBounds(frameWidth/2 - 120, frameHeight/2 - 60, 240, 120);
        gameOverPanel.setVisible(false);
        add(gameOverPanel);

        //place pipes
        pipesCooldown = new Timer(1500 , new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //debug
                System.out.println("Placing pipes");
                placePipes();
            }
        });

        pipesCooldown.start();

        //timers
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void draw(Graphics g){
        g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null);

        //draw player
        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);
        //draw pipes
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
        }
    }

    // safeguard function so that the game is not running when the window is closed
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void move() {
        if (gameOver) return;

        // Apply gravity for player movement
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());

        // Check if player hits the ground
        if (player.getPosY() + player.getHeight() >= frameHeight) {
            player.setPosY(frameHeight - player.getHeight());
            gameOver();
        }

        // Move pipes and check for score/collision
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());

            // Remove pipes that are off-screen
            if (pipe.getPosX() + pipe.getWidth() < 0) {
                pipes.remove(i);
                i--;
                continue;
            }

            // Check if player passed pipe (score point)
            if (!pipe.isPassed() && pipe.getPosX() + pipe.getWidth() < player.getPosX()) {
                pipe.setPassed(true);
                if(pipe.passed == true){
                    score++;
                    updateScore();
                }
            }

            // Check for collision with pipes
            if (checkCollision(player, pipe)) {
                gameOver();
            }
        }
    }
    
    // Update score display
    private void updateScore() {
        scoreLabel.setText("Score: " + score/2);
    }

    //pipe methods
    public void placePipes(){
        int randomPosY = (int) (pipeStartPosY - pipeHeight/4 - Math.random() * (pipeHeight/2));
        int openingSpace = frameHeight/4;

        //create upper pipe
        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        upperPipe.setVelocityX(-3);
        pipes.add(upperPipe);

        //create lower pipe
        Pipe lowerPipe = new Pipe(pipeStartPosX, (randomPosY + openingSpace + pipeHeight), pipeWidth, pipeHeight, lowerPipeImage);
        lowerPipe.setVelocityX(-3);
        pipes.add(lowerPipe);
    }

    //collision detection
    private boolean checkCollision(Player player, Pipe pipe) {
        Rectangle playerRect = new Rectangle(
                player.getPosX(), player.getPosY(),
                player.getWidth(), player.getHeight()
        );

        Rectangle pipeRect = new Rectangle(
                pipe.getPosX(), pipe.getPosY(),
                pipe.getWidth(), pipe.getHeight()
        );

        return playerRect.intersects(pipeRect);
    }

    private void gameOver() {
        gameOver = true;
        gameLoop.stop();
        pipesCooldown.stop();
        
        // Update and show game over panel
        gameOverPanel.updateScore(score);
        gameOverPanel.setVisible(true);
    }

    private void resetGame() {
        // Reset game state
        gameOver = false;
        score = 0;
        updateScore();

        // Reset player
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);

        // Clear pipes
        pipes.clear();
        
        // Hide game over panel
        gameOverPanel.setVisible(false);

        // Restart timers
        gameLoop.start();
        pipesCooldown.start();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        //move player
        move();
        //repaint the screen
        repaint();
    }

    //handling keyboard inputs

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // press space to jump
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.setVelocityY(-10);
        }
        //press R to RE:Do
        if (e.getKeyCode() == KeyEvent.VK_R) {
            if (gameOver) {
                resetGame();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    
    // Inner class for Game Over Panel
    private class GameOverPanel extends JPanel {
        private JLabel gameOverLabel;
        private JLabel scoreDisplayLabel;
        private JLabel restartLabel;
        
        public GameOverPanel(int score) {
            setLayout(null);
            setOpaque(false);
            
            // Game over text
            gameOverLabel = new JLabel("GAME OVER");
            gameOverLabel.setForeground(Color.WHITE);
            gameOverLabel.setFont(new Font("Arial", Font.BOLD, 28));
            gameOverLabel.setBounds(30, 10, 200, 30);
            add(gameOverLabel);
            
            // Score text
            scoreDisplayLabel = new JLabel("SCORE: " + score);
            scoreDisplayLabel.setForeground(Color.WHITE);
            scoreDisplayLabel.setFont(new Font("Arial", Font.BOLD, 22));
            scoreDisplayLabel.setBounds(60, 50, 150, 25);
            add(scoreDisplayLabel);
            
            // Restart instructions
            restartLabel = new JLabel("Press 'R' to Restart");
            restartLabel.setForeground(Color.WHITE);
            restartLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            restartLabel.setBounds(50, 80, 150, 20);
            add(restartLabel);
        }
        
        public void updateScore(int score) {
            scoreDisplayLabel.setText("SCORE: " + score/2);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Draw semi-transparent background
            g.setColor(new Color(80, 220, 80, 100));
            g.fillRect(0, 0, getWidth(), getHeight());
            
            // Draw panel border
            g.setColor(Color.WHITE);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            g.drawRect(-2, -2, getWidth() + 3, getHeight() + 3);
        }
    }
}
