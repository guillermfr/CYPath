package gameObjects;

import enumeration.Color;
import enumeration.Direction;
import exception.BadDistanceException;
import exception.UnknownDirectionException;
import exception.BadPositionException;
import graph.Position;
import graph.Edge;

import java.util.List;
import java.util.Map;

public class Player {
    private String name;
    private Position position;
    private Color color;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
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

    public void move(int x, int y, Board board, List<Player> playerList) throws Exception {
        if (this.isMoveValid(x, y, board, playerList)) {
            this.position.move(x, y);
        }
    }

    public void placeBarrier(Edge e, Edge f, Board board) {

    }

    /**
     * Checks if the move to (x ; y) is valid
     * @param x             x coordinate of the destination
     * @param y             y coordinate of the destination
     * @param board         game board
     * @param playerList    list of all players on the board
     * @return if the move is valid
     * @throws UnknownDirectionException if direction is not a valid direction
     * @throws BadPositionException if x or y is lesser than 0
     */
    private boolean isMoveValid(int x, int y, Board board, List<Player> playerList) throws Exception {
        Position playerPos = this.position;
        int playerX = playerPos.getX();
        int playerY = playerPos.getY();

        if (isCellOccupied(x, y, playerList)) return false;

        // Verifying where the user wants to go first, then checking if the requirements are met to avoid iterating through the user list on every iteration
        // If the player wants to move diagonal: there has to be a player to jump over, and another behind, with no barriers stopping him
        // If player wants to go north-west
        if (x + 1 == playerX && y + 1 == playerY) {
            return isDiagonalValid(Direction.NORTH, Direction.WEST, playerList, board);
        }

        // If player wants to go north-east
        if (x - 1 == playerX && y + 1 == playerY) {
            return isDiagonalValid(Direction.NORTH, Direction.EAST, playerList, board);
        }

        // If player wants to go south-east
        if (x - 1 == playerX && y - 1 == playerY) {
            return isDiagonalValid(Direction.SOUTH, Direction.EAST, playerList, board);
        }

        // If player wants to go south-west
        if (x + 1 == playerX && y - 1 == playerY) {
            return isDiagonalValid(Direction.SOUTH, Direction.WEST, playerList, board);
        }

        // If the player wants to move 2 cells in any direction: there has to be a player to jump over and no barriers to stop the player
        // If player wants to go north
        if (x == playerX && y + 2 == playerY) {
            return isJumpingMoveValid(Direction.NORTH, playerList, board);
        }

        // If player wants to go east
        if (x - 2 == playerX && y == playerY) {
            return isJumpingMoveValid(Direction.WEST, playerList, board);
        }

        // If player wants to go south
        if (x == playerX && y - 2 == playerY) {
            return isJumpingMoveValid(Direction.SOUTH, playerList, board);
        }

        // If player wants to go west
        if (x + 2 == playerX && y == playerY) {
            return isJumpingMoveValid(Direction.EAST, playerList, board);
        }

        // If the player wants to move by a single adjacent cell
        return checkAllRegularMoves(x, y, board);
    }

    /**
     * Checks if cell at (x ; y) is occupied
     * @param x             x coordinate of cell
     * @param y             y coordinate of cell
     * @param playerList    list of all players on the board
     * @return if the cell is occupied
     */
    private boolean isCellOccupied(int x, int y, List<Player> playerList) {
        for (Player p : playerList) {
            if (p.getPosition().getX() == x && p.getPosition().getY() == y) return true;
        }

        return false;
    }

    /**
     * Checks if a direction is filled with players for a distance
     * @param direction     direction from the player to check
     * @param distance      the amount of cells to check from the player
     * @param playerList    list of all players to include in the check
     * @return the presence of players in all the checked cells in the given direction
     * @throws BadDistanceException if the distance is lesser than 1
     * @throws UnknownDirectionException if the given direction doesn't exist
     */
    private boolean checkDirection(Direction direction, int distance, List<Player> playerList) throws Exception {
        if (distance < 1) throw new BadDistanceException("Distance has to be at least 1");

        int x = this.position.getX();
        int y = this.position.getY();

        switch (direction) {
            case NORTH -> {
                int checks = 0;

                for (Player p : playerList) {
                    for (int i = 1; i <= distance ; i++) {
                        if (p.getPosition().getX() == x && p.getPosition().getY() + i == y) checks++;
                    }
                }

                return checks == distance;
            }

            case WEST -> {
                int checks = 0;

                for (Player p : playerList) {
                    for (int i = 1; i <= distance ; i++) {
                        if (p.getPosition().getX() + i == x && p.getPosition().getY() == y) checks++;
                    }
                }

                return checks == distance;
            }

            case SOUTH -> {
                int checks = 0;

                for (Player p : playerList) {
                    for (int i = 1; i <= distance ; i++) {
                        if (p.getPosition().getX() == x && p.getPosition().getY() - i == y) checks++;
                    }
                }

                return checks == distance;
            }

            case EAST -> {
                int checks = 0;

                for (Player p : playerList) {
                    for (int i = 1; i <= distance ; i++) {
                        if (p.getPosition().getX() - i == x && p.getPosition().getY() == y) checks++;
                    }
                }

                return checks == distance;
            }

            default -> throw new UnknownDirectionException("This direction does not exist. Valid directions are NORTH, EAST, SOUTH, EAST");
        }
    }

