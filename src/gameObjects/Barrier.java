package gameObjects;

import graph.Edge;

public class Barrier {
    private Edge edge1;
    private Edge edge2;
    private Player placedBy;

    public Barrier(Edge e1, Edge e2, Player placedBy) {
        this.edge1 = e1;
        this.edge2 = e2;
        this.placedBy = placedBy;
    }

    public Edge getEdge1() {
        return this.edge1;
    }

    public Edge getEdge2() {
        return this.edge2;
    }

    public Player getPlacedBy() {
        return this.placedBy;
    }

    public void setEdge1(Edge edge1) {
        this.edge1 = edge1;
    }

    public void setEdge2(Edge edge2) {
        this.edge2 = edge2;
    }

    public void setPlacedBy(Player placedBy) {
        this.placedBy = placedBy;
    }
}
