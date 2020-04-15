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

    private WorldPageController world_page_controller;
    private double zoom_level = 1;

    public void new_clicked(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WorldPage.fxml"));
        try {
            stack_pane.getChildren().add(loader.load());
            world_page_controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void zoom_out(ActionEvent actionEvent) {
        zoom_level--;
        zoom();
    }

    public void zoom_in(ActionEvent actionEvent) {
        zoom_level++;
        zoom();
    }
    public void zoom() {
        world_page_controller.set_zoom_level(zoom_level);
    }
}