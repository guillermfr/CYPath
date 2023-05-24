package graphicInterface;

import enumeration.Direction;
import exception.BadPositionException;
import exception.BadSizeException;
import exception.BadWeightException;
import gameObjects.Barrier;
import gameObjects.Board;
import gameObjects.Game;
import gameObjects.Player;
import graph.Edge;
import graph.EdgeWeightedGraph;
import graph.Position;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static java.lang.Math.*;

/**
 * The GameController class controls the game logic and UI interactions in the application.
 */
public class GameController {

    private EdgeWeightedGraph graph;
    private final static int STACK_PANE_HEIGHT = 400;
    private final static int PANE_PADDING = 10;
    private final static int GRID_GAP = 5;
    private final static int SCREEN_WIDTH = 1920;
    private final static int SCREEN_HEIGHT = 1080;
    private final static double BARRIER_SIZE = 1.4;
    private final static int BARRIER_LIMIT = 20;

    @FXML
    StackPane mainStackPane;
    @FXML
    Pane gamePane;
    @FXML
    BorderPane rootPane;
    @FXML
    VBox rightVBox;

    @FXML
    Label currentPlayerTurnLabel;
    @FXML
    Label turnCountLabel;
    @FXML
    Label barrierCountLabel;
    @FXML
    Button playButton;

    /**
     * Default constructor of the GameController class.
     */
    public GameController() {}

    /**
     * Initializes the game with the specified number of players and board size.
     * @param nbPlayers Number of player
     * @param size The size of the board
     * @throws Exception If an error occurs during initialization.
     */
    public void init(int nbPlayers, int size) throws Exception {
        try {
            graph = new EdgeWeightedGraph(size);
        } catch (BadSizeException bse) {
            System.out.println(bse);
        }

        // Calculating size of different elements
        double stackPaneHeight = SCREEN_HEIGHT * 0.7;
        mainStackPane.setPrefHeight(stackPaneHeight);
        mainStackPane.setPrefWidth(stackPaneHeight);

        double panePadding = floor(SCREEN_HEIGHT * 1.5/100);

        // Creating the grid pane containing the game board
        GridPane gridPane = new GridPane();
        gridPane.setHgap(GRID_GAP);
        gridPane.setVgap(GRID_GAP);
        gridPane.setLayoutX(panePadding);
        gridPane.setLayoutY(panePadding);

        double boxSize = floor((stackPaneHeight - panePadding * 2 - (graph.getSize() - 1) * GRID_GAP)/graph.getSize());

        for (int row = 0; row < graph.getSize(); row++) {
            for (int col = 0; col < graph.getSize(); col++) {
                Rectangle cell = new Rectangle(boxSize, boxSize);
                cell.setFill(Color.web("83BBEE"));
                gridPane.add(cell,col,row);
            }
        }

        gamePane.getChildren().add(gridPane);

        // Adding players in a pane, child of the StackPane
        // The barriers will also be on this pane
        Pane playersAndBarriersPane = new Pane();
        playersAndBarriersPane.setPrefHeight(stackPaneHeight);
        playersAndBarriersPane.setPrefWidth(stackPaneHeight);

        mainStackPane.getChildren().add(playersAndBarriersPane);

        Game game = new Game();
        // Initialize
        Circle[] playerListFx = new Circle[nbPlayers];
        initPlayers(game, playerListFx, nbPlayers, size, panePadding, boxSize);
        playersAndBarriersPane.getChildren().addAll(playerListFx);

        // Turns
        gameTurn(game, playerListFx, playersAndBarriersPane, nbPlayers, size, panePadding, boxSize);
    }

    /**
     * Initializes the players and their corresponding graphical representations.
     * @param game The instance of the Game class.
     * @param playerListFx An array to store the graphical representation of the players
     * @param nbPlayers The number of players in the game.
     * @param size The size of the game board.
     * @param panePadding The padding value for the pane.
     * @param boxSize The size of each box in the grid.
     */
    private void initPlayers(Game game, Circle[] playerListFx, int nbPlayers, int size, double panePadding, double boxSize) {
        try {
            Board board = new Board(size);
            game.setBoard(board);
            game.initGame(nbPlayers, size);

            if (nbPlayers >= 2) {
                Circle yellowPlayer = createPlayer(0, game.getPlayers().get(0).getPosition(), Color.web("#F2F47E"), panePadding, boxSize);
                Circle bluePlayer = createPlayer(1, game.getPlayers().get(1).getPosition(), Color.web("#525EC8"), panePadding, boxSize);
                playerListFx[0] = yellowPlayer;
                playerListFx[1] = bluePlayer;
            }

            if (nbPlayers == 4) {
                Circle redPlayer = createPlayer(2, game.getPlayers().get(2).getPosition(), Color.web("#F17171"), panePadding, boxSize);
                Circle greenPlayer = createPlayer(3, game.getPlayers().get(3).getPosition(), Color.web("#7DE187"), panePadding, boxSize);
                playerListFx[2] = redPlayer;
                playerListFx[3] = greenPlayer;
            }
        } catch (BadSizeException e) {
            System.out.println(e);
        }
    }

