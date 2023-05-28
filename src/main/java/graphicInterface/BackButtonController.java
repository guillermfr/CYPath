package graphicInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static constant.GraphicInterfaceSizes.SCREEN_HEIGHT;
import static constant.GraphicInterfaceSizes.SCREEN_WIDTH;

/**
 * This class manages the back button.
 */
public class BackButtonController {

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
     * Default constructor of the BackButtonController class.
     */
    public BackButtonController() {}

    /**
     * Handler for the back button.
     * If this button is clicked, the user is redirected to the Home scene.
     * @param event The event that happened. In this case, it is a button click.
     * @throws IOException exception thrown if there is an error.
     */
    public void back(ActionEvent event) throws IOException {
        String file = "home.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(file));
        this.root = loader.load();

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }
}
