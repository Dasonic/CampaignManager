package software.lachlanroberts;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class MarkerController {
    @FXML
    private AnchorPane anchor;
    @FXML
    private ImageView icon;
    @FXML
    private Label title_label;
    @FXML
    private TextField title_textfield;
    @FXML
    private GridPane form_grid;
    @FXML
    private GridPane outer_grid;

    private double img_offset_x;
    private double img_offset_y;

    @FXML
    private void initialize() {
        Image new_img = new Image("map-pin.png");
        icon.setImage(new_img);

        img_offset_x = icon.getImage().getWidth() / 2;
        img_offset_y = icon.getImage().getHeight();
    }

    public void set_marker_point(double x, double y, double image_width, double image_height) {
        double anchor_offset_x = img_offset_x;
        double anchor_offset_y = img_offset_y * 2;

        double grid_height = 2 * 25 + 20;
        double grid_width = 150 + 75 + 20;

        if ((grid_height + img_offset_y + y > image_height)) { // Form near bottom of screen
            GridPane.setRowIndex(form_grid, 0);
            GridPane.setRowIndex(icon, 1);
            // In this case, the anchor point will need to be the icon height + the form height
            anchor_offset_y = img_offset_y + grid_height;
        }
        if (y < grid_height) { // Form near top of screen
            if (y < img_offset_y) { // If the form would extend the screen
                anchor.setVisible(false);
                System.err.println("WARNING: Marker too close to top, setting to invisible...");
            }
            GridPane.setRowIndex(form_grid, 1);
            GridPane.setRowIndex(icon, 0);
            // In this case, the anchor point will need to be the icon height + the form height
            anchor_offset_y = img_offset_y;
        }
        if ((grid_width + img_offset_x + x) > image_width) { // Form near right hand side of screen
            if (x > image_width - img_offset_x) { // If the form would extend the screen
                anchor.setVisible(false);
                System.err.println("WARNING: Marker too close to right, setting to invisible...");
            }
            GridPane.setColumnIndex(form_grid, 0);
            GridPane.setColumnIndex(icon, 1);
            // In this case, the anchor point will need to be the icon width + the form width
            anchor_offset_x = img_offset_x + grid_width;
        }

        anchor.setLayoutX(x - anchor_offset_x);
        anchor.setLayoutY(y - anchor_offset_y);
    }

    public void mouse_entered(MouseEvent mouseEvent) {
        title_label.setVisible(true);
    }

    public void mouse_exited(MouseEvent mouseEvent) {
        title_label.setVisible(false);
    }
}
