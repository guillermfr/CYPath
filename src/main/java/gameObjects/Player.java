package gameObjects;

import enumeration.Color;
import enumeration.Direction;
import exception.BadDistanceException;
import exception.BadWeightException;
import exception.UnknownDirectionException;
import exception.BadPositionException;
import graph.Position;
import graph.Edge;

import java.util.List;
import java.util.Map;

/**
 * Class that represents a player.
 * A player has a name, a Position and a color.
 * A player can move and place a barrier.
 */
public class Player {
    /**
     * Name of the player
     * Its name will be displayed throughout the game
     */
    private String name;

    /**
     * Position of the player
     * Its location on the game board
     */
    private Position position;

    /**
     * Color of the player
     * The color representing the player
     */
    private Color color;

    /**
     * Constructor for the class Player
     * @param name name of the player
     * @param color color of the player
     */
    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Gets the name of the player
     * @return player's current name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the color of the player
     * @return player's current color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gets the position of the player
     * @return player's current position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the new color of the player
     * @param color new color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Sets the new name of the player
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the new position of the player
     * @param position new position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Moves the player to location (x ; y) if it is valid
     * @param x             x coordinate of the to-be location
     * @param y             y coordinate of the to-be location
     * @param board         game board
     * @param playerList    list of all players on the board
     * @return Whether the player was moved or not
     * @throws UnknownDirectionException if at some point, a direction is not valid
     * @throws BadPositionException if x or y is lesser than 0
     */
    public boolean move(int x, int y, Board board, List<Player> playerList) throws Exception {
        if (this.isMoveValid(x, y, board, playerList)) {
            this.position.move(x, y);
            return true;
        }

        return false;
    }

