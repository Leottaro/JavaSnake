import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 720;
        int boardHeight = boardWidth;
        int tileSize = boardWidth/20;
        int timerDelay = 100;
        double snakeWidth = 1./4;

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
