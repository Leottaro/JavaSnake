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
    final int gridWidth;
    final int gridHeight;
    final int tileSize = 24;

    Tile apple;
    Random random;

    Tile direction;
    Tile newDir;
    boolean gameOver = false;
    Timer gameTimer;
    ArrayList<Tile> snakeTiles;
    int snakeSize = 4;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.gridWidth = boardWidth / tileSize;
        this.gridHeight = boardHeight / tileSize;

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snakeTiles = new ArrayList<Tile>();
        snakeTiles.add(new Tile(gridWidth / 2, gridHeight / 2));

        direction = new Tile(0, 0);
        newDir = new Tile(0, 0);
        apple = new Tile(-1, -1);
        random = new Random();
        placeFood();

        gameTimer = new Timer(100, this);
        gameTimer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Score
        g.setColor(Color.GREEN);
        g.drawString(String.format("Score: %d", snakeSize - 4), tileSize / 2, g.getFontMetrics().getHeight());

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
        g.fillRect(apple.getX() * tileSize, apple.getY() * tileSize, tileSize, tileSize);

        // Snake
        g.setColor(Color.GREEN);
        snakeTiles.forEach(tile -> g.fillRect(tile.getX() * tileSize, tile.getY() * tileSize, tileSize, tileSize));
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
        if (newX < 0 || newX >= gridWidth || newY < 0 || newY >= gridHeight)
            gameOver = true;

        snakeTiles.add(0, new Tile(newX, newY));
        if (snakeTiles.size() > snakeSize)
            snakeTiles.remove(snakeSize);

        // APPLE COLLISION
        if (snakeTiles.get(0).collide(apple)) {
            snakeSize++;
            placeFood();
        }

        // DEATH
        for (int i = 1; i < snakeTiles.size(); i++)
            if (snakeTiles.get(i).getX() == newX && snakeTiles.get(i).getY() == newY)
                gameOver = true;

        if (gameOver) {
            gameTimer.setRepeats(false);
            gameTimer.stop();
            System.out.println("GAMEOVER");
        }
    }

    public void placeFood() {
        boolean isValid = false;
        int newX = -1, newY = -1;
        while (!isValid) {
            newX = random.nextInt(gridWidth);
            newY = random.nextInt(gridHeight);

            for (Tile tile : snakeTiles) {
                if (tile.getX() != newX && tile.getY() != newY) {
                    isValid = true;
                    break;
                }
            }
        }
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
