package software.lachlanroberts;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class WorldPageController implements Initializable {
    @FXML
    private ImageView scroll_img;
    @FXML
    private Group group_stack;
    @FXML
    private ScrollPane img_pane;

    private double scroll_difference_y = 0;
    private double scroll_difference_x = 0;
    private double image_width = 0;
    private double image_height = 0;
    private double zoom_level = 1;

    private List<MarkerController> all_marker_controllers = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image new_img = new Image("dndmap.jpg");
        scroll_img.setImage(new_img);
    }

    public void set_zoom_level(double zoom_level) {
        this.zoom_level = zoom_level;
        var temp_img_width = scroll_img.getImage().getWidth();
        var temp_img_height = scroll_img.getImage().getHeight();
        scroll_img.setScaleX(zoom_level);
        scroll_img.setScaleY(zoom_level);
        // scale expends from center, so need to move it
        double ratio = (zoom_level - 1) / 2.0;
        scroll_img.setTranslateX(temp_img_width * ratio);
        scroll_img.setTranslateY(temp_img_height * ratio);
        // Now 0,0 is always top left corner, no matter what zoom
        // But the markers also need to be moved
        for (MarkerController marker_controller : all_marker_controllers) {
            marker_controller.set_zoom_level(zoom_level);
        }
    }


    private double calculate_difference(double num_1, double num_2) {
        return num_1 - num_2;
    }

    public void calculate_scroll_differences() {
        // Calculate the height difference
        double scroll_pane_height = img_pane.getViewportBounds().getHeight();
        image_height = scroll_img.getImage().getHeight() * scroll_img.getScaleY();
        scroll_difference_y = calculate_difference(image_height, scroll_pane_height);

        // Calculate the width difference
        double scroll_pane_width = img_pane.getViewportBounds().getWidth();
        image_width = scroll_img.getImage().getWidth() * scroll_img.getScaleX();
        scroll_difference_x = calculate_difference(image_width, scroll_pane_width);
    }


    public void image_pane_mouse_clicked(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseButton.PRIMARY) { // If double clicked - create new marker
            calculate_scroll_differences();
            double selected_y = ((scroll_difference_y * img_pane.getVvalue()) + mouseEvent.getY()) / zoom_level;
            double selected_x = ((scroll_difference_x * img_pane.getHvalue()) + mouseEvent.getX()) / zoom_level;
            System.out.println("X: " + selected_x + " Y: " + selected_y);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Marker.fxml"));
            group_stack.getChildren().add(fxmlLoader.load());
            MarkerController marker = fxmlLoader.getController();
            marker.set_marker_point(selected_x, selected_y, image_width / zoom_level, image_height / zoom_level, zoom_level);
            all_marker_controllers.add(marker);
        } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            for (MarkerController marker_controller : all_marker_controllers) {
                marker_controller.set_visible(false);
            }
            if (all_marker_controllers.size() > 0 && !all_marker_controllers.get(all_marker_controllers.size() - 1).is_confirmed()) {
                group_stack.getChildren().remove(group_stack.getChildren().size() - 1);
                all_marker_controllers.remove(all_marker_controllers.size() - 1);
            }
        } else {
            calculate_scroll_differences();
            double selected_y = (scroll_difference_y * img_pane.getVvalue()) + mouseEvent.getY();
            double selected_x = (scroll_difference_x * img_pane.getHvalue()) + mouseEvent.getX();
            Circle circ = new Circle(selected_x, selected_y, 10);
            group_stack.getChildren().add(circ);
        }
    }


}
