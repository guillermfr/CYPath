package graph;

import enumeration.Color;
import enumeration.Direction;
import exception.BadPositionException;
import exception.BadSizeException;
import exception.BadWeightException;
import exception.UnknownColorException;
import gameObjects.Player;

import java.io.Serializable;
import java.util.*;

/**
 * EdgeWeightedGraph represents the board of the game.
 * It is an edge weighted graph because we need to save the barriers, so we will change the weight of edges with a barrier.
 */
public class EdgeWeightedGraph implements Serializable {

    /**
     * Size of the graph.
     * The size represents the number of rows/columns of the board.
     * So there are size*size nodes in the graph.
     */
    private final int size;
    /**
     * Adjacency list of the graph.
     */
    private final LinkedList<Edge>[] adjacencyList;

    /**
     * Constructor method for the EdgeWeightedGraph class.
     * It creates an array of LinkedList of length depending on the size value.
     * @param size number of rows/columns.
     * @throws BadSizeException if the size isn't strictly greater than 0, it throws an error.
     */
    @SuppressWarnings("unchecked")
    public EdgeWeightedGraph(int size) throws BadSizeException {

        if(size < 0) {
            throw new BadSizeException();
        }

        this.size = size;
        this.adjacencyList = (LinkedList<Edge>[]) new LinkedList[size*size];

        for (int i = 0; i<size*size; i++) {
            this.adjacencyList[i] = new LinkedList<>();
        }

    }

    /**
     * Getter method for the size.
     * @return the size of the EdgeWeightedGraph.
     */
    public int getSize() {
        return size;
    }

    /**
     * Getter method for the adjacency list.
     * @return the adjacencyList of the EdgeWeightedGraph.
     */
    public LinkedList<Edge>[] getAdjacencyList() {
        return adjacencyList;
    }

    /**
     * Method adding an edge to the adjacency list.
     * It takes and edge, and add it to the corresponding source in the adjacency list.
     * @param edge the edge added to the adjacency list.
     */
    public void addEdge(Edge edge) {
        adjacencyList[edge.getSource().toAdjacencyListIndex(this.size)].add(edge);
    }

    /**
     * Method initializing the graph depending on its size.
     * It creates a graph where every node is connected to nodes on its right, left, top and bottom.
     */
    public void initializeGraph(){
        // For every node, we verify their "position" on the graph
        for (int y = 0; y < this.size; y++) {
            for (int x = 0; x < this.size; x++) {
                try {
                    // If the node isn't on the first line, then it has a node connected to it above
                    if(y > 0) {
                        addEdge(new Edge(new Position(x,y),new Position(x,y-1),0));
                    }
                    // If the node isn't on the last line, then it has a node connected to it below
                    if(y < this.size - 1) {
                        addEdge(new Edge(new Position(x,y),new Position(x,y+1),0));
                    }
                    // If the node isn't on the first column, then it has a node connected to it to its left
                    if(x > 0) {
                        addEdge(new Edge(new Position(x,y),new Position(x-1,y),0));
                    }
                    // If the node isn't on the last column, then it has a node connected to it to its right
                    if(x < this.size - 1) {
                        addEdge(new Edge(new Position(x,y),new Position(x+1,y),0));
                    }
                }
                catch (BadPositionException | BadWeightException exception) {
                    System.out.println(exception);
                }
            }
        }
    }

    /**
     * Main method for the DFS.
     * It is a recursive method that explores every Position to see if a player can reach the side he needs to go to.
     * @param pos Current position.
     * @param visited Array of boolean that marks visited Positions.
     * @param color Color of the current player.
     * @param reached Boolean which is true if the side is reachable.
     * @throws UnknownColorException if the color isn't valid, throws an exception
     */
    private void DFSUtil(Position pos, boolean[] visited, Color color, boolean[] reached) throws UnknownColorException {
        // If the node isn't visited, we need to visit it
        if(!visited[pos.toAdjacencyListIndex(this.size)]) {
            // Depending on the color, we check if every player has cleared path (with no barrier) to the side they need to reach
            switch (color) {
                case BLUE -> {
                    if (pos.getY() == 0) reached[0] = true;
                }

                case YELLOW -> {
                    if (pos.getY() == this.size - 1) reached[0] = true;
                }

                case GREEN -> {
                    if (pos.getX() == 0) reached[0] = true;
                }

                case RED -> {
                    if (pos.getX() == this.size - 1) reached[0] = true;
                }

                default -> throw new UnknownColorException("Unknown color");
            }

            // We mark that the current node has been visited
            visited[pos.toAdjacencyListIndex(this.size)] = true;

            // We get the neighbour edges of the current node
            Map<Direction, Edge> edgeNeighbours = pos.getNeighbourEdges(this);

            // We visit every neighbour nodes if there is no barrier between the two nodes
            for(Map.Entry<Direction, Edge> entry : edgeNeighbours.entrySet()) {
                if(entry.getValue().getWeight() == 0) {
                    this.DFSUtil(entry.getValue().getTarget(), visited, color, reached);
                }
            }
        }
    }

    /**
     * Intermediate method for the DFS.
     * It creates an array to mark visited Positions.
     * @param pos Position of the current player.
     * @param color Color of the current player.
     * @return true if the player has a path, false if not.
     */
    public boolean DFS(Position pos, Color color) {
        // Creation of the array of visited nodes and the boolean to know if the side can be reached or not
        boolean[] visited = new boolean[this.size*this.size];
        boolean[] reached = new boolean[1];

        try {
            this.DFSUtil(pos, visited, color, reached);
        }
        catch (UnknownColorException uce) {
            System.out.println(uce);
        }

        return reached[0];
    }

    /**
     * This method checks if every player has a path to the side he needs to go to.
     * To do that, we do a Depth First Search starting from the Position of every player.
     * @param players the list of players.
     * @return a map with every player and a boolean which is true if the corresponding player has a path.
     */
    public Map<Player, Boolean> checkPath(List<Player> players) {
        // We create a map with players as keys and boolean as values
        Map<Player, Boolean> checkPathPlayers = new HashMap<>();

        // For every player, we check if there is a path between his position and the side he needs to reach
        for (Player p : players) {
            checkPathPlayers.put(p, this.DFS(p.getPosition(), p.getColor()));
        }

        return checkPathPlayers;
    }
    /**
     * Returns a string representation of the adjacency list for the graph.
     * @return A string representation of the adjacency list, displaying each vertex and its adjacent vertices.
     */
    @Override
    public String toString() {
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < this.size*this.size; i++) {
            display.append(i).append(" : ").append(this.adjacencyList[i]).append("\n");
        }
        return display.toString();
    }
}