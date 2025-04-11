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



    //constructor
    public FlappyBird(){
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setFocusable(true);
        addKeyListener(this);
        //setBackground(Color.blue);

        //load images
        backgroundImage = new ImageIcon("src/assets/background.png").getImage();
        birdImage = new ImageIcon("src/assets/bird.png").getImage();
        lowerPipeImage = new ImageIcon("src/assets/lowerPipe.png").getImage();
        upperPipeImage = new ImageIcon("src/assets/upperPipe.png").getImage();

        //create player
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage, 0);

        //create pipes
        pipes = new ArrayList<Pipe>();

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void move(){
        //apply gravity for player movement
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        //move pipes
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());
        }
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
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            //jump
            player.setVelocityY(-10);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}