    /**
     * Performs a game turn for the current player, handling player's actions such as moving the player or placing barriers.
     * @param game The instance of the Game class.
     * @param playerListFx An array storing the graphical representation of the players.
     * @param playersAndBarriersPane The pane on which the players and barriers are
     * @param nbPlayers The number of players in the game.
     * @param size The size of the game board.
     * @param panePadding The padding value for the pane.
     * @param boxSize The size of each box in the grid.
     */
    private void gameTurn(Game game, Circle[] playerListFx, Pane playersAndBarriersPane, int nbPlayers, int size, double panePadding, double boxSize) {
        int currentPlayerId = game.getCurrentPlayerTurn(nbPlayers);
        Player currentPlayer = game.getPlayers().get(currentPlayerId);

        // Update player turn on top
        currentPlayerTurnLabel.setText(currentPlayer.getName() + "'s Turn");

        // Update turn count on the side
        turnCountLabel.setText("Turn " + (game.getTurnCount() + 1));

        // Handle player's turn
        // Monitor the current mode between move player and placing barrier
        BooleanProperty isModeMovePlayer = new SimpleBooleanProperty(true);
        // Monitor whether the barrier has to be placed horizontally or vertically
        BooleanProperty isBarrierHorizontal = new SimpleBooleanProperty(true);

        EventHandler<MouseEvent> rootPaneEventHandler = event -> {
            if (event.getButton() == MouseButton.SECONDARY && !isModeMovePlayer.get()) {
                isBarrierHorizontal.set(!isBarrierHorizontal.get());
                System.out.println("Now " + (isBarrierHorizontal.get() ? "horizontal" : "vertical"));
            }
        };

        rootPane.setOnMouseClicked(rootPaneEventHandler);

        // Default handler: moving the player
        EventHandler<MouseEvent> mainStackPanePlayerEventHandler = event -> {
            try {
                if (event.getButton() == MouseButton.PRIMARY) {
                    Position newPos = new Position(pxCoordsToPlayerCoords(event.getX(), event.getY(), panePadding, boxSize));
                    if (newPos.getX() >= size || newPos.getY() >= size) throw new BadPositionException();
                    boolean isMoveValid = currentPlayer.move(newPos.getX(), newPos.getY(), game.getBoard(), game.getPlayers());
                    if (isMoveValid) {
                        double[] newCoords = playerCoordsToPxCoords(newPos, panePadding, boxSize);
                        playerListFx[currentPlayerId].setCenterX(newCoords[0]);
                        playerListFx[currentPlayerId].setCenterY(newCoords[1]);

                        if (game.checkVictory() == null) {
                            goToNextTurn(game, playerListFx, playersAndBarriersPane, isModeMovePlayer, isBarrierHorizontal, nbPlayers, size, panePadding, boxSize);
                        } else System.out.println("Victoire de " + currentPlayer.getName()); // TODO : Victory screen or sth like that
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        };

        mainStackPane.setOnMouseClicked(mainStackPanePlayerEventHandler);

        // Barrier placing handler
        EventHandler<MouseEvent> mainStackPaneBarrierEventHandler = new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    try {
                        Edge[] edges = pxCoordsToBarrierCoords(event.getX(), event.getY(), game.getBoard(), isBarrierHorizontal.get(), panePadding, boxSize) ;
                        if (edges == null) System.out.println("Can't place a barrier here");
                        else {
                            boolean isBarrierValid = currentPlayer.placeBarrier(edges[0], edges[1], game.getBoard(), game.getPlayers(), game.getBoard().getBarriers());
                            if (isBarrierValid) {
                                Position[] positions1 = new Position[2];
                                edges[0].normalizeEdgePositions(positions1, game.getBoard());
                                Position[] positions2 = new Position[2];
                                edges[1].normalizeEdgePositions(positions2, game.getBoard());

                                Rectangle rectangle = createBarrier(positions1[0], positions2[0], panePadding, boxSize);
                                playersAndBarriersPane.getChildren().add(rectangle);

                                barrierCountLabel.setText(game.getBoard().getBarriers().size() + "/" + BARRIER_LIMIT);

                                // Remove barrier handlers before going to next turn
                                mainStackPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
                                goToNextTurn(game, playerListFx, playersAndBarriersPane, isModeMovePlayer, isBarrierHorizontal, nbPlayers, size, panePadding, boxSize);
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }
        };

        // Handle the switching and (de)activating the corresponding eventHandler
        playButton.setOnAction(e -> {
            if (isModeMovePlayer.get() && game.getBoard().getBarriers().size() < BARRIER_LIMIT) {
                isModeMovePlayer.set(false);
                playButton.setText("Move player");
                mainStackPane.setOnMouseClicked(mainStackPaneBarrierEventHandler);
            } else {
                isModeMovePlayer.set(true);
                playButton.setText("Place a barrier");
                mainStackPane.setOnMouseClicked(mainStackPanePlayerEventHandler);
            }
        });
    }

    /**
     * Proceeds to the next turn and resets properties and handlers
     * @param game                      The instance of the Game class
     * @param playerListFx              An array storing the graphical representation of the players
     * @param playersAndBarriersPane    The pane on which the players and barriers are
     * @param isModeMovePlayer          Whether the mode was move player or not
     * @param isBarrierHorizontal       Whether the barrier placing orientation was horizontal or vertical
     * @param nbPlayers                 The number of players in the game
     * @param size                      The size of the game board
     * @param panePadding               The padding value for the pane
     * @param boxSize                   The size of each box in the grid
     */
    private void goToNextTurn(Game game, Circle[] playerListFx, Pane playersAndBarriersPane, BooleanProperty isModeMovePlayer, BooleanProperty isBarrierHorizontal, int nbPlayers, int size, double panePadding, double boxSize) {
        // Reset properties and handlers
        if (!isModeMovePlayer.get()) isModeMovePlayer.set(true);
        if (!isBarrierHorizontal.get()) isBarrierHorizontal.set(true);
        playButton.setText("Place a barrier");

        game.setTurnCount(game.getTurnCount() + 1);
        gameTurn(game, playerListFx, playersAndBarriersPane, nbPlayers, size, panePadding, boxSize);
    }

    /**
     * Creates a graphical representation of a player at the specified position.
     * @param id The ID of the player.
     * @param pos The position of the player.
     * @param color The color of the player.
     * @param panePadding The padding value of the pane.
     * @param boxSize The size of each box in the grid.
     * @return The Circle object representing the player.
     */
    private Circle createPlayer(int id, Position pos, Color color, double panePadding, double boxSize) {
        double[] pxCoords = playerCoordsToPxCoords(pos, panePadding, boxSize);
        Circle player = new Circle(pxCoords[0], pxCoords[1], boxSize / 2.5, color);

        DropShadow ds = new DropShadow();
        ds.setRadius(4.0);
        ds.setOffsetX(2.0);
        ds.setOffsetY(4.0);
        ds.setColor(Color.web("#000000", 0.25));
        player.setEffect(ds);

        switch (id) {
            case 0 -> player.setStroke(Color.web("#CF8962"));
            case 1 -> player.setStroke(Color.web("#282741"));
            case 2 -> player.setStroke(Color.web("#A73030"));
            case 3 -> player.setStroke(Color.web("#419549"));
        }

        player.setStrokeWidth(1.0);

        return player;
    }

    /**
     * Converts player coordinates to pixel coordinates on the game board.
     * @param pos The player's position.
     * @param panePadding The padding value for the pane.
     * @param boxSize The size of each box in the grid.
     * @return An array containing the x and y pixel coordinates.
     */
    private double[] playerCoordsToPxCoords(Position pos, double panePadding, double boxSize) {
        double[] px = new double[2];
        px[0] = panePadding + (pos.getX() + 0.5) * boxSize + pos.getX() * GRID_GAP;
        px[1] = panePadding + (pos.getY() + 0.5) * boxSize + pos.getY() * GRID_GAP;

        return px;
    }

    /**
     * Convert pixel coordinate to player coordinate on the game board.
     * @param x The x pixel coordinate.
     * @param y The y pixel coordinate.
     * @param panePadding The padding value for the pane.
     * @param boxSize The size of each box int the grid.
     * @return An array containing the x and y player coordinates.
     */
    private int[] pxCoordsToPlayerCoords(double x, double y, double panePadding, double boxSize) {
        int[] pos = new int[2];

        pos[0] = (int) round((x - panePadding - 0.5*boxSize) / (boxSize + GRID_GAP));
        pos[1] = (int) round((y - panePadding - 0.5*boxSize) / (boxSize + GRID_GAP));

        return pos;
    }

    // For Barrier
    /**
     * Creates a graphical representation of a barrier under or on the right of the two given positions.
     * @param pos1 The first position of the barrier.
     * @param pos2 The second position of the barrier.
     * @param panePadding The padding value for the pane.
     * @param boxSize The size of each box in the grid
     * @return The Rectangle object representing the barrier.
     * @throws Exception If the positions do not form a valid horizontal or vertical barrier.
     */
    private Rectangle createBarrier(Position pos1, Position pos2, double panePadding, double boxSize) throws Exception {
        if (pos1.toAdjacencyListIndex(graph.getSize()) > pos2.toAdjacencyListIndex(graph.getSize())) {
            Position tempPos = pos1;
            pos1 = pos2;
            pos2 = tempPos;
        }

        Rectangle barrier = new Rectangle();
        barrier.setFill(Color.web("#101548"));

        DropShadow ds = new DropShadow();
        ds.setRadius(4.0);
        ds.setOffsetX(2.0);
        ds.setOffsetY(4.0);
        ds.setColor(Color.web("#000000", 0.25));
        barrier.setEffect(ds);

        double barrierX, barrierY, barrierWidth, barrierHeight;

        // Horizontal barrier
        if (pos2.getX() - pos1.getX() == 1 && pos2.getY() - pos1.getY() == 0) {
            if (pos1.getY() == graph.getSize() - 1) throw new Exception();  // TODO dedicated Exception
            barrierX = panePadding + pos1.getX() * (boxSize + GRID_GAP);
            barrierY = panePadding + pos1.getY() * GRID_GAP + (pos1.getY() + 1) * boxSize - GRID_GAP * ((BARRIER_SIZE - 1) / 2);
            barrierWidth = 2 * boxSize + GRID_GAP;
            barrierHeight = GRID_GAP * BARRIER_SIZE;
        } else {
            // Vertical barrier
            if (pos2.getX() - pos1.getX() == 0 && pos2.getY() - pos1.getY() == 1) {
                if (pos1.getX() == graph.getSize() - 1) throw new Exception(); // TODO dedicated Exception
                barrierX = panePadding + pos1.getX() * GRID_GAP + (pos1.getX() + 1) * boxSize - GRID_GAP * ((BARRIER_SIZE - 1) / 2);
                barrierY = panePadding + pos1.getY() * (boxSize + GRID_GAP);
                barrierWidth = GRID_GAP * BARRIER_SIZE;
                barrierHeight = 2 * boxSize + GRID_GAP;
            } else {
                // If any other case, the barrier does not have correct Positions: throw an exception (TODO)
                throw new Exception();
            }
        }

        barrier.setX(barrierX);
        barrier.setY(barrierY);
        barrier.setWidth(barrierWidth);
        barrier.setHeight(barrierHeight);

        return barrier;
    }

    /**
     * Convert pixel coordinates to barrier coordinates on the game board.
     * @param x The x pixel coordinate.
     * @param y The y pixel coordinate
     * @param board The game board.
     * @param isBarrierHorizontal A boolean indicating whether the barrier is horizontal (true) or vertical (false).
     * @param panePadding The padding value for the pane.
     * @param boxSize The size of each box in the grid.
     * @return An array containing the two edges forming the barrier.
     * @throws BadPositionException If the calculated positions are invalid or out of bounds.
     */
    Edge[] pxCoordsToBarrierCoords(double x, double y, Board board, boolean isBarrierHorizontal, double panePadding, double boxSize) throws BadPositionException {
        Edge[] edges = new Edge[2];

        if (isBarrierHorizontal) {
            double newX = floor((x - panePadding) / (boxSize + GRID_GAP));
            double newY = (y - panePadding + (double) GRID_GAP / 2) / (boxSize + GRID_GAP);

            // If the click was within a little margin in Y, we still accept it and return the closest value
            if (round(newY) > 0 && round(newY) < board.getSize() && abs(round(newY) - newY) <= 0.1) {
                newY = round(newY) - 1;

                Position pos = new Position((int) newX, (int) newY);
                edges[0] = pos.getNeighbourEdges(board).get(Direction.SOUTH);
                edges[1] = pos.getNeighbourPositions(board).get(newX == board.getSize() - 1 ? Direction.WEST : Direction.EAST).getNeighbourEdges(board).get(Direction.SOUTH); // If at the border, the barrier is from right to left instead
            } else {
                return null;
            }
        } else {
            double newX = (x - panePadding + (double) GRID_GAP / 2) / (boxSize + GRID_GAP);
            double newY = floor((y - panePadding) / (boxSize + GRID_GAP));

            // If the click was within a little margin in X, we still accept it and return the closest value
            if (round(newX) > 0 && round(newX) < board.getSize() && abs(round(newX) - newX) <= 0.1) {
                newX = round(newX) - 1;

                Position pos = new Position((int) newX, (int) newY);
                edges[0] = pos.getNeighbourEdges(board).get(Direction.EAST);
                edges[1] = pos.getNeighbourPositions(board).get(newY == board.getSize() - 1 ? Direction.NORTH : Direction.SOUTH).getNeighbourEdges(board).get(Direction.EAST); // If at the border, the barrier is from bottom to top instead
            } else {
                return null;
            }
        }

        return edges;
    }
}
