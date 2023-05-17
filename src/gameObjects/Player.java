package gameObjects;

import enumeration.Color;
import enumeration.Direction;
import exception.UnknownDirectionException;
import graph.Position;
import graph.Edge;

import java.util.LinkedList;
import java.util.List;

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

    public void move(int x, int y, Board board, List<Player> playerList) throws UnknownDirectionException {
        if (this.isMoveValid(x, y, board, playerList)) {
            this.position.move(x, y);
        }
    }

    public void placeBarrier(Edge e, Edge f, Board board) {

    }

    private boolean isMoveValid(int x, int y, Board board, List<Player> playerList) throws UnknownDirectionException {
        LinkedList<Edge>[] adjacencyList = board.getAdjacencyList();
        Position playerPos = this.position;
        int playerX = playerPos.getX();
        int playerY = playerPos.getY();

        if (isCellOccupied(x, y, playerList)) return false;

        // TODO: Check for barriers as well for the diagonal moves
        // Verifying where the user wants to go first, then checking if the requirements are met to avoid iterating through the user list on every iteration
        // If the player wants to move diagonal: there has to be a player to jump over, and another behind
        // If player wants to go north-west
        if (x + 1 == playerX && y + 1 == playerY) {
            return checkDirection(Direction.NORTH, 2, playerX, playerY, playerList) || checkDirection(Direction.WEST, 2, playerX, playerY, playerList);
        }

        // If player wants to go north-east
        if (x - 1 == playerX && y + 1 == playerY) {
            return checkDirection(Direction.NORTH, 2, playerX, playerY, playerList) || checkDirection(Direction.EAST, 2, playerX, playerY, playerList);
        }

        // If player wants to go south-east
        if (x - 1 == playerX && y - 1 == playerY) {
            return checkDirection(Direction.SOUTH, 2, playerX, playerY, playerList) || checkDirection(Direction.EAST, 2, playerX, playerY, playerList);
        }

        // If player wants to go south-west
        if (x + 1 == playerX && y - 1 == playerY) {
            return checkDirection(Direction.SOUTH, 2, playerX, playerY, playerList) || checkDirection(Direction.WEST, 2, playerX, playerY, playerList);
        }

        // If the player wants to move 2 cells in any direction: there has to be a player to jump over
        // If player wants to go north
        if (x == playerX && y + 2 == playerY) {
            return checkDirection(Direction.NORTH, 1, playerX, playerY, playerList);
        }

        // If player wants to go east
        if (x - 2 == playerX && y == playerY) {
            return checkDirection(Direction.EAST, 1, playerX, playerY, playerList);
        }

        // If player wants to go south
        if (x == playerX && y - 2 == playerY) {
            return checkDirection(Direction.SOUTH, 1, playerX, playerY, playerList);
        }

        // If player wants to go west
        if (x + 2 == playerX && y == playerY) {
            return checkDirection(Direction.WEST, 1, playerX, playerY, playerList);
        }

        // If the player wants to move by a single adjacent cell
        return (
            x + 1 == playerX && y == playerY ||
            x == playerX && y - 1 == playerY ||
            x - 1 == playerX && y == playerY ||
            x == playerX && y + 1 == playerY
        );
    }

    private boolean isCellOccupied(int x, int y, List<Player> playerList) {
        for (Player p : playerList) {
            if (p.getPosition().getX() == x && p.getPosition().getY() == y) return true;
        }

        return false;
    }

    private boolean checkDirection(Direction direction, int distance, int x, int y, List<Player> playerList) throws UnknownDirectionException {
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

            default -> throw new UnknownDirectionException("This direction does not exist.");
        }
    }
    
    private boolean isBarrierValid(Edge e, Edge f, Board board) {

        return true;
    }
}
