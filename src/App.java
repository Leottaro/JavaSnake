import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 720;
        int boardHeight = boardWidth;
        int tileSize = boardWidth/10;
        int timerDelay = 100;

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        SnakeGame Game = new SnakeGame(boardWidth, boardHeight, tileSize, timerDelay);
        frame.add(Game);
        frame.pack();
        Game.requestFocus();
    }
}
