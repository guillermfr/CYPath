package graphicInterface;

import constant.ButtonId;
import exception.BadSizeException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;

import java.io.IOException;

import static constant.GraphicInterfaceSizes.SCREEN_HEIGHT;
import static constant.GraphicInterfaceSizes.SCREEN_WIDTH;

/**
 * This class manages the menu buttons.
 * It allows the user to navigate between scenes of the menu.
 */
public class SceneController {

    /**
     * Stage of the application.
     */
    private Stage stage;
    /**
     * Current scene.
     */
    private Scene scene;
    /**
     * Parent root.
     */
    private Parent root;

    /**
     * Default constructor of the SceneController class.
     */
    public SceneController() {}

    /**
     * Handler for the buttons.
     * Depending on the button, the user will be redirected on a new scene.
     * @param event The event that happened. In this case, it is a button click.
     * @throws Exception exception thrown if there is an error.
     */
    public void switchScene(ActionEvent event) throws Exception {

        // We get the id of the button clicked.
        String id = ((Control)event.getSource()).getId();
        String file = "home.fxml";

        // Depending on the id, there are different scenes.
        switch (id) {
            case ButtonId.NEW_GAME -> file = "newGame.fxml";
            case ButtonId.CONTINUE -> file = "continue.fxml";
            case ButtonId.TWO_PLAYERS, ButtonId.FOUR_PLAYERS -> file = "Board.fxml";
        }

        // We load the corresponding scene.
        FXMLLoader loader = new FXMLLoader(getClass().getResource(file));
        this.root = loader.load();

        GameController gameController;
        if(id.equals(ButtonId.TWO_PLAYERS)) {
            gameController = loader.getController();
            gameController.init(2, 9);
        } else if(id.equals(ButtonId.FOUR_PLAYERS)) {
            gameController = loader.getController();
            gameController.init(4, 9);
        }

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method closes the application.
     * @param event The event that happened. In this case, it is a button click.
     */
    public void exit(ActionEvent event) {
        Platform.exit();
    }

}
