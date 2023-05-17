package graph;

public class Position {

    private int x;
    private int y;
    private boolean occupied;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.occupied = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public void move(Position newPos) {
        this.x = newPos.x;
        this.y = newPos.y;
    }

    public int getNumber(int size) {
        return (this.x)*size + this.y;
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
