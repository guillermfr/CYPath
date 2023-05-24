package graphicInterface;

import enumeration.Direction;
import exception.BadPositionException;
import exception.BadSizeException;
import gameObjects.Board;
import gameObjects.Game;
import gameObjects.Player;
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

import static java.lang.Math.floor;

public class GameController {

    private EdgeWeightedGraph graph;
    private final static int STACK_PANE_HEIGHT = 400;
    private final static int PANE_PADDING = 10;
    private final static int GRID_GAP = 5;
    private final static int SCREEN_WIDTH = 1920;
    private final static int SCREEN_HEIGHT = 1080;
    private final static double BARRIER_SIZE = 1.4;

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
        initPlayers(nbPlayers, size, game, playerListFx, panePadding, boxSize);
        playersAndBarriersPane.getChildren().addAll(playerListFx);

        // Turns
        gameTurn(nbPlayers, size, game, playerListFx, panePadding, boxSize);
    }

    private void initPlayers(int nbPlayers, int size, Game game, Circle[] playerListFx, double panePadding, double boxSize) {
        try {
            Board board = new Board(size);
            game.setBoard(board);
            game.initGame(nbPlayers, size);

            if (nbPlayers >= 2) {
                Circle yellowPlayer = createPlayer(game.getPlayers().get(0).getPosition(), panePadding, boxSize, Color.web("#F2F47E"), 0);
                Circle bluePlayer = createPlayer(game.getPlayers().get(1).getPosition(), panePadding, boxSize, Color.web("#525EC8"), 1);
                playerListFx[0] = yellowPlayer;
                playerListFx[1] = bluePlayer;
            }

            if (nbPlayers == 4) {
                Circle redPlayer = createPlayer(game.getPlayers().get(2).getPosition(), panePadding, boxSize, Color.web("#F17171"), 2);
                Circle greenPlayer = createPlayer(game.getPlayers().get(3).getPosition(), panePadding, boxSize, Color.web("#7DE187"), 3);
                playerListFx[2] = redPlayer;
                playerListFx[3] = greenPlayer;
            }
        } catch (BadSizeException e) {
            System.out.println(e);
        }
    }

    private void gameTurn(int nbPlayers, int size, Game game, Circle[] playerListFx, double panePadding, double boxSize) {
        int currentPlayerId = game.getCurrentPlayerTurn(nbPlayers);
        Player currentPlayer = game.getPlayers().get(currentPlayerId);

        // Update player turn on top
        currentPlayerTurnLabel.setText(currentPlayer.getName() + "'s Turn");

        // Update turn count on the side
        turnCountLabel.setText("Turn " + (game.getTurnCount() + 1));

        // Handle player's turn
        // For placing barriers
        EventHandler<MouseEvent> rootPaneEventHandler = event -> {
            BooleanProperty isBarrierHorizontal = new SimpleBooleanProperty(true);
            if (event.getButton() == MouseButton.PRIMARY) {
                mainStackPane.setOnMouseClicked(e -> {
                    try {
                        Position[] positions = pxCoordsToBarrierCoords(e.getX(), e.getY(), isBarrierHorizontal.get(), panePadding, boxSize, game.getBoard());
                        System.out.println(positions[0]);
                        System.out.println(positions[1]);
                    } catch (BadPositionException ex) {
                        System.out.println(ex);
                    }
                });
            }

            if (event.getButton() == MouseButton.SECONDARY) {
                isBarrierHorizontal.set(!isBarrierHorizontal.get());
            }
        };

        // For moving the player
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
                            game.setTurnCount(game.getTurnCount() + 1);
                            gameTurn(nbPlayers, size, game, playerListFx, panePadding, boxSize);
                        } else System.out.println("Victoire de " + currentPlayer.getName()); // TODO : Victory screen or sth like that
                    }

                    System.out.println(newPos);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        };

        // Handle the switching and (de)activating the corresponding eventHandler
        // Monitor the current mode between move player (0) and placing barrier (1)
        BooleanProperty isModeMovePlayer = new SimpleBooleanProperty(true);
        playButton.setOnAction(e -> {
            if (isModeMovePlayer.get()) {
                isModeMovePlayer.set(false);
                playButton.setText("Move player");
                mainStackPane.setOnMouseClicked(null);
                rootPane.setOnMouseClicked(rootPaneEventHandler);
            } else {
                isModeMovePlayer.set(true);
                playButton.setText("Place a barrier");
                rootPane.setOnMouseClicked(null);
                mainStackPane.setOnMouseClicked(mainStackPanePlayerEventHandler);
            }
        });

        mainStackPane.setOnMouseClicked(mainStackPanePlayerEventHandler);
    }

    private Circle createPlayer(Position pos, double panePadding, double boxSize, Color color, int id) {
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

    // For Player
    private double[] playerCoordsToPxCoords(Position pos, double panePadding, double boxSize) {
        double[] px = new double[2];
        px[0] = panePadding + (pos.getX() + 0.5) * boxSize + pos.getX() * GRID_GAP;
        px[1] = panePadding + (pos.getY() + 0.5) * boxSize + pos.getY() * GRID_GAP;

        return px;
    }

    private int[] pxCoordsToPlayerCoords(double x, double y, double panePadding, double boxSize) {
        int[] pos = new int[2];

        pos[0] = (int) Math.round((x - panePadding - 0.5*boxSize) / (boxSize + GRID_GAP));
        pos[1] = (int) Math.round((y - panePadding - 0.5*boxSize) / (boxSize + GRID_GAP));

        return pos;
    }

    // For Barrier
    /**
     * Creates a barrier under or on the right of the two given positions
     * @param pos1
     * @param pos2
     * @param boxSize
     * @param panePadding
     * @return
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

        double barrierX = 0, barrierY = 0, barrierWidth = 0, barrierHeight = 0;

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

    Position[] pxCoordsToBarrierCoords(double x, double y, boolean isBarrierHorizontal, double panePadding, double boxSize, Board board) throws BadPositionException {
        Position[] positions = new Position[2];

        if (isBarrierHorizontal) {
            int newX = (int) floor((x - panePadding) / (boxSize + GRID_GAP)); // TODO : find the right x fork to have a nice barrier hitbox with ifs
            int newY = (int) floor((y - panePadding) / (boxSize + GRID_GAP)); // TODO : find the right y fork to have a nice barrier hitbox with ifs
            positions[0] = new Position(newX, newY);
            positions[1] = positions[0].getNeighbourPositions(board).get(Direction.EAST);
        }
        // TODO : isBarrierVertical

        return positions;
    }
}
