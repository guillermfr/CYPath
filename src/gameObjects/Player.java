package gameObjects;

import enumeration.Color;
import graph.Position;
import graph.Edge;

import java.util.List;

public class Player {
    private String name;
    private Position position;
    private Color color;

    public Player(String name, Color color) {

    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void move(Position pos, Board board, List<Player> playerList) {
        if (this.isMoveValid(pos, board, playerList)) {
            this.position.move(pos);
        }
    }

    public void placeBarrier(Edge e, Edge f, Board board) {

    }

    private boolean isMoveValid(Position destination, Board board, List<Player> playerList) {
        // Vérification de la position d'autres joueurs
        for (Player p : playerList) {
            Position playerPos = p.getPosition();
            if (destination.equals(playerPos)) return false;
        }

        // Vérification de la position des barrières TODO

        return true;
    }
    
    private boolean isBarrierValid(Edge e, Edge f, Board board) {

    }
}
