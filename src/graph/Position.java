package graph;

import exception.BadPositionException;

/**
 * Position is used for two purposes: to know where a player is on the board, and to create edges for the graph that represents the board.
 */
public class Position {

    /**
     * First component of the Position.
     */
    private int x;
    /**
     * Second component of the Position.
     */
    private int y;

    /**
     * Constructor method for the Position class.
     * @param x coordinate x
     * @param y coordinate y
     * @throws BadPositionException if x or y is lesser than 0
     */
    public Position(int x, int y) throws BadPositionException {
        if(x < 0 || y < 0) {
            throw new BadPositionException();
        }
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for the x attribute.
     * @return the value of the coordinate x.
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for the y attribute.
     * @return the value of the coordinate y.
     */
    public int getY() {
        return y;
    }

    /**
     * Setter for the x attribute.
     * @param x new value for the x attribute.
     * @throws BadPositionException if the value is negative, throws an exception
     */
    public void setX(int x) throws BadPositionException {
        if(x < 0) {
            throw new BadPositionException();
        }
        this.x = x;
    }

    /**
     * Setter for the y attribute.
     * @param y new value for the y attribute.
     * @throws BadPositionException if the value is negative, throws an exception
     */
    public void setY(int y) throws BadPositionException {
        if(y < 0) {
            throw new BadPositionException();
        }
        this.y = y;
    }

    /**
     * Method for changing attributes of a Position.
     * @param x new value for the x attribute.
     * @param y new value for the y attribute.
     */
    public void move(int x, int y) {
        try {
            this.setX(x);
            this.setY(y);
        }
        catch (BadPositionException bpe) {
            System.out.println(bpe);
        }
    }

    /**
     * This method calculates the index of a Position.
     * The index can be used for the adjacency list.
     * @param boardSize size of the board.
     * @return the index of a Position.
     */
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