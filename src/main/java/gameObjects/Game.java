package gameObjects;

import enumeration.Color;
import exception.BadPositionException;
import exception.UnknownColorException;
import graph.Position;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Board board;
    private List<Player> players;
    private int turnCount;

    public Game() {
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

    public void addPlayer(Player player ) {
        this.players.add(player);
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
                    if (p.getPosition().getY() == this.board.getSize() - 1) return p;
                }

                case RED -> {
                    if (p.getPosition().getX() == this.board.getSize() - 1) return p;
                }

                case GREEN -> {
                    if (p.getPosition().getX() == 0) return p;
                }

                default -> throw new UnknownColorException("This color does not exist.");
            }
        }

        return null;
    }

    public void initGame(int nbPlayers, int size) {
        try{
            this.board = new Board(size);
            this.board.initializeGraph();

            if(nbPlayers != 2 && nbPlayers != 4) throw new Exception(); //TODO : create exception

            int maxCoord = size-1;

            Player player1 = new Player("Yellow", Color.YELLOW);
            player1.setPosition(new Position(maxCoord/2, 0));

            Player player2 = new Player("Blue", Color.BLUE);
            player2.setPosition(new Position(maxCoord/2, maxCoord));

            this.setPlayers(player1, player2);

            if(nbPlayers == 4) {
                Player player3 = new Player("Red", Color.RED);
                player3.setPosition(new Position(0, maxCoord/2));

                Player player4 = new Player("Green", Color.GREEN);
                player4.setPosition(new Position(maxCoord, maxCoord/2));

                this.addPlayer(player3);
                this.addPlayer(player4);
            }

            this.turnCount = 0;
        }
        catch (BadPositionException bpe) {
            System.out.println(bpe);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public int getCurrentPlayerTurn(int nbPlayers) {
        return this.turnCount % nbPlayers;
    }
}