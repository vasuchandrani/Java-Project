import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


class Brick extends Thread {
    int[][] map;
    
    Brick() {

        map = new int[8][5];

        for (int[] map1 : map) {
            for (int j = 0; j<map[0].length; j++) {
                map1[j] = 1;
            }
        }
    }
    void initializeMap() {
        for (int[] map1 : map) {
            for (int j = 0; j<map[0].length; j++) {
                map1[j] = 1;
            }
        }
    }

    public void run() {

        
    }

    public void paint (Graphics g) {
        for (int i=0; i<map.length; i++) {
            for(int j=0; j<map[0].length; j++) {
                if (map[i][j] == 1) {
                    g.drawRect(50 + 50*i, 50 + 50*j, 48, 48);
                }
            }
        }
    }
}


public class BrickGame extends Frame implements KeyListener,Runnable {

    Brick brick;

    int paddleX;
    int x1, y1;
    int xSpeed, ySpeed;
    boolean ballMoving = false;
    boolean gameOver = false; 

    BrickGame() {

        setTitle("Brick Breaker Game");
        setSize(500, 500);
        setVisible(true);

        brick = new Brick();

        paddleX = 200;
        x1 = 250;
        y1 = 400;
        xSpeed = 10;
        ySpeed = -13;

        addKeyListener(this);
    }   

    public void paint(Graphics g) {


        g.setColor(Color.BLACK);
        g.fillRect(paddleX, 450, 100, 10); // Paddle
        g.fillOval(x1, y1, 20, 20); // Ball 
        
        if (gameOver) {
            
            g.setColor(Color.RED);
            g.drawString("GAME OVER", getWidth() / 2 - 35, getHeight() / 2);
            g.drawString("PRESS SPACEBAR TO RESTART", getWidth() / 2 - 90, getHeight() / 2 + 30);
        
        }
    }

    @Override
    public void run() {

        while (ballMoving) {
            x1 += xSpeed;
            y1 += ySpeed;
    
            // Ball collision with walls
            if (x1 < 30 || x1 > getWidth() - 30) {
                xSpeed = -xSpeed;
            }
            if (y1 < 30) {
                ySpeed = -ySpeed;
            }
            if (y1 > getHeight() - 30) {
                ballMoving = false;
                gameOver = true;
            }

            // Ball collision with paddle
            if (y1 + 25 >= 450 && x1 + 20 >= paddleX && x1 <= paddleX + 100) {
                ySpeed = -ySpeed;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        BrickGame b = new BrickGame();
        Thread t = new Thread(b);

        t.start();

    }

}
