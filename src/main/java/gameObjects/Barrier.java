package gameObjects;

import graph.Edge;

import java.io.Serializable;

/**
 * Class that represents a Barrier on the board.
 * A barrier is defined by 2 edges and by the player who placed it.
 */
public class Barrier implements Serializable {
    /**
     * First edge of the barrier.
     */
    private Edge edge1;
    /**
     * Second edge of the barrier.
     */
    private Edge edge2;
    /**
     * Player who placed the barrier.
     */
    private Player placedBy;

    /**
     * Constructor method for the barrier.
     * @param e1 first edge of the barrier.
     * @param e2 second edge of the barrier.
     * @param placedBy player who placed the barrier.
     */
    public Barrier(Edge e1, Edge e2, Player placedBy) {
        this.edge1 = e1;
        this.edge2 = e2;
        this.placedBy = placedBy;
    }

    /**
     * Getter for the first edge of the barrier.
     * @return the first edge of the barrier.
     */
    public Edge getEdge1() {
        return this.edge1;
    }

    /**
     * Getter for the second edge of the barrier.
     * @return the second edge of the barrier.
     */
    public Edge getEdge2() {
        return this.edge2;
    }

    /**
     * Getter for the player who placed the barrier.
     * @return the player who placed the barrier.
     */
    public Player getPlacedBy() {
        return this.placedBy;
    }

    /**
     * Setter for the first edge of the barrier.
     * @param edge1 first edge of the barrier.
     */
    public void setEdge1(Edge edge1) {
        this.edge1 = edge1;
    }

    /**
     * Setter for the second edge of the barrier.
     * @param edge2 second edge of the barrier.
     */
    public void setEdge2(Edge edge2) {
        this.edge2 = edge2;
    }

    /**
     * Setter for the player who placed the barrier.
     * @param placedBy the player who placed the barrier
     */
    public void setPlacedBy(Player placedBy) {
        this.placedBy = placedBy;
    }
}