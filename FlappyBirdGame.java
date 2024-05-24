import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlappyBirdGame extends JFrame implements KeyListener {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int GROUND_HEIGHT = 50;
    private final int OBSTACLE_WIDTH = 50;
    private final int OBSTACLE_GAP = 260;
    private final int OBSTACLE_SPEED = 5;
    private final int GRAVITY = 1;
    private final int JUMP_POWER = -15;
    private final int BIRD_WIDTH = 50;  // Scaled bird width
    private final int BIRD_HEIGHT = 50; // Scaled bird height

    private int birdY;
    private int birdVelocity;
    private boolean isGameOver;
    private int score;
    private List<Rectangle> obstacles;
    private Image birdImage;

    public FlappyBirdGame() {
        setTitle("Flappy Bird");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        birdY = HEIGHT / 2;
        birdVelocity = 0;
        isGameOver = false;
        score = 0;
        obstacles = new ArrayList<>();

        // Load and scale the bird image
        ImageIcon birdIcon = new ImageIcon("C:\\Users\\Jay\\Pictures\\flappy bird img.png");
        birdImage = birdIcon.getImage().getScaledInstance(BIRD_WIDTH, BIRD_HEIGHT, Image.SCALE_SMOOTH);

        addKeyListener(this);
        setFocusable(true);

        Timer timer = new Timer(20, e -> {
            if (!isGameOver) {
                update();
                repaint();
            }
        });
        timer.start();

        generateInitialObstacles();
    }
    
    private void generateObstacles1() {
        Random random = new Random();
        int gapPosition = random.nextInt(HEIGHT - GROUND_HEIGHT - OBSTACLE_GAP);
        obstacles.add(new Rectangle(WIDTH, 0, OBSTACLE_WIDTH, gapPosition));
        obstacles.add(new Rectangle(WIDTH, gapPosition + OBSTACLE_GAP + GROUND_HEIGHT, OBSTACLE_WIDTH, HEIGHT - gapPosition - OBSTACLE_GAP - GROUND_HEIGHT));
    }

    private void update() {
        birdVelocity += GRAVITY;
        birdY += birdVelocity;

        List<Rectangle> obstaclesToRemove = new ArrayList<>();
        for (Rectangle obstacle : obstacles) {
            obstacle.x -= OBSTACLE_SPEED;
            if (obstacle.intersects(new Rectangle(WIDTH / 2 - BIRD_WIDTH / 2, birdY, BIRD_WIDTH, BIRD_HEIGHT))) {
                isGameOver = true;
            }
            if (obstacle.x + OBSTACLE_WIDTH < 0) {
                obstaclesToRemove.add(obstacle);
                if (obstacle.y == 0) {
                    score++;
                }
            }
        }
        obstacles.removeAll(obstaclesToRemove);

        // Add new obstacles when the last obstacle has moved past a certain point
        if (obstacles.isEmpty() || obstacles.get(obstacles.size() - 1).x < WIDTH - 400) {
            generateObstacles1();
        }

        if (birdY > HEIGHT - GROUND_HEIGHT || birdY < 0) {
            isGameOver = true;
        }
    }

    private void generateInitialObstacles() {
        for (int i = 0; i < 4; i++) {
            generateObstacles1();
        }
    }

    private void generateObstacles() {
        Random random = new Random();
        int gapPosition = random.nextInt(HEIGHT - GROUND_HEIGHT - OBSTACLE_GAP);
        obstacles.add(new Rectangle(WIDTH, 0, OBSTACLE_WIDTH, gapPosition));
        obstacles.add(new Rectangle(WIDTH, gapPosition + OBSTACLE_GAP, OBSTACLE_WIDTH, HEIGHT - gapPosition - OBSTACLE_GAP - GROUND_HEIGHT));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.CYAN);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.GREEN);
        for (Rectangle obstacle : obstacles) {
            g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }

        // Draw the scaled bird image
        g.drawImage(birdImage, WIDTH / 2 - BIRD_WIDTH / 2, birdY, this);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        // Draw score at bottom right
        String scoreText = "Score: " + score;
        FontMetrics metrics = g.getFontMetrics();
        int x = WIDTH - metrics.stringWidth(scoreText) - 20;
        int y = HEIGHT - 20;
        g.drawString(scoreText, x, y);

        if (isGameOver) {
            g.drawString("Game Over!", WIDTH / 2 - 100, HEIGHT / 2);
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            birdVelocity = JUMP_POWER;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlappyBirdGame game = new FlappyBirdGame();
            game.setVisible(true);
        });
    }
}
