package gameObjects;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Board board;
    private List<Player> players;
    private int turnCount;

    public Game() {
        // Est-ce qu'on initialise direct ici ou on laisse vide et on initialise après avec des setter ?
        this.players = new ArrayList<>();
        this.turnCount = 0;
    }

    public Board getBoard() {
        return this.board;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public int getTurnCount() {
        return this.turnCount;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setPlayers(Player ...players) {
        this.players = new ArrayList<>();
        this.players.addAll(List.of(players));
    }

    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    public void turnCountIncrement() {
        this.turnCount++;
    }

    public Player checkVictory() {
        // Vérifier si un joueur est sur son côté opposé

        return null;
    }
}
