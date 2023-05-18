package graph;

import enumeration.Direction;
import exception.BadPositionException;
import exception.BadSizeException;
import exception.BadWeightException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

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

    private void DFSUtil(Position pos, boolean[] visited) {
        if(!visited[pos.toAdjacencyListIndex(this.size)]) {
            visited[pos.toAdjacencyListIndex(this.size)] = true;
            System.out.println(pos.toAdjacencyListIndex(this.size));

            Map<Direction, Position> neighbours = pos.getNeighbourPositions(this);

            for(Map.Entry<Direction, Position> entry : neighbours.entrySet()) {
                System.out.println(entry.getKey());
                this.DFSUtil(entry.getValue(), visited);
            }
        }
    }

    public void DFS(Position pos) {
        boolean[] visited = new boolean[this.size*this.size];

        this.DFSUtil(pos, visited);
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

            test.DFS(new Position(0,0));
        }
        catch (BadSizeException | BadPositionException exception) {
            System.out.println(exception);
        }
    }

}