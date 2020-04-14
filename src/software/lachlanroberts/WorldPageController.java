package software.lachlanroberts;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image new_img = new Image("dndmap.jpg");
        scroll_img.setImage(new_img);
    }


    private double calculate_difference(double num_1, double num_2) {
        return num_1 - num_2;
    }

    public void calculate_scroll_differences() {
        // Calculate the height difference
        double scroll_pane_height = img_pane.getViewportBounds().getHeight();
        image_height = scroll_img.getImage().getHeight();
        scroll_difference_y = calculate_difference(image_height, scroll_pane_height);

        // Calculate the width difference
        double scroll_pane_width = img_pane.getViewportBounds().getWidth();
        image_width = scroll_img.getImage().getWidth();
        scroll_difference_x = calculate_difference(image_width, scroll_pane_width);
    }


    public void image_pane_mouse_clicked(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2) {
            calculate_scroll_differences();
            double selected_y = (scroll_difference_y * img_pane.getVvalue()) + mouseEvent.getY();
            double selected_x = (scroll_difference_x * img_pane.getHvalue()) + mouseEvent.getX();
            System.out.println("X: " + selected_x + " Y: " + selected_y);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Marker.fxml"));
            group_stack.getChildren().add(fxmlLoader.load());
            MarkerController marker = fxmlLoader.<MarkerController>getController();
            marker.set_marker_point(selected_x, selected_y, image_width, image_height);
        }
    }


}
