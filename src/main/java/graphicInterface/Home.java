package graphicInterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Home class represents the main entry point of the JavaFX application.
 * It loads and displays the "home.fxml" scene.
 */
public class Home extends Application {
    /**
     * The start() method is called when the JavaFX application is launched.
     * It loads the "home.fxml" file, creates a scene, and displays it in the stage.
     * @param stage The primary stage for this application.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1920, 1080);

            stage.setTitle("test");
            stage.setScene(scene);
            stage.show();


        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * The main method is the entry point of the Java application.
     * It launches the application by calling the launch() method.
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}
