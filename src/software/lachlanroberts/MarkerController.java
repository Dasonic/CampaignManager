package software.lachlanroberts;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MarkerController {
    @FXML
    private AnchorPane icon_anchor;
    @FXML
    private Group group;
    @FXML
    private ImageView icon;
    @FXML
    private TextField title_textfield;
    @FXML
    private TextArea description_textarea;
    @FXML
    private Button form_button;
    @FXML
    private VBox form_vbox;

    private double img_offset_x;
    private double img_offset_y;
    private String title;
    private String description;
    private long last_hidden = 0;

    @FXML
    private void initialize() {
        Image new_img = new Image("map-pin.png");
        icon.setImage(new_img);

        img_offset_x = icon.getImage().getWidth() / 2;
        img_offset_y = icon.getImage().getHeight();
    }

    public void set_marker_point(double x, double y, double image_width, double image_height) {
        // Set the icon to where the mouse clicked
        icon_anchor.setLayoutX(x - img_offset_x);
        icon_anchor.setLayoutY(y - img_offset_y);

        // Set the form to next to the icon, depending on how close to the edge
        // Base case
        double form_vbox_y_offset = form_vbox.getHeight() / 2;
        double form_vbox_x_offset = - img_offset_x * 2;
        // Too close to bottom of screen
        if ((y + form_vbox.getHeight()) >= image_height) {
            form_vbox_y_offset *= 2;
        // Too close to top of screen
        } else if (y < form_vbox.getHeight() / 2) {
            form_vbox_y_offset = img_offset_y;
            if (y < img_offset_y) { // Too close to top of screen to create marker
                group.setVisible(false);
                System.err.println("WARNING: Marker too close to top, setting to invisible...");
            }
        }
        // Too close to right side of screen
        if ((x - form_vbox_x_offset + form_vbox.getWidth()) > image_width) {
            form_vbox_x_offset = -form_vbox_x_offset + form_vbox.getWidth();
            if (x > image_width - img_offset_x) { // Too close to right of screen to create marker
                group.setVisible(false);
                System.err.println("WARNING: Marker too close to right side, setting to invisible...");
            }
        }

        form_vbox.setLayoutY(y - form_vbox_y_offset);
        form_vbox.setLayoutX(x - form_vbox_x_offset);

    }

    public void set_visible(boolean value) {
        if (System.currentTimeMillis() > (100 + last_hidden)) {
            if (!value) { // Remove unsaved changes when hiding
                title_textfield.setText(title);
                description_textarea.setText(description);
                title_textfield.setDisable(true);
                description_textarea.setDisable(true);
            }
            form_vbox.setVisible(value);
            last_hidden = System.currentTimeMillis();
        }
    }

    public boolean is_confirmed() {
        return (title != null);
    }


    public void submit_form(MouseEvent mouseEvent) {
        if (title_textfield.isDisabled()) { // Allow editing
            title_textfield.setDisable(false);
            description_textarea.setDisable(false);
            form_button.setText("Confirm Marker");
        } else { // Save new details!
            title = title_textfield.getText();
            description = description_textarea.getText();
            title_textfield.setDisable(true);
            description_textarea.setDisable(true);
            form_button.setText("Edit Marker");
        }
    }

    public void icon_clicked(MouseEvent mouseEvent) {
        set_visible(true);
    }
}
