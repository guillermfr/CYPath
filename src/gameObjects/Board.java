package gameObjects;

import graph.EdgeWeightedGraph;
import graph.Position;

import java.util.ArrayList;
import java.util.List;


public class Board extends EdgeWeightedGraph {
    private List<Barrier> barriers;

    public Board(int size) {
        super (size);
        this.barriers = new ArrayList<>();
    }

    public Position getPosition() {
        // du coup on sait pas ce que ça fait
        return null;
    }

    public List<Barrier> getBarriers() {
        return this.barriers;
    }

    public int getBarrierCount() {
        return this.barriers.size();
    }
}
