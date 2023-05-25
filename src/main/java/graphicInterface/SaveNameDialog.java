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

public class SaveNameDialog extends Dialog<SaveFileName> {

    private SaveFileName saveName;

    private TextField saveNameField;

    public SaveNameDialog(SaveFileName saveName) {
        super();
        this.setTitle("Save game");
        this.saveName = saveName;

        buildUI();
        setPropertyBinding();
        setResultConverter();
    }

    private void buildUI() {
        Pane pane = createPane();
        getDialogPane().setContent(pane);
        // TODO : add CSS ?

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!validateDialog()) {
                    event.consume();
                }
            }

            private boolean validateDialog() {
                return !saveNameField.getText().isEmpty();
            }
        });
    }

    private void setPropertyBinding() {
        saveNameField.textProperty().bindBidirectional(saveName.saveNameProperty());
    }

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

    private Pane createPane() {
        VBox content = new VBox(10);

        Label saveNameLabel = new Label("Save name:");
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
