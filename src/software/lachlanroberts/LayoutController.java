package software.lachlanroberts;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class LayoutController {
    @FXML
    private StackPane stack_pane;
    @FXML
    private ImageView zoom_in_img;
    @FXML
    private ImageView zoom_out_img;
    @FXML
    private Button zoom_in_button;
    @FXML
    private Button zoom_out_button;

    private WorldPageController world_page_controller;
    private double zoom_level = 1;

    @FXML
    private void initialize() {
        Image zoom_in_icon = new Image("zoom-in.png");
        Image zoom_out_icon = new Image("zoom-out.png");
        zoom_in_img.setImage(zoom_in_icon);
        zoom_out_img.setImage(zoom_out_icon);
        zoom_in_button.setGraphic(zoom_in_img);
        zoom_out_button.setGraphic(zoom_out_img);
    }

    public void new_clicked(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WorldPage.fxml"));
        try {
            stack_pane.getChildren().add(loader.load());
            world_page_controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        zoom_hbox.toFront();
    }

    public void zoom_out() {
        if (zoom_level != 1 && world_page_controller != null) {
            zoom_level--;
            zoom();
        }
    }

    public void zoom_in() {
        if (world_page_controller != null) {
            zoom_level++;
            zoom();
        }
    }
    public void zoom() {
        world_page_controller.set_zoom_level(zoom_level);
    }
}