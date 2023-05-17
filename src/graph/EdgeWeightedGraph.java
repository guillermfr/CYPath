package graph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class EdgeWeightedGraph {

    private final int size;
    private LinkedList<Edge>[] adjacencyList;

    public EdgeWeightedGraph(int size) {

        this.size = size;
        this.adjacencyList = new LinkedList[size*size];

        for (int i = 0; i<size*size; i++) {
            this.adjacencyList[i] = new LinkedList<>();
        }

    }

    public int getSize() {
        return size;
    }

    public LinkedList<Edge>[] getAdjacencyList() {
        return adjacencyList;
    }

    public void addEdge(Edge edge) {
        adjacencyList[edge.getSource().getNumber(this.size)].add(edge);
    }

    public void initializeGraph(){
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if(i > 0) {
                    addEdge(new Edge(new Position(i,j),new Position(i-1,j),0));
                }
                if(i < this.size - 1) {
                    addEdge(new Edge(new Position(i,j),new Position(i+1,j),0));
                }
                if(j > 0) {
                    addEdge(new Edge(new Position(i,j),new Position(i,j-1),0));
                }
                if(j < this.size - 1) {
                    addEdge(new Edge(new Position(i,j),new Position(i,j+1),0));
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < this.size*this.size; i++) {
            display.append(i).append(" : ").append(this.adjacencyList[i]).append("\n");
        }
        return display.toString();
    }

    public void displayGraph() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if(i == 0) {
                    System.out.print(" ");
                }
                System.out.print((new Position(i,j)).getNumber(this.size));
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

}

