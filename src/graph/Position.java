package graph;

public class Position {

    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
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

    public void move(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public int toAdjacencyListIndex(int boardSize) {
        return this.y * boardSize + this.x;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position pos) {
            return pos.getX() == this.getX() && pos.getY() == this.getY();
        }

        return false;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
