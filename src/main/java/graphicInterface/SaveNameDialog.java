package graphicInterface;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import saveLoad.SaveFileName;

/**
 *  This class manages the "Save game" window.
 */
public class SaveNameDialog extends Dialog<SaveFileName> {

    private SaveFileName saveName;

    private TextField saveNameField;

    /**
     * Default constructor of the SaveNameDialog class.
     * @param saveName
     */
    public SaveNameDialog(SaveFileName saveName) {
        super();
        this.setTitle("Save game");
        this.saveName = saveName;

        buildUI();
        setPropertyBinding();
        setResultConverter();
    }

    /**
     * Constructs the UI of the DialogPane.
     * Creates a pane and set it as the DialogPane's content, then adds the buttons and an event filter.
     */
    private void buildUI() {
        Pane pane = createPane();
        getDialogPane().setContent(pane);

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().setStyle("-fx-background-color: #282741;");
        ((Button) getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Cancel");

        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setStyle("-fx-background-color: #70b9fd;");
        okButton.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!validateDialog()) {
                    event.consume();
                }
            }

            /**
             * Validates the input of the dialog.
             * @return true if the saveNameField is not empty, false otherwise.
             */
            private boolean validateDialog() {
                return !saveNameField.getText().isEmpty();
            }
        });
    }

    /**
     * Binds the textProperty of the saveNameField to the saveNameProperty of the saveName object.
     */
    private void setPropertyBinding() {
        saveNameField.textProperty().bindBidirectional(saveName.saveNameProperty());
    }

    /**
     * Converts the result of the DialogPane to a SaveFileName object.
     */
    private void setResultConverter() {
        javafx.util.Callback<ButtonType, SaveFileName> saveNameResultConverter = new Callback<ButtonType, SaveFileName>() {
            @Override
            public SaveFileName call(ButtonType buttonType) {
                if(buttonType == ButtonType.OK) {
                    return saveName;
                } else {
                    return null;
                }
            }
        };
        setResultConverter(saveNameResultConverter);
    }

    /**
     * Creates a pane containing the UI.
     * @return the pane containing the UI.
     */
    private Pane createPane() {
        VBox content = new VBox(10);

        Label saveNameLabel = new Label("Save name:");
        saveNameLabel.setStyle("-fx-text-fill: #C1E1FF;");
        this.saveNameField = new TextField();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(5);
        gridPane.add(saveNameLabel, 0, 0);
        gridPane.add(this.saveNameField, 1, 0);
        GridPane.setHgrow(this.saveNameField, Priority.ALWAYS);

        content.getChildren().add(gridPane);

        return content;
    }

}
