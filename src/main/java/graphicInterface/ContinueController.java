package graphicInterface;

import constant.GameProperties;
import gameObjects.Game;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import saveLoad.CreateSaveDir;
import saveLoad.SerializationUtils;

import java.io.File;
import java.net.URL;
import java.util.*;

import static constant.GraphicInterfaceSizes.SCREEN_HEIGHT;
import static constant.GraphicInterfaceSizes.SCREEN_WIDTH;

/**
 * This class manages the Continue scene.
 * This scene needs to get the existing saves and display them.
 */
public class ContinueController extends SceneController implements Initializable {

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
     * List of the existing saves.
     */
    @FXML
    ListView<HBox> listContinue;

    /**
     * Borderpane of the scene.
     */
    @FXML
    BorderPane borderPane;

    List<String> saveFiles;

    /**
     * Default constructor of the ContinueController class.
     */
    public ContinueController() {}

    /**
     * This method initialize the Continue scene.
     * We get the saves from the save folder, then display them, and if there are none, we display a message.
     * There are 2 buttons next to the file name: the first one is to resume the game, and the second one is to delete the file.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // We get the path of the save directory
        File saveDir = new File(GameProperties.SAVE_PATH);

        // If the folder exists and isn't empty
        if(saveDir.exists() && Objects.requireNonNull(saveDir.listFiles()).length != 0) {
            saveFiles = new ArrayList<>();

            List<HBox> list = new ArrayList<>();

            // We pass through every file of the save directory and add them to a list of buttons
            for (File file : Objects.requireNonNull(saveDir.listFiles())) {
                saveFiles.add(file.getPath());

                HBox saveHBox = new HBox();
                saveHBox.getStyleClass().add("hBoxListView");
                saveHBox.setMaxWidth(listContinue.getMaxWidth());

                Label saveName = new Label(file.getName());
                saveName.getStyleClass().add("labelSaveName");

                Pane pane = new Pane();
                HBox.setHgrow(pane, Priority.ALWAYS);

                Button resumeButton = new Button("Resume");
                resumeButton.getStyleClass().add("buttonContinueMenu");
                resumeButton.setOnMouseClicked(new EventHandler<>() {
                    /**
                     * Handles the mouse event triggered by selecting a "Continue" option from the list of saved games.
                     * Resumes the game from the selected saved file and navigates to the game board screen.
                     * @param mouseEvent The mouse event triggered by selecting a saved game.
                     */
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        Game resumedGame = SerializationUtils.deserialisationGame(file.getName());
                        if(resumedGame != null) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("Board.fxml"));
                            try {
                                root = loader.load();
                                GameController gameController = loader.getController();
                                // Initialize the game controller with the resumed game and the file name
                                gameController.initResume(resumedGame, file.getName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            stage = (Stage) listContinue.getScene().getWindow();

                            scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
                            stage.setScene(scene);
                            stage.show();
                        }
                    }
                });

                Button deleteSaveButton = new Button("Delete");
                deleteSaveButton.getStyleClass().add("buttonContinueMenu");
                deleteSaveButton.setOnMouseClicked(new EventHandler<>() {
                    /**
                     * Handles the mouse event triggered by selecting a delete option for a saved game.
                     * @param mouseEvent The mouse event triggered by the delete option.
                     */
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        saveFiles.remove(file.getPath());
                        listContinue.getItems().remove(saveHBox);
                        System.out.println(file.delete());
                    }
                });

                saveHBox.getChildren().addAll(saveName, pane, resumeButton, deleteSaveButton);

                list.add(saveHBox);
            }

            // We add the list of buttons to the ListView
            listContinue.setItems(FXCollections.observableList(list));
        } else {
            // If there is no save folder, we create it
            CreateSaveDir.createSaveDir();

            // If there is no save file
            VBox vbox = new VBox();

            // We create a label that says that there is no save file
            Label noSaveFile = new Label("There is no save file available");
            noSaveFile.setStyle("-fx-text-fill: #C1E1FF; -fx-font-family: \"Reem Kufi\"; -fx-font-size: 36;");

            // We add an image
            URL imageURL = getClass().getResource("/img/error_404.png");
            assert imageURL != null;
            Image image = new Image (imageURL.toString());
            ImageView imageView = new ImageView(image);
            imageView.setStyle("-fx-border-color: black; -fx-border-width: 10");

            // We add the label and the image to the VBox and then add it to the BorderPane
            vbox.getChildren().add(imageView);
            vbox.getChildren().add(noSaveFile);
            vbox.setAlignment(Pos.TOP_CENTER);

            borderPane.setCenter(vbox);
        }

        listContinue.setStyle("-fx-control-inner-background: #282741; -fx-background-color: #282741; -fx-control-inner-background-alt: -fx-control-inner-background ;");
    }

}
