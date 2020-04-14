package software.lachlanroberts;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class LayoutController {
    @FXML
    private StackPane stack_pane;
    @FXML
    private ScrollPane scroll_pane;

    public void new_clicked(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        try {
            stack_pane.getChildren().add(loader.load(getClass().getResource("WorldPage.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}