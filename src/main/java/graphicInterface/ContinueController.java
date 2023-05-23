package graphicInterface;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * This class manages the Continue scene.
 * This scene needs to get the existing saves and display them.
 */
public class ContinueController extends SceneController implements Initializable {

    /**
     * List of the existing saves.
     */
    @FXML
    ListView<Button> listContinue;

    /**
     * Borderpane of the scene.
     */
    @FXML
    BorderPane borderPane;

    /**
     * This method initialize the Continue scene.
     * We get the saves from the save folder, then display them, and if there are none, we display a message.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // We get the path of the save directory
        URL saveURL = getClass().getResource("/save");
        // If the folder exists and isn't empty
        if(saveURL != null && !saveURL.getPath().isEmpty()) {
            File saveDir = null;

            try {
                saveDir = new File(saveURL.toURI());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

            List<Button> list = new ArrayList<>();

            // We pass through every file of the save directory and add them to a list of buttons
            for (File file : saveDir.listFiles()) {
                Button button = new Button(file.getName());
                button.getStyleClass().add("button");
                button.setMaxWidth(Double.MAX_VALUE);

                list.add(button);
            }

            // We add the list of buttons to the ListView
            listContinue.setItems(FXCollections.observableList(list));
        } else {
            // If there is no save file
            VBox vbox = new VBox();

            // We create a label that says that there is no save file
            Label noSaveFile = new Label("There is no save file available");
            noSaveFile.setStyle("-fx-text-fill: #C1E1FF; -fx-font-family: \"Reem Kufi\"; -fx-font-size: 36;");

            // We add an image
            URL imageURL = getClass().getResource("/img/error_404.png");
            Image image = new Image (imageURL.toString());
            ImageView imageView = new ImageView(image);
            imageView.setStyle("-fx-border-color: black; -fx-border-width: 10");

            // We had the label and the image to the VBox and then add it to the BorderPane
            vbox.getChildren().add(imageView);
            vbox.getChildren().add(noSaveFile);
            vbox.setAlignment(Pos.TOP_CENTER);

            borderPane.setCenter(vbox);
        }

        listContinue.setStyle("-fx-control-inner-background: #282741; -fx-background-color: #282741; -fx-control-inner-background-alt: -fx-control-inner-background ;");
    }

}
