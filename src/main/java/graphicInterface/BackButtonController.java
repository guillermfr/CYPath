package graphicInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.stage.Stage;

import java.io.IOException;

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

    @FXML
    Button back;

    public void back(ActionEvent event) throws IOException {
        String file = "home.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(file));
        this.root = loader.load();

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
