import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BrickBreaker extends Frame implements KeyListener, Runnable {
    int paddleX = 250;
    int paddleY = 450;
    int paddleWidth = 80;
    int paddleHeight = 15;

    int ballX = 300;
    int ballY = 400;
    int ballDiameter = 20;
    int ballXSpeed = 0xfffffffa;
    int ballYSpeed = -6;

    ArrayList<Rectangle> bricks = new ArrayList<>();
    int BRICK_ROWS = 4;
    int BRICK_COLUMNS = 9;
    int BRICK_WIDTH = 40;
    int BRICK_HEIGHT = 40;

    static Thread t;

    public BrickBreaker() {
        setTitle("Brick Breaker Game");
        setSize(500, 500);
        setResizable(false);
        setVisible(true);
        addKeyListener(this);
        
        // Initialize bricks
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLUMNS; j++) {
                bricks.add(new Rectangle(j * (BRICK_WIDTH + 10) + 35, i * (BRICK_HEIGHT + 10) + 50, BRICK_WIDTH, BRICK_HEIGHT));
            }
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }
    @Override
    public void paint(Graphics g) {
        // Clear the screen
        // g.clearRect(0, 0, getWidth(), getHeight());

        // Draw Paddle
        g.fillRect(paddleX, paddleY, paddleWidth, paddleHeight);

        // Draw Ball
        g.fillOval(ballX, ballY, 20, 20);

        // Draw Bricks
        for (Rectangle brick : bricks) {
            g.fillRect(brick.x, brick.y, brick.width, brick.height);
        }
    }

    public void run() {
        while (true) {
            // Move the ball
            ballX += ballXSpeed;
            ballY += ballYSpeed;

            // Ball collision with walls
            if (ballX <= 0 || ballX >= getWidth() - ballDiameter) {
                ballXSpeed = -ballXSpeed;
            }
            if (ballY <= 0) {
                ballYSpeed = -ballYSpeed;
            }

            // Ball collision with paddle
            if (ballY + ballDiameter >= paddleY && ballX + ballDiameter/2 >= paddleX && ballX <= paddleX + paddleWidth) {
                ballYSpeed = -ballYSpeed;
            }

            // Ball collision with bricks
            for (int i = 0; i < bricks.size(); i++) {
                Rectangle brick = bricks.get(i);
                if (brick.intersects(new Rectangle(ballX, ballY, ballDiameter, ballDiameter))) {
                    bricks.remove(i);
                    ballYSpeed = -ballYSpeed;
                    break;
                }
            }

            // Check if ball falls below the paddle
            if (ballY >= getHeight()) {
                System.out.println("Game Over!");
                System.exit(0);
            }

            repaint();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE) {
            t.start();
        }

        if (key == KeyEvent.VK_LEFT && paddleX >= 0) {
            paddleX -= 15;
        } else if (key == KeyEvent.VK_RIGHT && paddleX <= getWidth() - paddleWidth) {
            paddleX += 15;
        }
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        BrickBreaker brick = new BrickBreaker();
        t = new Thread(brick);
    }
}
