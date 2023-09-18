public class Tile {
    private int x, y;

    Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean collide(Tile t) {
        return t.getX() == this.x && t.getY() == this.y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }
}