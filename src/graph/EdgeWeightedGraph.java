package graph;

import java.util.LinkedList;
import java.util.List;

public class EdgeWeightedGraph {

    private int size;
    private LinkedList<Edge>[] adjacencyList;

    public EdgeWeightedGraph(int size) {

        this.size = size;
        this.adjacencyList = new LinkedList[size*size];

        for (int i = 0; i<size*size; i++) {
            this.adjacencyList[i] = new LinkedList<>();
        }

    }

    public void addEdge(Edge edge) {
        adjacencyList[edge.getSource().getNumber()].add(edge);
        adjacencyList[edge.getTarget().getNumber()].add(edge);
    }

    public void initializeGraph(){

    }

    public static void main(String[] args) {

        EdgeWeightedGraph test = new EdgeWeightedGraph(9);
        System.out.println(test);

    }

}

