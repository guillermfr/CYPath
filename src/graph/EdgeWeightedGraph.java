package graph;

import enumeration.Color;
import enumeration.Direction;
import exception.BadPositionException;
import exception.BadSizeException;
import exception.BadWeightException;
import exception.UnknownColorException;
import gameObjects.Player;

import java.util.*;

/**
 * EdgeWeightedGraph represents the board of the game.
 * It is an edge weighted graph because we need to save the barriers, so we will change the weight of edges with a barrier.
 */
public class EdgeWeightedGraph {

    /**
     * Size of the graph.
     * The size represents the number of rows/columns of the board.
     * So there are size*size nodes in the graph.
     */
    private final int size;
    /**
     * Adjacency list of the graph.
     */
    private LinkedList<Edge>[] adjacencyList;

    /**
     * Constructor method for the EdgeWeightedGraph class.
     * It creates an array of LinkedList of length depending on the size value.
     * @param size number of rows/columns.
     * @throws BadSizeException if the size isn't strictly greater than 0, it throws an error.
     */
    public EdgeWeightedGraph(int size) throws BadSizeException {

        if(size < 0) {
            throw new BadSizeException();
        }

        this.size = size;
        this.adjacencyList = new LinkedList[size*size];

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
        for (int y = 0; y < this.size; y++) {
            for (int x = 0; x < this.size; x++) {
                try {
                    if(y > 0) {
                        addEdge(new Edge(new Position(x,y),new Position(x,y-1),0));
                    }
                    if(y < this.size - 1) {
                        addEdge(new Edge(new Position(x,y),new Position(x,y+1),0));
                    }
                    if(x > 0) {
                        addEdge(new Edge(new Position(x,y),new Position(x-1,y),0));
                    }
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
        if(!visited[pos.toAdjacencyListIndex(this.size)]) {
            switch (color) {
                case BLUE -> {
                    if (pos.getY() == 0) reached[0] = true;
                }

                case YELLOW -> {
                    if (pos.getY() == this.size) reached[0] = true;
                }

                case RED -> {
                    if (pos.getX() == 0) reached[0] = true;
                }

                case GREEN -> {
                    if (pos.getX() == this.size) reached[0] = true;
                }

                default -> {
                    throw new UnknownColorException("Unknown color");
                }
            }
            visited[pos.toAdjacencyListIndex(this.size)] = true;

            Map<Direction, Edge> edgeNeighbours = pos.getNeighbourEdges(this);

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
        Map<Player, Boolean> checkPathPlayers = new HashMap<Player, Boolean>();

        for (Player p : players) {
            checkPathPlayers.put(p, this.DFS(p.getPosition(), p.getColor()));
        }

        return checkPathPlayers;
    }

    @Override
    public String toString() {
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < this.size*this.size; i++) {
            display.append(i).append(" : ").append(this.adjacencyList[i]).append("\n");
        }
        return display.toString();
    }

    /**
     * Method displaying the graph.
     * The display is really simple and is just useful for tests.
     */
    public void displayGraph() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if(i == 0) {
                    System.out.print(" ");
                }
                try {
                    System.out.print((new Position(i,j)).toAdjacencyListIndex(this.size));
                }
                catch (BadPositionException bpe) {
                    System.out.println(bpe);
                }
                if(j != this.size - 1) {
                    System.out.print("-");
                }
            }
            System.out.println();
            if(i != this.size-1) {
                for (int k = 0; k < this.size; k++) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        try {
            EdgeWeightedGraph test = new EdgeWeightedGraph(4);
            test.initializeGraph();
            System.out.println(test);

            System.out.println(test.DFS(new Position(1,1), Color.BLUE));
        }
        catch (BadSizeException | BadPositionException exception) {
            System.out.println(exception);
        }
    }

}