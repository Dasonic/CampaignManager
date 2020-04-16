package software.lachlanroberts;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CampaignManager extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Layout.fxml"));
        primaryStage.setTitle("Campaign Manager");
        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add("styling.css");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
