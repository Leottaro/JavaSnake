import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    final int boardWidth;
    final int boardHeight;
    final int tileSize;
    final int gridWidth;
    final int gridHeight;

    private ArrayList<Tile> snakeTiles;
    private ArrayList<Tile> snakeDirs;
    private Tile direction;
    private Tile newDir;
    private int score;
    private Tile apple;
    private Random random;
    private Timer gameTimer;
    private int timerDelay;
    private boolean gameOver;
    private double snakeWidth;

    SnakeGame(int boardWidth, int boardHeight, int tileSize) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.tileSize = tileSize;
        this.gridWidth = boardWidth / tileSize;
        this.gridHeight = boardHeight / tileSize;
        this.timerDelay = 100;
        this.snakeWidth = 1. / 4;

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        Init();
    }

    private void Init() {
        snakeTiles = new ArrayList<Tile>();
        snakeDirs = new ArrayList<Tile>();
        snakeTiles.add(new Tile(gridWidth / 2, gridHeight / 2));
        snakeDirs.add(new Tile(0, 0));
        direction = new Tile(0, 0);
        newDir = new Tile(0, 0);
        score = 4;
        apple = new Tile(-1, -1);
        random = new Random();
        placeFood();
        gameTimer = new Timer(timerDelay, this);
        gameOver = false;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        int offset = (int) (tileSize * snakeWidth);

        // Grid
        g.setColor(Color.GRAY);
        for (int x = tileSize; x < boardHeight; x += tileSize) {
            g.drawLine(x, 0, x, boardHeight);
        }
        for (int y = tileSize; y < boardWidth; y += tileSize) {
            g.drawLine(0, y, boardWidth, y);
        }

        // Apple
        g.setColor(Color.RED);
        g.fillRect(apple.getX() * tileSize + offset, apple.getY() * tileSize + offset, tileSize - offset * 2,
                tileSize - offset * 2);

        // Snake
        g.setColor(Color.GREEN);
        for (int i = 0; i < snakeTiles.size(); i++) {
            int x = snakeTiles.get(i).getX() * tileSize + offset;
            int y = snakeTiles.get(i).getY() * tileSize + offset;
            int width = tileSize - offset * 2;
            int height = tileSize - offset * 2;
            g.fillRect(x, y, width, height);
            if (i != 0) {
                g.fillRect(x + offset * snakeDirs.get(i - 1).getX(), y + offset * snakeDirs.get(i - 1).getY(),
                        width, height);
            }
            if (i != snakeTiles.size() - 1) {
                g.fillRect(x - offset * snakeDirs.get(i).getX(), y - offset * snakeDirs.get(i).getY(), width, height);
            }
        }

        // Score
        g.setFont(new Font("Lucida Grande", 0, tileSize / 2));
        if (gameOver)
            g.setColor(Color.RED);
        else
            g.setColor(Color.WHITE);
        g.drawString(String.format("Score: %d", score), offset, g.getFontMetrics().getHeight() + offset);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tick();
        repaint();
    }

    public void tick() {
        if (gameOver || (newDir.getX() == 0 && newDir.getY() == 0))
            return;
        direction.setCoords(newDir.getX(), newDir.getY());

        // MOVE
        int newX = snakeTiles.get(0).getX() + direction.getX();
        int newY = snakeTiles.get(0).getY() + direction.getY();

        snakeTiles.add(0, new Tile(newX, newY));
        snakeDirs.add(0, new Tile(direction.getX(), direction.getY()));
        if (snakeTiles.size() > score) {
            snakeTiles.remove(score);
            snakeDirs.remove(score);
        }

        // APPLE COLLISION
        if (snakeTiles.get(0).collide(apple)) {
            score++;
            placeFood();
        }

        // DEATH
        if (newX < 0 || newX >= gridWidth || newY < 0 || newY >= gridHeight) {
            gameOver = true;
        } else {
            for (int i = 1; i < snakeTiles.size(); i++)
                if (snakeTiles.get(0).collide(snakeTiles.get(i)))
                    gameOver = true;
        }

        if (gameOver) {
            Pause();
            System.out.println("GAMEOVER");
        }
    }

    public void placeFood() {
        int newX, newY;
        boolean isValid;
        do {
            isValid = true;
            newX = random.nextInt(gridWidth);
            newY = random.nextInt(gridHeight);

            for (Tile tile : snakeTiles) {
                if (tile.getX() == newX && tile.getY() == newY) {
                    isValid = false;
                    break;
                }
            }
        } while (!isValid);
        apple.setCoords(newX, newY);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 90: // Z
            case 87: // W
            case 38: // UP
                if (direction.getY() != 1)
                    newDir.setCoords(0, -1);
                break;
            case 83: // S
            case 40: // DOWN
                if (direction.getY() != -1)
                    newDir.setCoords(0, 1);
                break;
            case 81: // Q
            case 65: // A
            case 37: // LEFT
                if (direction.getX() != 1)
                    newDir.setCoords(-1, 0);
                break;
            case 68: // D
            case 39: // RIGHT
                if (direction.getX() != -1)
                    newDir.setCoords(1, 0);
                break;
            case 10: // ENTER
            case 8: // BACKSPACE
            case 32: // SPACE
            case 27: // ESCAPE
                if (gameOver) {
                    Init();
                    gameTimer.start();
                }
                break;
            default:
                break;
        }
    }

    // USER METODS
    public int getScore() {
        return score;
    }

    public Timer getGameTimer() {
        return gameTimer;
    }

    public void start() {
        gameTimer.start();
    }

    public void Pause() {
        gameTimer.stop();
    }

    public int getTimerDelay() {
        return timerDelay;
    }

    public void setTimerDelay(int timerDelay) {
        this.timerDelay = timerDelay;
        this.gameTimer.setDelay(timerDelay);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public double getSnakeWidth() {
        return snakeWidth;
    }

    public void setSnakeWidth(double snakeWidth) {
        assert snakeWidth >= 0 && snakeWidth < 1. : "snakeWidth must be a percentage";
        this.snakeWidth = snakeWidth;
    }

    // PAS BESOIN
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
