package graph;

import enumeration.Direction;
import exception.BadPositionException;
import gameObjects.Board;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static java.lang.Math.abs;

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

    /**
     * This method generates a map of Edges connected to a Position.
     * It is useful to know the neighbours of a Position.
     * @param graph graph representing the board.
     * @return a Map with every direction and the edge associated with the direction, if the direction exists.
     */
    public Map<Direction, Edge> getNeighbourEdges(EdgeWeightedGraph graph) {
        if(graph.getSize() < 2) return null;

        Map<Direction, Edge> neighbours = new HashMap<Direction, Edge>();
        int index = this.toAdjacencyListIndex(graph.getSize());

        // For every edge connected to the Position, we verify in which direction it is, and add it to the Map.
        for (Edge e : graph.getAdjacencyList()[index]) {
            if(this.getY()-1 == e.getTarget().getY()) {
                neighbours.put(Direction.NORTH, e);
            } else if(this.getY()+1 == e.getTarget().getY()) {
                neighbours.put(Direction.SOUTH, e);
            } else if(this.getX()-1 == e.getTarget().getX()) {
                neighbours.put(Direction.WEST, e);
            } else {
                neighbours.put(Direction.EAST, e);
            }
        }

        return neighbours;
    }

    /**
     * This method generates a map of Positions connected to a Position.
     * It is useful to know the neighbours of a Position.
     * We simply reuse getNeighbourEdges and get the target position of every Edge.
     * @param graph graph representing the board.
     * @return a Map with every direction and the edge associated with the direction, if the direction exists.
     */
    public Map<Direction, Position> getNeighbourPositions(EdgeWeightedGraph graph) {
        if(graph.getSize() < 2) return null;

        Map<Direction, Edge> edgeNeighbours = this.getNeighbourEdges(graph);
        Map<Direction, Position> positionNeighbours = new HashMap<Direction, Position>();

        for(Map.Entry<Direction, Edge> entry : edgeNeighbours.entrySet()) {
            positionNeighbours.put(entry.getKey(), entry.getValue().getTarget());
        }

        return positionNeighbours;
    }

    /**
     * Checks if another Position is at a certain x and y offset from current Position
     * @param otherPos the other Position to check
     * @param x        the x offset to check
     * @param y        the y offset to check
     * @return if the offset is correct
     */
    public boolean checkDistance(Position otherPos, int x, int y) {
        return abs(this.x - otherPos.getX()) == x && abs(this.y - otherPos.getY()) == y;
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
