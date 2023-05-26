package graphicInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadButtonController {
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
     * Default constructor of the LoadButtonController class.
     */
    public LoadButtonController() {}

    /**
     * Handler for the Load button.
     * If this button is clicked, the user is redirected to the Continue scene.
     * @param event The event that happened. In this case, it is a button click.
     * @throws IOException exception thrown if there is an error.
     */
    public void load(ActionEvent event) throws IOException {
        String file = "continue.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(file));
        this.root = loader.load();

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
