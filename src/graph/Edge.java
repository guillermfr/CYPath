package graph;

public class Edge {
    private Position source;
    private Position target;
    private int weight;

    public Edge(Position source, Position target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public Position getSource() {
        return this.source;
    }

    public Position getTarget() {
        return this.target;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setSource(Position source) {
        this.source = source;
    }

    public void setTarget(Position target) {
        this.target = target;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
