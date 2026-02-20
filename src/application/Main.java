package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {

    private static Scene mainScene;

    @Override
    public void start(Stage primaryStage) {
        try {
            /*
                BorderPane root = new BorderPane();
                Scene scene = new Scene(root, 400, 400);
                Label label = new Label("My JavaFX Program");
                root.setCenter(label);
                primaryStage.setTitle("JavaFX on my Linux");
                primaryStage.setScene(scene);
                primaryStage.show();
            */

            ScrollPane scrollPane = new FXMLLoader(getClass().getResource("/gui/MainView.fxml")).load();

            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);

            mainScene = new Scene(scrollPane);
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("JavaFX on my Linux");
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Scene getMainScene() {
        return mainScene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}