package graphicInterface;

import enumeration.Direction;
import exception.*;
import gameObjects.Board;
import gameObjects.Game;
import gameObjects.Player;
import graph.Edge;
import graph.EdgeWeightedGraph;
import graph.Position;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import saveLoad.CreateSaveDir;
import saveLoad.SaveFileName;
import saveLoad.SerializationUtils;

import java.util.ArrayList;
import java.util.Optional;

import static constant.GameProperties.BARRIER_LIMIT;
import static constant.GraphicInterfaceSizes.*;
import static java.lang.Math.*;

/**
 * The GameController class controls the game logic and UI interactions in the application.
 */
public class GameController {

    private EdgeWeightedGraph graph;
    private Game game;
    private String saveNameGeneral = "";

    @FXML
    StackPane mainStackPane;
    @FXML
    StackPane topStackPane;
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
    @FXML
    Button saveButton;

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
        gamePane.setPrefHeight(stackPaneHeight);
        gamePane.setPrefWidth(stackPaneHeight);

        double panePadding = stackPaneHeight * 0.02 * 9 / graph.getSize();

        long gridGap = round(panePadding / 2);

        // Creating the grid pane containing the game board
        GridPane gridPane = new GridPane();
        gridPane.setHgap(gridGap);
        gridPane.setVgap(gridGap);
        gridPane.setLayoutX(panePadding);
        gridPane.setLayoutY(panePadding);

        double boxSize = (stackPaneHeight - panePadding * 2 - (graph.getSize() - 1) * gridGap)/(graph.getSize());

