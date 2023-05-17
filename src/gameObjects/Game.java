package gameObjects;

import exception.UnknownColorException;

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

    public Player checkVictory() throws UnknownColorException {
        for (Player p : players) {
            switch (p.getColor()) {
                case BLUE -> {
                    if (p.getPosition().getY() == 0) return p;
                }

                case YELLOW -> {
                    if (p.getPosition().getY() == this.board.getSize()) return p;
                }

                case RED -> {
                    if (p.getPosition().getX() == 0) return p;
                }

                case GREEN -> {
                    if (p.getPosition().getX() == this.board.getSize()) return p;
                }

                default -> throw new UnknownColorException("This color does not exist.");
            }
        }

        return null;
    }
}
