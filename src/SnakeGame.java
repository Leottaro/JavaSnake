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
    final int timerDelay;

    ArrayList<Tile> snakeTiles;
    ArrayList<Tile> snakeDirs;
    Tile direction;
    Tile newDir;
    int snakeSize = 4;
    Tile apple;
    Random random;
    Timer gameTimer;
    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight, int tileSize, int timerDelay) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.tileSize = tileSize;
        this.gridWidth = boardWidth / tileSize;
        this.gridHeight = boardHeight / tileSize;
        this.timerDelay = timerDelay;

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snakeTiles = new ArrayList<Tile>();
        snakeDirs = new ArrayList<Tile>();
        snakeTiles.add(new Tile(gridWidth / 2, gridHeight / 2));
        snakeDirs.add(new Tile(0, 0));
        direction = new Tile(0, 0);
        newDir = new Tile(0, 0);
        apple = new Tile(-1, -1);
        random = new Random();
        placeFood();
        gameTimer = new Timer(timerDelay, this);
        gameTimer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        int offset = tileSize / 4;

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
        if (gameOver)
            g.setColor(Color.RED);
        else
            g.setColor(Color.GREEN);
        g.setFont(new Font("Lucida Grande", 0, tileSize/2));
        g.drawString(String.format("Score: %d", snakeSize - 4), offset, g.getFontMetrics().getHeight() + offset);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tick();
        repaint();
    }

    public void tick() {
        if (newDir.getX() == 0 && newDir.getY() == 0)
            return;
        direction.setCoords(newDir.getX(), newDir.getY());

        // MOVE
        int newX = snakeTiles.get(0).getX() + direction.getX();
        int newY = snakeTiles.get(0).getY() + direction.getY();

        snakeTiles.add(0, new Tile(newX, newY));
        snakeDirs.add(0, new Tile(direction.getX(), direction.getY()));
        if (snakeTiles.size() > snakeSize) {
            snakeTiles.remove(snakeSize);
            snakeDirs.remove(snakeSize);
        }

        // APPLE COLLISION
        if (snakeTiles.get(0).collide(apple)) {
            snakeSize++;
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
            gameTimer.setRepeats(false);
            gameTimer.stop();
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
            default:
                break;
        }
    }

    // PAS BESOIN
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