        // boxSize is a double, so we can't just use it as it is, or it will get rounded and all the boxes will leave a giant gap at the end of the board
        // That's why we correct the x and y depending on what pixel we are on and what pixel we should be on
        long currentY = 0;
        for (int row = 0; row < graph.getSize(); row++) {
            double supposedY = (row + 1) * boxSize + row * gridGap;
            long correctY = round(supposedY) - currentY;
            currentY += correctY + gridGap;

            long currentX = 0;
            for (int col = 0; col < graph.getSize(); col++) {
                double supposedX = (col + 1) * boxSize + col * gridGap;
                long correctX = round(supposedX) - currentX;
                currentX += correctX + gridGap;

                Rectangle cell = new Rectangle(correctX, correctY);
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

        this.game = new Game();
        // Initialize
        Circle[] playerListFx = new Circle[nbPlayers];
        initPlayers(playerListFx, nbPlayers, size, panePadding, gridGap, boxSize);
        playersAndBarriersPane.getChildren().addAll(playerListFx);

        // Turns
        gameTurn(playerListFx, playersAndBarriersPane, nbPlayers, size, panePadding, gridGap, boxSize);
    }

    /**
     * Initializes the players and their corresponding graphical representations.
     * @param playerListFx An array to store the graphical representation of the players
     * @param nbPlayers The number of players in the game.
     * @param size The size of the game board.
     * @param panePadding The padding value for the pane.
     * @param gridGap The gap between each box
     * @param boxSize The size of each box in the grid.
     */
    private void initPlayers(Circle[] playerListFx, int nbPlayers, int size, double panePadding, long gridGap, double boxSize) {
        try {
            Board board = new Board(size);
            game.setBoard(board);
            game.initGame(nbPlayers, size);

            if (nbPlayers >= 2) {
                Circle yellowPlayer = createPlayer(0, game.getPlayers().get(0).getPosition(), false, panePadding, gridGap, boxSize);
                Circle bluePlayer = createPlayer(1, game.getPlayers().get(1).getPosition(), false, panePadding, gridGap, boxSize);
                playerListFx[0] = yellowPlayer;
                playerListFx[1] = bluePlayer;
            }

            if (nbPlayers == 4) {
                Circle redPlayer = createPlayer(2, game.getPlayers().get(2).getPosition(), false, panePadding, gridGap, boxSize);
                Circle greenPlayer = createPlayer(3, game.getPlayers().get(3).getPosition(), false, panePadding, gridGap, boxSize);
                playerListFx[2] = redPlayer;
                playerListFx[3] = greenPlayer;
            }
        } catch (UnknownPlayerIdException | BadSizeException e) {
            System.out.println(e);
        }
    }

    /**
     * Performs a game turn for the current player, handling player's actions such as moving the player or placing barriers.
     * @param playerListFx An array storing the graphical representation of the players.
     * @param playersAndBarriersPane The pane on which the players and barriers are
     * @param nbPlayers The number of players in the game.
     * @param size The size of the game board.
     * @param panePadding The padding value for the pane.
     * @param gridGap The gap between each box
     * @param boxSize The size of each box in the grid.
     */
    private void gameTurn(Circle[] playerListFx, Pane playersAndBarriersPane, int nbPlayers, int size, double panePadding, long gridGap, double boxSize) throws Exception {
        int currentPlayerId = game.getCurrentPlayerTurn();
        Player currentPlayer = game.getPlayers().get(currentPlayerId);

        // Update player turn on top
        currentPlayerTurnLabel.setText(currentPlayer.getName() + "'s Turn");

        // Update turn count on the side
        turnCountLabel.setText("Turn " + (game.getTurnCount() + 1));

        // Show all possible moves to the player
        ArrayList<Circle> ghostPlayers = new ArrayList<>(); // There can be at most 6 ghost players
        int[][] possibleMovesOffsets = new int[][]{{0, -2}, {-1, -1}, {0, -1}, {1, -1}, {-2, 0}, {-1, 0}, {1, 0}, {2, 0}, {-1, 1}, {0, 1}, {1, 1}, {0, 2}};
        for (int[] offsets : possibleMovesOffsets) {
            int x = currentPlayer.getPosition().getX() + offsets[0];
            int y = currentPlayer.getPosition().getY() + offsets[1];
            boolean isMoveValid = currentPlayer.isMoveValid(x, y, game.getBoard(), game.getPlayers());

            if (isMoveValid) {
                Circle ghostPlayer = createPlayer(currentPlayerId, new Position(x, y), true, panePadding, gridGap, boxSize);

                playersAndBarriersPane.getChildren().add(ghostPlayer);
                ghostPlayers.add(ghostPlayer);
            }
        }

        // Handle player's turn
        // Monitor the current mode between move player and placing barrier
        BooleanProperty isModeMovePlayer = new SimpleBooleanProperty(true);
        // Monitor whether the barrier has to be placed horizontally or vertically
        BooleanProperty isBarrierHorizontal = new SimpleBooleanProperty(true);

        // Default handler: moving the player
        EventHandler<MouseEvent> mainStackPanePlayerEventHandler = event -> {
            try {
                if (game.checkVictory() == null) {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        Position newPos = new Position(pxCoordsToPlayerCoords(event.getX(), event.getY(), panePadding, gridGap, boxSize));
                        if (newPos.getX() >= size || newPos.getY() >= size) throw new BadPositionException();
                        boolean isMoveValid = currentPlayer.move(newPos.getX(), newPos.getY(), game.getBoard(), game.getPlayers());
                        if (isMoveValid) {
                            double[] newCoords = playerCoordsToPxCoords(newPos, panePadding, gridGap, boxSize);
                            playerListFx[currentPlayerId].setCenterX(newCoords[0]);
                            playerListFx[currentPlayerId].setCenterY(newCoords[1]);

                            if (game.checkVictory() == null) {
                                goToNextTurn(playerListFx, ghostPlayers, playersAndBarriersPane, isModeMovePlayer, isBarrierHorizontal, nbPlayers, size, panePadding, gridGap, boxSize);
                            } else {
                                // Remove ghost players
                                for (Circle gp : ghostPlayers) {
                                    playersAndBarriersPane.getChildren().remove(gp);
                                }
                                ghostPlayers.clear();

                                // Remove the save button
                                playButton.getStyleClass().clear();
                                playButton.getStyleClass().add("unavailableButtonMenu");
                                rightVBox.getChildren().remove(saveButton);

                                // Update the top text
                                String winText = currentPlayer.getName() + " won !";
                                currentPlayerTurnLabel.setText(winText);

                                // Calculate the new size of the text
                                Text text = new Text(winText);
                                Font font = Font.font("Reem Kufi Regular", 40.0);
                                text.setFont(font);
                                double textSize = text.getLayoutBounds().getWidth();

                                // Display and align everything
                                Circle winner = createPlayer(currentPlayerId, new Position(0, 0), false, panePadding, gridGap, boxSize);
                                winner.setRadius(winner.getRadius() * graph.getSize() / 9);
                                topStackPane.getChildren().add(winner);
                                StackPane.setMargin(winner, new Insets(0, textSize * 1.5, 0, 0));
                                StackPane.setMargin(currentPlayerTurnLabel, new Insets(0, 0, 0, textSize / 2));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        };

        mainStackPane.setOnMousePressed(mainStackPanePlayerEventHandler);

        IntegerProperty lastBarrierX = new SimpleIntegerProperty(-1);
        IntegerProperty lastBarrierY = new SimpleIntegerProperty(-1);
        Rectangle[] previousGhostBarrier = new Rectangle[1];
        Transition[] lastTransition = new Transition[1];
        mainStackPane.setOnMouseMoved(e -> {
            double x = e.getX() - panePadding;
            double y = e.getY() - panePadding;

            if (previousGhostBarrier[0] != null) {
                if (isBarrierHorizontal.get()) {
                    double prevGBCenterX = previousGhostBarrier[0].getX() - panePadding + (boxSize + gridGap) / 2;
                    double prevGBCenterY = previousGhostBarrier[0].getY() - panePadding + BARRIER_HIT_BOX * (boxSize + gridGap) / 2;

                    if (abs(x - prevGBCenterX) >= (previousGhostBarrier[0].getWidth() + gridGap) / 4 || abs(y - prevGBCenterY) >= previousGhostBarrier[0].getHeight() / 2 || (previousGhostBarrier[0].getX() - panePadding) / (boxSize + gridGap) >= 7) {
                        removeGhostBarrier(previousGhostBarrier, lastTransition, lastBarrierX, lastBarrierY, playersAndBarriersPane);
                    }
                } else {
                    double prevGBCenterX = previousGhostBarrier[0].getX() - panePadding + BARRIER_HIT_BOX * (boxSize + gridGap) / 2;
                    double prevGBCenterY = previousGhostBarrier[0].getY() - panePadding + (boxSize + gridGap) / 2;

                    if (abs(x - prevGBCenterX) >= previousGhostBarrier[0].getWidth() / 2 || abs(y - prevGBCenterY) >= (previousGhostBarrier[0].getHeight() + gridGap) / 4 || (previousGhostBarrier[0].getY() - panePadding) / (boxSize + gridGap) >= 7) {
                        removeGhostBarrier(previousGhostBarrier, lastTransition, lastBarrierX, lastBarrierY, playersAndBarriersPane);
                    }
                }
            }

            updateGhostBarrier(x, y, previousGhostBarrier, lastBarrierX, lastBarrierY, isModeMovePlayer, isBarrierHorizontal, playersAndBarriersPane, panePadding, gridGap, boxSize);
        });

        // Barrier placing handler
        EventHandler<MouseEvent> mainStackPaneBarrierEventHandler = new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    try {
                        Edge[] edges = pxCoordsToBarrierCoords(event.getX(), event.getY(), game.getBoard(), isBarrierHorizontal.get(), panePadding, gridGap, boxSize) ;
                        if (edges != null) {
                            boolean isBarrierValid = currentPlayer.placeBarrier(edges[0], edges[1], game.getBoard(), game.getPlayers(), game.getBoard().getBarriers());
                            if (isBarrierValid) {
                                Rectangle rectangle = createBarrier(edges[0], edges[1], false, game.getBoard(), panePadding, gridGap, boxSize);
                                playersAndBarriersPane.getChildren().add(rectangle);

                                int barrierCount = game.getBoard().getBarriers().size();
                                barrierCountLabel.setText(barrierCount + "/" + BARRIER_LIMIT);
                                if (barrierCount == BARRIER_LIMIT) {
                                    playButton.getStyleClass().clear();
                                    playButton.getStyleClass().add("unavailableButtonMenu");
                                }

                                removeGhostBarrier(previousGhostBarrier, lastTransition, lastBarrierX, lastBarrierY, playersAndBarriersPane);

                                // Remove barrier handlers before going to next turn
                                mainStackPane.removeEventHandler(MouseEvent.MOUSE_PRESSED, this);
                                goToNextTurn(playerListFx, ghostPlayers, playersAndBarriersPane, isModeMovePlayer, isBarrierHorizontal, nbPlayers, size, panePadding, gridGap, boxSize);
                            } else {
                                Animation animation = new Transition() {
                                    {
                                        setCycleDuration(Duration.millis(500));
                                        setInterpolator(Interpolator.EASE_OUT);
                                        lastTransition[0] = this;
                                    }

                                    @Override
                                    protected void interpolate(double v) {
                                        if (previousGhostBarrier[0] != null) previousGhostBarrier[0].setFill(new Color(1 - v, 0, 0, 0.5));
                                    }
                                };


                                previousGhostBarrier[0].setFill(new Color(1, 0, 0, 0.5));
                                animation.play();
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }
        };

        // Handle the switching between horizontal and vertical barrier
        EventHandler<MouseEvent> switchBarrierEventHandler = event -> {
            if (event.getButton() == MouseButton.SECONDARY && !isModeMovePlayer.get()) {
                isBarrierHorizontal.set(!isBarrierHorizontal.get());

                removeGhostBarrier(previousGhostBarrier, lastTransition, lastBarrierX, lastBarrierY, playersAndBarriersPane);
                updateGhostBarrier(event.getX() - panePadding, event.getY() - panePadding, previousGhostBarrier, lastBarrierX, lastBarrierY, isModeMovePlayer, isBarrierHorizontal, playersAndBarriersPane, panePadding, gridGap, boxSize);
            }
        };

        mainStackPane.setOnMouseReleased(switchBarrierEventHandler);

        // Handle the switching and (de)activating the corresponding eventHandler
        playButton.setOnAction(e -> {
            try {
                if (game.checkVictory() == null) {
                    if (isModeMovePlayer.get()) {
                        if (game.getBoard().getBarriers().size() < BARRIER_LIMIT) {
                            isModeMovePlayer.set(false);
                            playButton.setText("Move player");

                            for (Circle ghostPlayer : ghostPlayers) {
                                playersAndBarriersPane.getChildren().remove(ghostPlayer);
                            }

                            mainStackPane.setOnMousePressed(mainStackPaneBarrierEventHandler);
                        }
                    } else {
                        isModeMovePlayer.set(true);
                        playButton.setText("Place a barrier");

                        for (Circle ghostPlayer : ghostPlayers) {
                            playersAndBarriersPane.getChildren().add(ghostPlayer);
                        }

                        mainStackPane.setOnMousePressed(mainStackPanePlayerEventHandler);
                    }
                }
            } catch (UnknownColorException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Proceeds to the next turn and resets properties and handlers
     * @param playerListFx              An array storing the graphical representation of the players
     * @param ghostPlayers              The ArrayList containing all ghost players of the current player
     * @param playersAndBarriersPane    The pane on which the players and barriers are
     * @param isModeMovePlayer          Whether the mode was move player or not
     * @param isBarrierHorizontal       Whether the barrier placing orientation was horizontal or vertical
     * @param nbPlayers                 The number of players in the game
     * @param size                      The size of the game board
     * @param panePadding               The padding value for the pane
     * @param gridGap The gap between each box
     * @param boxSize                   The size of each box in the grid
     */
    private void goToNextTurn(Circle[] playerListFx, ArrayList<Circle> ghostPlayers, Pane playersAndBarriersPane, BooleanProperty isModeMovePlayer, BooleanProperty isBarrierHorizontal, int nbPlayers, int size, double panePadding, long gridGap, double boxSize) throws Exception {
        // Reset properties and handlers
        if (!isModeMovePlayer.get()) isModeMovePlayer.set(true);
        if (!isBarrierHorizontal.get()) isBarrierHorizontal.set(true);
        playButton.setText("Place a barrier");

        // Delete ghost players
        for (Circle ghostPlayer : ghostPlayers) {
            playersAndBarriersPane.getChildren().remove(ghostPlayer);
        }

        ghostPlayers.clear();

        game.setTurnCount(game.getTurnCount() + 1);
        gameTurn(playerListFx, playersAndBarriersPane, nbPlayers, size, panePadding, gridGap, boxSize);
    }

    /**
     * Creates a graphical representation of a player at the specified position.
     * @param id The ID of the player.
     * @param pos The position of the player.
     * @param isGhost Whether the player to create is a ghost player or not
     * @param panePadding The padding value of the pane.
     * @param gridGap The gap between each box
     * @param boxSize The size of each box in the grid.
     * @return The Circle object representing the player.
     */
    private Circle createPlayer(int id, Position pos, boolean isGhost, double panePadding, long gridGap, double boxSize) throws UnknownPlayerIdException {

        double[] pxCoords = playerCoordsToPxCoords(pos, panePadding, gridGap, boxSize);

        Circle player = new Circle(pxCoords[0], pxCoords[1], boxSize / 2.5);

        String color, strokeColor;

        switch (id) {
            case 0 -> {
                color = "#F2F47E";
                strokeColor = "#CF8962";
            }

            case 1 -> {
                color = "#525EC8";
                strokeColor = "#282741";
            }

            case 2 -> {
                color = "#F17171";
                strokeColor = "#A73030";
            }

            case 3 -> {
                color = "#7DE187";
                strokeColor = "#419549";
            }

            default -> throw new UnknownPlayerIdException("This id isn't associated with any player");
        }

        player.setFill(Color.web(color, isGhost ? 0.3 : 1));
        player.setStroke(Color.web(strokeColor, isGhost ? 0.3 : 1));
        player.setStrokeWidth(1.0);

        DropShadow ds = new DropShadow();
        ds.setRadius(4.0);
        ds.setOffsetX(2.0);
        ds.setOffsetY(4.0);
        ds.setColor(Color.web("#000000", 0.25));
        player.setEffect(ds);

        return player;
    }

    /**
     * Converts player coordinates to pixel coordinates on the game board.
     * @param pos The player's position.
     * @param panePadding The padding value for the pane.
     * @param gridGap The gap between each box
     * @param boxSize The size of each box in the grid.
     * @return An array containing the x and y pixel coordinates.
     */
    private double[] playerCoordsToPxCoords(Position pos, double panePadding, long gridGap, double boxSize) {
        double[] px = new double[2];
        px[0] = panePadding + (pos.getX() + 0.5) * boxSize + pos.getX() * gridGap;
        px[1] = panePadding + (pos.getY() + 0.5) * boxSize + pos.getY() * gridGap;

        return px;
    }

    /**
     * Convert pixel coordinate to player coordinate on the game board.
     * @param x The x pixel coordinate.
     * @param y The y pixel coordinate.
     * @param panePadding The padding value for the pane.
     * @param gridGap The gap between each box
     * @param boxSize The size of each box in the grid.
     * @return An array containing the x and y player coordinates.
     */
    private int[] pxCoordsToPlayerCoords(double x, double y, double panePadding, long gridGap, double boxSize) {
        int[] pos = new int[2];

        pos[0] = (int) round((x - panePadding - 0.5*boxSize) / (boxSize + gridGap));
        pos[1] = (int) round((y - panePadding - 0.5*boxSize) / (boxSize + gridGap));

        return pos;
    }

    // For Barrier
    /**
     * Creates a graphical representation of a barrier under or on the right of the two given positions.
     * @param e1 The first edge of the barrier.
     * @param e2 The second edge of the barrier.
     * @param isGhost Whether the barrier is a ghost one or not
     * @param board The game board
     * @param panePadding The padding value for the pane.
     * @param gridGap The gap between each box
     * @param boxSize The size of each box in the grid
     * @return The Rectangle object representing the barrier.
     * @throws BadBarrierEdgesException If the positions do not form a valid horizontal or vertical barrier.
     */
    private Rectangle createBarrier(Edge e1, Edge e2, boolean isGhost, Board board, double panePadding, long gridGap, double boxSize) throws BadBarrierEdgesException {
        Position[] positions1 = new Position[2];
        e1.normalizeEdgePositions(positions1, board);
        Position[] positions2 = new Position[2];
        e2.normalizeEdgePositions(positions2, board);

        Position pos1 = positions1[0];
        Position pos2 = positions2[0];

        if (pos1.toAdjacencyListIndex(graph.getSize()) > pos2.toAdjacencyListIndex(graph.getSize())) {
            Position tempPos = pos1;
            pos1 = pos2;
            pos2 = tempPos;
        }

        Rectangle barrier = new Rectangle();
        barrier.setFill(Color.web("#101548", isGhost ? 0.5 : 1));

        DropShadow ds = new DropShadow();
        ds.setRadius(4.0);
        ds.setOffsetX(2.0);
        ds.setOffsetY(4.0);
        ds.setColor(Color.web("#000000", 0.25));
        barrier.setEffect(ds);

        double barrierX, barrierY, barrierWidth, barrierHeight;

        // Horizontal barrier
        if (pos2.getX() - pos1.getX() == 1 && pos2.getY() - pos1.getY() == 0) {
            barrierX = panePadding + pos1.getX() * (boxSize + gridGap);
            barrierY = panePadding + pos1.getY() * gridGap + (pos1.getY() + 1) * boxSize - gridGap * ((BARRIER_SIZE - 1) / 2);
            barrierWidth = 2 * boxSize + gridGap;
            barrierHeight = gridGap * BARRIER_SIZE;
        } else {
            // Vertical barrier
            if (pos2.getX() - pos1.getX() == 0 && pos2.getY() - pos1.getY() == 1) {
                barrierX = panePadding + pos1.getX() * gridGap + (pos1.getX() + 1) * boxSize - gridGap * ((BARRIER_SIZE - 1) / 2);
                barrierY = panePadding + pos1.getY() * (boxSize + gridGap);
                barrierWidth = gridGap * BARRIER_SIZE;
                barrierHeight = 2 * boxSize + gridGap;
            } else {
                throw new BadBarrierEdgesException("The given edges are not adjacent and thus cannot create a barrier.");
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
     * @param gridGap The gap between each box
     * @param boxSize The size of each box in the grid.
     * @return An array containing the two edges forming the barrier.
     * @throws BadPositionException If the calculated positions are invalid or out of bounds.
     */
    private Edge[] pxCoordsToBarrierCoords(double x, double y, Board board, boolean isBarrierHorizontal, double panePadding, long gridGap, double boxSize) throws BadPositionException {
        Edge[] edges = new Edge[2];

        if (isBarrierHorizontal) {
            double newX = floor((x - panePadding) / (boxSize + gridGap));
            double newY = (y - panePadding + (double) gridGap / 2) / (boxSize + gridGap);

            // If the click was within a little margin in Y, we still accept it and return the closest value
            if (round(newY) > 0 && round(newY) < board.getSize() && abs(round(newY) - newY) <= BARRIER_HIT_BOX) {
                newY = round(newY) - 1;

                Position pos = new Position((int) newX, (int) newY);
                edges[0] = pos.getNeighbourEdges(board).get(Direction.SOUTH);
                edges[1] = pos.getNeighbourPositions(board).get(newX == board.getSize() - 1 ? Direction.WEST : Direction.EAST).getNeighbourEdges(board).get(Direction.SOUTH); // If at the border, the barrier is from right to left instead
            } else {
                return null;
            }
        } else {
            double newX = (x - panePadding + (double) gridGap / 2) / (boxSize + gridGap);
            double newY = floor((y - panePadding) / (boxSize + gridGap));

            // If the click was within a little margin in X, we still accept it and return the closest value
            if (round(newX) > 0 && round(newX) < board.getSize() && abs(round(newX) - newX) <= BARRIER_HIT_BOX) {
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

    /**
     * Updates the displayed ghost barrier
     * @param x                         x coordinate of the cursor on the mainStackPane
     * @param y                         y coordinate of the cursor on the mainStackPane
     * @param previousGhostBarrier      The array containing the previous ghost barrier
     * @param lastBarrierX              The property monitoring the x coordinate of the last placed barrier
     * @param lastBarrierY              The property monitoring the y coordinate of the last placed barrier
     * @param isModeMovePlayer          Whether the current mode is moving player or placing a barrier
     * @param isBarrierHorizontal       Whether the player is trying to place the barrier horizontally or vertically
     * @param playersAndBarriersPane    The pane on which the players and barriers are
     * @param panePadding               The padding value for the pane.
     * @param gridGap                   The gap between each box
     * @param boxSize                   The size of each box in the grid
     */
    private void updateGhostBarrier(double x, double y, Rectangle[] previousGhostBarrier, IntegerProperty lastBarrierX, IntegerProperty lastBarrierY, BooleanProperty isModeMovePlayer, BooleanProperty isBarrierHorizontal, Pane playersAndBarriersPane, double panePadding, long gridGap, double boxSize) {
        if (x > 0 && x < mainStackPane.getHeight() - panePadding * 2 && y > 0 && y < mainStackPane.getHeight() - panePadding * 2) {
            if (!isModeMovePlayer.get()) {
                try {
                    Edge[] edges = pxCoordsToBarrierCoords(x + panePadding, y + panePadding, game.getBoard(), isBarrierHorizontal.get(), panePadding, gridGap, boxSize);
                    if (edges != null && (lastBarrierX.get() != edges[0].getSource().getX() || lastBarrierY.get() != edges[0].getSource().getY())) {
                        Rectangle ghostBarrier = createBarrier(edges[0], edges[1], true, game.getBoard(), panePadding, gridGap, boxSize);
                        playersAndBarriersPane.getChildren().add(ghostBarrier);
                        previousGhostBarrier[0] = ghostBarrier;
                        lastBarrierX.set(edges[0].getSource().getX());
                        lastBarrierY.set(edges[0].getSource().getY());
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }
    }

    /**
     * Clears the previous ghost barrier and all animation and display associated with it
     * @param previousGhostBarrier      The array containing the previous ghost barrier
     * @param lastTransition            The array containing the last transition of the ghost barrier, if any
     * @param lastBarrierX              The property monitoring the x coordinate of the last placed barrier
     * @param lastBarrierY              The property monitoring the y coordinate of the last placed barrier
     * @param playersAndBarriersPane    The pane on which the players and barriers are
     */
    private void removeGhostBarrier(Rectangle[] previousGhostBarrier, Transition[] lastTransition, IntegerProperty lastBarrierX, IntegerProperty lastBarrierY, Pane playersAndBarriersPane) {
        if (lastTransition[0] != null) {
            lastTransition[0].stop();
            lastTransition[0] = null;
        }

        playersAndBarriersPane.getChildren().remove(previousGhostBarrier[0]);
        previousGhostBarrier[0] = null;
        lastBarrierX.set(-1);
        lastBarrierY.set(-1);
    }

    /**
     * Saves the game.
     */
    public void saveGame() {
        CreateSaveDir.createSaveDirWithTest();
        if(saveNameGeneral.isEmpty()) {
            Dialog<SaveFileName> saveNameDialog = new SaveNameDialog(new SaveFileName(""));
            Optional<SaveFileName> saveName = saveNameDialog.showAndWait();
            if(saveName.isPresent()) {
                SerializationUtils.serialisationGame(this.game, saveName.get().getSaveName());
                saveNameGeneral = saveName.get().getSaveName();
            }
        } else {
            SerializationUtils.serialisationGame(this.game, saveNameGeneral);
        }
    }
}