    /**
     * Places a barrier on edges e and f if it is valid
     * @param e             first edge on which the barrier is placed
     * @param f             second edge on which the barrier is placed
     * @param board         game board
     * @param playerList    list of all players on the board
     * @param barrierList   list of all barriers on the board
     * @return Whether the barrier was placed or not
     * @throws BadWeightException if the weight is negative
     */
    public boolean placeBarrier(Edge e, Edge f, Board board, List<Player> playerList, List<Barrier> barrierList) throws BadWeightException {
        if (this.isBarrierValid(e, f, board, playerList, barrierList)) {
            e.setBidirectionalEdgeWeight(1, board);
            f.setBidirectionalEdgeWeight(1, board);

            barrierList.add(new Barrier(e, f, this));

            return true;
        }

        return false;
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
    public boolean isMoveValid(int x, int y, Board board, List<Player> playerList) throws Exception {
        Position playerPos = this.position;
        int playerX = playerPos.getX();
        int playerY = playerPos.getY();

        if (isCellOccupied(x, y, playerList)) return false;

        // Verifying where the user wants to go first, then checking if the requirements are met to avoid iterating through the user list on every iteration
        // If the player wants to move diagonal: there has to be a player to jump over, and another behind, with no barriers stopping him
        // If player wants to go north-west
        if (x + 1 == playerX && y + 1 == playerY) {
            return isDiagonalMoveValid(Direction.NORTH, Direction.WEST, playerList, board);
        }

        // If player wants to go north-east
        if (x - 1 == playerX && y + 1 == playerY) {
            return isDiagonalMoveValid(Direction.NORTH, Direction.EAST, playerList, board);
        }

        // If player wants to go south-east
        if (x - 1 == playerX && y - 1 == playerY) {
            return isDiagonalMoveValid(Direction.SOUTH, Direction.EAST, playerList, board);
        }

        // If player wants to go south-west
        if (x + 1 == playerX && y - 1 == playerY) {
            return isDiagonalMoveValid(Direction.SOUTH, Direction.WEST, playerList, board);
        }

        // If the player wants to move 2 cells in any direction: there has to be a player to jump over and no barriers to stop the player
        // If player wants to go north
        if (x == playerX && y + 2 == playerY) {
            return isJumpingMoveValid(Direction.NORTH, playerList, board);
        }

        // If player wants to go east
        if (x - 2 == playerX && y == playerY) {
            return isJumpingMoveValid(Direction.EAST, playerList, board);
        }

        // If player wants to go south
        if (x == playerX && y - 2 == playerY) {
            return isJumpingMoveValid(Direction.SOUTH, playerList, board);
        }

        // If player wants to go west
        if (x + 2 == playerX && y == playerY) {
            return isJumpingMoveValid(Direction.WEST, playerList, board);
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
     * For location (x ; y), checks if there is a barrier in one direction
     * The border of the board is considered as a barrier
     * @param direction direction from the location to check
     * @param x         x coordinate of the location
     * @param y         y coordinate of the location
     * @param board     game board
     * @return the presence of a barrier in the given direction
     * @throws UnknownDirectionException if direction is not a valid direction
     * @throws BadPositionException if x or y is lesser than 0
     */
    private boolean checkBarrier(Direction direction, int x, int y, Board board) throws Exception {
        Map<Direction, Edge> neighbours = (new Position(x, y)).getNeighbourEdges(board);
        return !neighbours.containsKey(direction) || neighbours.get(direction).getWeight() == 1;
    }

    /**
     * For location at Position p, checks if there is a barrier in one direction
     * The border of the board is considered as a barrier
     * @param direction direction from the location to check
     * @param p         position from where to check
     * @param board     game board
     * @return the presence of a barrier in the given direction
     */
    private boolean checkBarrier(Direction direction, Position p, Board board) {
        Map<Direction, Edge> neighbours = p.getNeighbourEdges(board);
        return !neighbours.containsKey(direction) || neighbours.get(direction).getWeight() == 1;
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
    private boolean isDiagonalMoveValid(Direction d1, Direction d2, List<Player> playerList, Board board) throws Exception {
        int x = this.position.getX();
        int y = this.position.getY();

        // Set the offset for each direction, NORTH being 0 and counting clockwise
        int xOffsetD1 = new int[]{0, 1, 0, -1}[d1.ordinal()];
        int yOffsetD1 = new int[]{-1, 0, 1, 0}[d1.ordinal()];
        int xOffsetD2 = new int[]{0, 1, 0, -1}[d2.ordinal()];
        int yOffsetD2 = new int[]{-1, 0, 1, 0}[d2.ordinal()];

        return (
            ((checkDirection(d1, 2, playerList) ||                                                                              // Whether there are 2 players back to back in a single direction...
                checkDirection(d1, 1, playerList) && checkBarrier(d1, x + xOffsetD1, y + yOffsetD1, board)) &&            //...or there is a barrier behind a single player
                !checkBarrier(d1, x, y, board) &&                                                                                       // Checking if there's no barrier in d1 directly from the Player
                !checkBarrier(d2, x + xOffsetD1, y + yOffsetD1, board)) ||                                                        // Checking barrier in d2 after player has moved in d1
            ((checkDirection(d2, 2, playerList) ||                                                                              // Now we repeat by inverting d2 and d1
                checkDirection(d2, 1, playerList) && checkBarrier(d2, x + xOffsetD2, y + yOffsetD2, board)) &&
                !checkBarrier(d2, x, y, board) &&
                !checkBarrier(d1, x + xOffsetD2, y + yOffsetD2, board))
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

        return checkDirection(d, 1, playerList) && !checkBarrier(d, x, y, board) && !checkBarrier(d, x + xOffset, y + yOffset, board);
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

            if (!checkBarrier(d, playerX, playerY, board) && destX == playerX + xOffset && destY == playerY + yOffset) return true;
        }

        return false;
    }

    /**
     * Checks if placing a barrier on edges e and f is valid
     * @param e             first edge on which the barrier is placed
     * @param f             second edge on which the barrier is placed
     * @param board         game board
     * @param playerList    list of all players on the board
     * @param barrierList   list of all barriers on the board
     * @return if the given barrier would be valid if placed
     * @throws BadWeightException if the weight is negative
     */
    private boolean isBarrierValid(Edge e, Edge f, Board board, List<Player> playerList, List<Barrier> barrierList) throws BadWeightException {
        final int BARRIER_LIMIT = 20;

        // Check if there are less than twenty barriers
        if (barrierList.size() >= BARRIER_LIMIT) return false;

        // We will get the Positions from smallest to biggest (in terms of adjacency list index)
        Position[] ePositions = new Position[2];
        int eIndex = e.normalizeEdgePositions(ePositions, board);

        Position[] fPositions = new Position[2];
        int fIndex = f.normalizeEdgePositions(fPositions, board);

        // Check if the barrier is two adjacent edges
        boolean isHorizontalBarrier = ePositions[0].checkDistance(fPositions[0], 1, 0) && ePositions[1].checkDistance(fPositions[1], 1, 0);
        boolean isVerticalBarrier = ePositions[0].checkDistance(fPositions[0], 0, 1) && ePositions[1].checkDistance(fPositions[1], 0, 1);
        if (!isHorizontalBarrier && !isVerticalBarrier) return false;

        // Check if the barrier doesn't overlap another in the same orientation i.e. one of the edge is already a barrier
        if (e.getWeight() == 1 || f.getWeight() == 1) return false;

        // Check if the barrier doesn't cross another 2-long barrier (in a + shape)
        // We have to check that there is no barrier on the right of ePositions[0] and ePositions[1] (or at the bottom if a vertical barrier)
        // but also that if there are two, they belong to two separate barriers (in a long + shape, kind of)
        Map<Direction, Edge> neighbourList1 = ePositions[0].getNeighbourEdges(board);
        Map<Direction, Edge> neighbourList2 = ePositions[1].getNeighbourEdges(board);

        if (
            isHorizontalBarrier &&
                checkBarrier(Direction.EAST, ePositions[0], board) &&
                checkBarrier(Direction.EAST, ePositions[1], board) &&
                (barrierList.stream().anyMatch(b -> (
                    b.getEdge1() == neighbourList1.get(eIndex < fIndex ? Direction.EAST : Direction.WEST) &&
                        b.getEdge2() == neighbourList2.get(eIndex < fIndex ? Direction.EAST : Direction.WEST) ||
                    b.getEdge1() == neighbourList2.get(eIndex < fIndex ? Direction.EAST : Direction.WEST) &&
                        b.getEdge2() == neighbourList1.get(eIndex < fIndex ? Direction.EAST : Direction.WEST)
                ))) ||
            isVerticalBarrier &&
                checkBarrier(Direction.SOUTH, ePositions[0], board) &&
                checkBarrier(Direction.SOUTH, ePositions[1], board) &&
                (barrierList.stream().anyMatch(b -> (
                    b.getEdge1() == neighbourList1.get(eIndex < fIndex ? Direction.SOUTH : Direction.NORTH) &&
                        b.getEdge2() == neighbourList2.get(eIndex < fIndex ? Direction.SOUTH : Direction.NORTH) ||
                    b.getEdge1() == neighbourList2.get(eIndex < fIndex ? Direction.SOUTH : Direction.NORTH) &&
                        b.getEdge2() == neighbourList1.get(eIndex < fIndex ? Direction.SOUTH : Direction.NORTH)
                )))
        ) return false;

        // Check if the barrier doesn't cut any player from its goal
        // Temporarily accept the barriers as valid to check if they would be
        e.setBidirectionalEdgeWeight(1, board);
        f.setBidirectionalEdgeWeight(1, board);
        boolean isGameStillPossible = !board.checkPath(playerList).containsValue(false);
        // Remove them
        e.setBidirectionalEdgeWeight(0, board);
        f.setBidirectionalEdgeWeight(0, board);

        return isGameStillPossible;
    }
}