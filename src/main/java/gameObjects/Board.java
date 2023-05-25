package gameObjects;

import exception.BadSizeException;
import graph.EdgeWeightedGraph;
import graph.Position;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the board itself.
 * This class extends the EdgeWeightedGraph class because we wanted to separate these 2 classes.
 * Compared to the EdgeWeightedGraph class, this class includes a list of Barriers placed on the board.
 */
public class Board extends EdgeWeightedGraph implements Serializable {
    /**
     * List of barriers placed on the board.
     */
    private List<Barrier> barriers;

    /**
     * Constructor of the board class.
     * This class extends another class, so we can directly use the super method.
     * @param size size of the board.
     * @throws BadSizeException if the size isn't strictly greater than 0, it throws an error.
     */
    public Board(int size) throws BadSizeException {
        super(size);
        this.barriers = new ArrayList<>();
    }

    /**
     * Getter for the list of barriers.
     * @return the list of barriers.
     */
    public List<Barrier> getBarriers() {
        return this.barriers;
    }

    /**
     * Method to get the number of barriers in the list.
     * @return the number of barriers in the list.
     */
    public int getBarrierCount() {
        return this.barriers.size();
    }
}
