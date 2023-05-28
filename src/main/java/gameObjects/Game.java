package gameObjects;

import enumeration.Color;
import exception.BadNumberPlayersException;
import exception.BadPositionException;
import exception.BadSizeException;
import exception.UnknownColorException;
import graph.Position;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the whole game. It is useful to save everything we need to load a game later.
 * This class contains a board, a list of players and a turns' counter.
 */
public class Game implements Serializable {
    /**
     * Board of the game.
     */
    private Board board;

    /**
     * List of players.
     * The length of the list is either 2 or 4, because there can only be 2 or 4 players.
     */
    private List<Player> players;

    /**
     * Turns' counter.
     * It increments by 1 everytime a player does something.
     */
    private int turnCount;

    /**
     * Constructor of the class.
     * We just initialize the list of players and set the turns' counter to 0.
     */
    public Game() {
        this.players = new ArrayList<>();
        this.turnCount = 0;
    }

    /**
     * Getter for the board.
     * @return the board of the game.
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Getter for the list of players.
     * @return the list of players.
     */
    public List<Player> getPlayers() {
        return this.players;
    }

    /**
     * Getter for the turns' counter.
     * @return the turns' counter.
     */
    public int getTurnCount() {
        return this.turnCount;
    }

    /**
     * Setter for the board.
     * @param board board of the game.
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Setter for the list of players.
     * We initialize the list of players then add every player passed in the parameters.
     * @param players players of the game.
     */
    public void setPlayers(Player ...players) {
        this.players = new ArrayList<>();
        this.players.addAll(List.of(players));
    }

    /**
     * This method add 1 player to the list of players.
     * @param player player that needs to be added to the list.
     */
    public void addPlayer(Player player ) {
        this.players.add(player);
    }

    /**
     * Setter of the turns' counter.
     * @param turnCount turns' counter.
     */
    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    /**
     * This method increments the turn's counter by 1.
     */
    public void turnCountIncrement() {
        this.turnCount++;
    }

    /**
     * This method checks if a player has won the game.
     * To know if a player has won the game, we check if he has reached the other side of the board.
     * @return a Player if there is a winner, if there is none, returns null.
     * @throws UnknownColorException If the color of the player is unknown, throws an exception.
     */
    public Player checkVictory() throws UnknownColorException {
        // For every player, we check if he has reached the other side of the board
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

    /**
     * This method initializes a Game object.
     * To do this, we create a board, create the list of players, and initialize the turns' count to 0.
     * @param nbPlayers The number of players of the game, which is either 2 or 4.
     * @param size The size of the board, which is 9 by default.
     */
    public void initGame(int nbPlayers, int size) {
        try{
            // We create the board and initialize it
            this.board = new Board(size);
            this.board.initializeGraph();

            // If the number of players isn't valid, we throw an exception
            if(nbPlayers != 2 && nbPlayers != 4) throw new BadNumberPlayersException();

            int maxCoord = size-1;

            // We create the first two players, give them their position and add them to the list
            Player player1 = new Player("Yellow", Color.YELLOW);
            player1.setPosition(new Position(maxCoord/2, 0));

            Player player2 = new Player("Blue", Color.BLUE);
            player2.setPosition(new Position(maxCoord/2, maxCoord));

            this.setPlayers(player1, player2);

            // If the number of players is equal to 4, we create 2 more players and add them to the list
            if(nbPlayers == 4) {
                Player player3 = new Player("Red", Color.RED);
                player3.setPosition(new Position(0, maxCoord/2));

                Player player4 = new Player("Green", Color.GREEN);
                player4.setPosition(new Position(maxCoord, maxCoord/2));

                this.addPlayer(player3);
                this.addPlayer(player4);
            }

            // We initialize the turns' count to 0
            this.turnCount = 0;
        }
        catch (BadPositionException | BadNumberPlayersException | BadSizeException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method returns the index of the player whose turn it is to play.
     * @return the index of the player whose turn it is to play.
     */
    public int getCurrentPlayerTurn() {
        return this.turnCount % this.players.size();
    }

    @Override
    public String toString() {
        return "Game{" +
                "board=" + board +
                ", players=" + players +
                ", turnCount=" + turnCount +
                '}';
    }
}