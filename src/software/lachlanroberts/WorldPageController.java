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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class WorldPageController implements Initializable {
    @FXML
    private ImageView scrollImg;
    @FXML
    private Group groupStack;
    @FXML
    private ScrollPane imgPane;

    public LayoutController parent;

    private double scrollDifferenceY = 0;
    private double scrollDifferenceX = 0;
    private double imageWidth = 0;
    private double imageHeight = 0;
    private double zoomLevel = 1;

    private List<MarkerController> allMarkerControllers = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image new_img = new Image("dndmap.jpg");
        scrollImg.setImage(new_img);
    }

    public void setZoomLevel(double zoom_level) {
        this.zoomLevel = zoom_level;
        // Increase the scale of the image
        scrollImg.setScaleX(zoom_level);
        scrollImg.setScaleY(zoom_level);
        // Put the scroll pane to roughly the same position it was before
        double prevHValue = imgPane.getHvalue();
        double prevVValue = imgPane.getVvalue();
        imgPane.setHvalue(prevHValue);
        imgPane.setVvalue(prevVValue);
        // scale expends from center, so need to move it
        double ratio = (zoom_level - 1) / 2.0;
        scrollImg.setTranslateX(scrollImg.getImage().getWidth() * ratio);
        scrollImg.setTranslateY(scrollImg.getImage().getHeight() * ratio);
        // Now 0,0 is always top left corner, no matter what zoom
        // But the markers also need to be moved
        for (MarkerController markerController : allMarkerControllers) {
            markerController.setZoomLevel(zoom_level);
        }
    }


    private double calculateDifference(double num1, double num2) {
        return num1 - num2;
    }

    public void calculateScrollDifferences() {
        // Calculate the height difference
        double scrollPaneHeight = imgPane.getViewportBounds().getHeight();
        imageHeight = scrollImg.getImage().getHeight() * scrollImg.getScaleY();
        scrollDifferenceY = calculateDifference(imageHeight, scrollPaneHeight);

        // Calculate the width difference
        double scrollPaneWidth = imgPane.getViewportBounds().getWidth();
        imageWidth = scrollImg.getImage().getWidth() * scrollImg.getScaleX();
        scrollDifferenceX = calculateDifference(imageWidth, scrollPaneWidth);
    }


    public void imagePaneMouseClicked(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseButton.PRIMARY) { // If double clicked - create new marker
            // Calculate the X and Y relative to the map (image) instead of the program
            calculateScrollDifferences();
            double selectedY = ((scrollDifferenceY * imgPane.getVvalue()) + mouseEvent.getY()) / zoomLevel;
            double selectedX = ((scrollDifferenceX * imgPane.getHvalue()) + mouseEvent.getX()) / zoomLevel;
            System.out.println("X: " + selectedX + " Y: " + selectedY);

            // Create a new marker
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Marker.fxml"));
            groupStack.getChildren().add(fxmlLoader.load());
            MarkerController marker = fxmlLoader.getController();
            marker.setMarkerPoint(selectedX, selectedY, imageWidth / zoomLevel, imageHeight / zoomLevel, zoomLevel);
            marker.setLayoutController(parent);
            allMarkerControllers.add(marker);
        } else if (mouseEvent.getButton() == MouseButton.PRIMARY) { // If single clicked, set all the marker forms to invisible
            for (MarkerController markerController : allMarkerControllers) {
                markerController.setFormVisible(false);
            }
            if (allMarkerControllers.size() > 0 && !allMarkerControllers.get(allMarkerControllers.size() - 1).isConfirmed()) { // if the marker wasn't confirmed, remove it
                groupStack.getChildren().remove(groupStack.getChildren().size() - 1);
                allMarkerControllers.remove(allMarkerControllers.size() - 1);
            }
        }
    }
    public MarkerController findMarker(String title) {
        for (MarkerController marker : allMarkerControllers) {
            if (marker.getTitle().equals(title))
                return marker;
        }
        return null;
    }

}
