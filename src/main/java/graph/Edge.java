package graph;

import enumeration.Direction;
import exception.BadWeightException;
import gameObjects.Board;

/**
 * Edge is used for the adjacency list. It represents a link between two Positions.
 * This class also has a weight. This will be useful to represent barriers.
 */
public class Edge {
    /**
     * Source of the edge.
     */
    private Position source;
    /**
     * Target of the edge.
     */
    private Position target;
    /**
     * Weight of the edge.
     */
    private int weight;

    /**
     * Constructor method for the Edge class.
     * It takes 2 Position parameters and a weight.
     * @param source source of the edge
     * @param target target of the edge
     * @param weight weight of the edge
     * @throws BadWeightException if the value is negative, throws an exception.
     */
    public Edge(Position source, Position target, int weight) throws BadWeightException {
        if(weight < 0) {
            throw new BadWeightException();
        }
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    /**
     * Getter method for the source.
     * @return the source of the Edge.
     */
    public Position getSource() {
        return this.source;
    }

    /**
     * Getter method for the target.
     * @return the target of the Edge.
     */
    public Position getTarget() {
        return this.target;
    }

    /**
     * Getter method for the weight.
     * @return the weight of the Edge.
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * Setter method for the source.
     * @param source Position object that represent the source of the edge.
     */
    public void setSource(Position source) {
        this.source = source;
    }

    /**
     * Setter method for the target.
     * @param target Position object that represent the target of the edge.
     */
    public void setTarget(Position target) {
        this.target = target;
    }

    /**
     * Setter method for the weight.
     * @param weight Position object that represent the weight of the edge.
     * @throws BadWeightException if the value is negative, throws an exception.
     */
    public void setWeight(int weight) throws BadWeightException {
        if(weight < 0) {
            throw new BadWeightException();
        }
        this.weight = weight;
    }

    /**
     * Takes an edge and a list of Position to write in, and write the Positions so that the smallest in terms of adjacency list index is the first element, and the biggest the second
     * @param positions list in which we write
     * @param board     game board
     * @return the adjacency list index of the smallest among the two positions
     */
    public int normalizeEdgePositions(Position[] positions, Board board) {
        int index1 = this.getSource().toAdjacencyListIndex(board.getSize());
        int index2 = this.getTarget().toAdjacencyListIndex(board.getSize());

        if (index1 < index2) {
            positions[0] = this.getSource();
            positions[1] = this.getTarget();
            return index1;
        } else {
            positions[0] = this.getTarget();
            positions[1] = this.getSource();
            return index2;
        }
    }

    /**
     * Sets the weight of an edge and of its opposite
     * @param weight    weight to set to the edge
     * @param board     game board
     * @throws BadWeightException if the value is negative, throws an exception.
     */
    public void setBidirectionalEdgeWeight(int weight, Board board) throws BadWeightException {
        if (weight < 0) {
            throw new BadWeightException();
        }

        boolean isBarrierHorizontal = this.getTarget().checkDistance(this.getSource(), 0, 1);
        this.weight = weight;
        this.getTarget().getNeighbourEdges(board).get(isBarrierHorizontal ? Direction.NORTH : Direction.WEST).setWeight(weight);
    }

    @Override
    public String toString() {
        return "Edge{" +
                source + "-" + target +
                ", weight=" + weight +
                '}';
    }
}
