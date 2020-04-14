package software.lachlanroberts;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class MarkerController {
    @FXML
    private AnchorPane anchor;
    @FXML
    private void initialize() { }

    public void set_anchor(double x, double y) {
        System.out.println("MARKER: " + x + " " + y);
        anchor.setLayoutX(x);
        anchor.setLayoutY(y);
    }
}
