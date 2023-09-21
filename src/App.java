import java.awt.Toolkit;
import javax.swing.JFrame;

public class App {
    public static final int tileNumber = 15;
    public static final int timerDelay = 100;
    public static final double snakeWidth = 1. / 4;

    public static void main(String[] args) throws Exception {
        int boardHeight = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.75);
        boardHeight -= boardHeight % tileNumber;
        int boardWidth = boardHeight;
        int tileSize = boardHeight / tileNumber;

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGame Game = new SnakeGame(boardWidth, boardHeight, tileSize);
        Game.setTimerDelay(timerDelay);
        Game.setSnakeWidth(snakeWidth);
        Game.start();
        frame.add(Game);
        frame.pack();
        Game.requestFocus();
    }
}