    /**
     * For location (x ; y), checks if there is no barrier in one direction
     * @param direction direction from the player to check
     * @param x         x coordinate of the location
     * @param y         y coordinate of the location
     * @param board     game board
     * @return the lack of barrier in the given direction
     * @throws UnknownDirectionException if direction is not a valid direction
     * @throws BadPositionException if x or y is lesser than 0
     */
    private boolean checkNoBarriers(Direction direction, int x, int y, Board board) throws Exception {
        Map<Direction, Edge> neighbours = (new Position(x, y)).getNeighbourEdges(board);
        return !neighbours.containsKey(direction) || neighbours.get(direction).getWeight() == 0;
    }

    /**
     * Checks if a movement in the diagonal d1-d2 is valid
     * @param d1            the first direction of the movement
     * @param d2            the second direction of the movement
     * @param playerList    list of all players on the board
     * @param board         game board
     * @return if the given diagonal move is valid
     * @throws UnknownDirectionException if direction is not a valid direction
     * @throws BadPositionException if x or y is lesser than 0
     */
    private boolean isDiagonalValid(Direction d1, Direction d2, List<Player> playerList, Board board) throws Exception {
        int x = this.position.getX();
        int y = this.position.getY();

        // Set the offset for each direction, NORTH being 0 and counting clockwise
        int xOffsetD1 = new int[]{0, 1, 0, -1}[d1.ordinal()];
        int yOffsetD1 = new int[]{-1, 0, 1, 0}[d1.ordinal()];
        int xOffsetD2 = new int[]{0, 1, 0, -1}[d2.ordinal()];
        int yOffsetD2 = new int[]{-1, 0, 1, 0}[d2.ordinal()];

        return (
            ((checkDirection(d1, 2, playerList)                                                                             // Whether there are 2 players back to back in a single direction...
                || checkDirection(d1, 1, playerList) && !checkNoBarriers(d1, x + xOffsetD1, y + yOffsetD1, board))    //...or there is a barrier behind a single player
                    && checkNoBarriers(d1, x, y, board)                                                                             // Checking if there's no barrier in d1 directly from the Player
                    && checkNoBarriers(d2, x + xOffsetD1, y + yOffsetD1, board)) ||                                           // Checking barrier in d2 after player has moved in d1
            ((checkDirection(d2, 2, playerList)                                                                             // Now we repeat by inverting d2 and d1
                || checkDirection(d2, 1, playerList) && !checkNoBarriers(d2, x + xOffsetD2, y + yOffsetD2, board))
                    && checkNoBarriers(d2, x, y, board)
                    && checkNoBarriers(d1, x + xOffsetD2, y + yOffsetD2, board))
        );
    }

    /**
     * Checks if a move jumping over another player is valid
     * @param d             direction the player is moving in
     * @param playerList    list of all players on the board
     * @param board         game board
     * @return if the given jumping move is valid
     * @throws UnknownDirectionException if direction is not a valid direction
     */
    private boolean isJumpingMoveValid(Direction d, List<Player> playerList, Board board) throws Exception {
        int x = this.position.getX();
        int y = this.position.getY();

        // Set the offset for each direction, NORTH being 0 and counting clockwise
        int xOffset = new int[]{0, 1, 0, -1}[d.ordinal()];
        int yOffset = new int[]{-1, 0, 1, 0}[d.ordinal()];

        return checkDirection(d, 1, playerList) && checkNoBarriers(d, x, y, board) && checkNoBarriers(d, x + xOffset, y + yOffset, board);
    }

    /**
     * Checks if the movement to (destX ; destY) is a single cell movement, and not blocked by a barrier
     * @param destX     x coordinate of destination
     * @param destY     y coordinate of destination
     * @param board     game board
     * @return if the given regular move is valid
     * @throws UnknownDirectionException if direction is not a valid direction
     * @throws BadPositionException if x or y is lesser than 0
     */
    private boolean checkAllRegularMoves(int destX, int destY, Board board) throws Exception {
        int playerX = this.position.getX();
        int playerY = this.position.getY();

        for (Direction d : Direction.values()) {
            // Set the offset for each direction, NORTH being 0 and counting clockwise
            int xOffset = new int[]{0, 1, 0, -1}[d.ordinal()];
            int yOffset = new int[]{-1, 0, 1, 0}[d.ordinal()];

            if (checkNoBarriers(d, playerX, playerY, board) && destX == playerX + xOffset && destY == playerY + yOffset) return true;
        }

        return false;
    }
    
    private boolean isBarrierValid(Edge e, Edge f, Board board) {

        return true;
    }
}
