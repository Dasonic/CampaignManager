package software.lachlanroberts;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MarkerController {
    @FXML
    private Group group;
    @FXML
    private AnchorPane iconAnchor;
    @FXML
    private ImageView icon;
    @FXML
    private VBox formVbox;
    @FXML
    private TextField titleTextfield;
    @FXML
    private TextArea shortDescriptionTextArea;
    @FXML
    private Hyperlink notePageLink;
    @FXML
    private Button formButton;

    // Marker contents variables
    private String title;
    private String shortDescription;
    private String fullDescription;

    // Marker status variables
    private long lastHidden = 0;

    // Positioning variables
    private double pointX = 0;
    private double pointY = 0;
    private double imgOffsetX;
    private double imgOffsetY;
    private double formVboxXOffset = 0;
    private double formVboxYOffset = 0;

    private LayoutController layoutController;

    public void setLinkedNotePage(NotePageController linkedNotePage) {
        this.linkedNotePage = linkedNotePage;
    }

    private NotePageController linkedNotePage;

    @FXML
    private void initialize() {
        Image newImg = new Image("map-pin.png");
        icon.setImage(newImg);

        imgOffsetX = icon.getImage().getWidth() / 2;
        imgOffsetY = icon.getImage().getHeight();
    }

    public void setZoomLevel(double zoomLevel) {
        setLayout(zoomLevel);
    }

    private void setLayout(double zoomLevel) {
        double x = pointX * zoomLevel;
        double y = pointY * zoomLevel;

        // Set the icon
        iconAnchor.setLayoutX(x - imgOffsetX);
        iconAnchor.setLayoutY(y - imgOffsetY);

        // Set the form
        formVbox.setLayoutX(x - formVboxXOffset);
        formVbox.setLayoutY(y - formVboxYOffset);
    }

    // Calculate where to display the form
    public void setMarkerPoint(double x, double y, double imageWidth, double imageHeight, double zoomLevel) {
        pointX = x;
        pointY = y;

        // Set the form to next to the icon, depending on how close to the edge
        // Base case
//        formVboxYOffset = formVbox.getHeight() / 2;
        formVboxYOffset = 320 / 2.0; // getHeight returns incorrect value, so form height is hard coded
        formVboxXOffset = -imgOffsetX * 2;
        // Too close to bottom of screen
        if ((y + formVbox.getHeight()) >= imageHeight) {
            formVboxYOffset *= 2;
        // Too close to top of screen
        } else if (y < formVbox.getHeight() / 2) {
            formVboxYOffset = imgOffsetY;
            if (y < imgOffsetY) { // Too close to top of screen to create marker
                group.setVisible(false);
                System.err.println("WARNING: Marker too close to top, setting to invisible...");
            }
        }
        // Too close to right side of screen
        if ((x - formVboxXOffset + formVbox.getWidth()) > imageWidth) {
            formVboxXOffset = -formVboxXOffset + formVbox.getWidth();
            if (x > imageWidth - imgOffsetX) { // Too close to right of screen to create marker
                group.setVisible(false);
                System.err.println("WARNING: Marker too close to right side, setting to invisible...");
            }
        }
        setLayout(zoomLevel);
    }

    public void setFormVisible(boolean value) {
        if (System.currentTimeMillis() > (100 + lastHidden)) { // Stop the visibility being changed instantly
            if (!value) { // Remove unsaved changes when hiding
                titleTextfield.setText(title);
                shortDescriptionTextArea.setText(shortDescription);
                titleTextfield.setDisable(true);
                shortDescriptionTextArea.setDisable(true);
                formButton.setText("Edit Marker");
            }
            formVbox.setVisible(value);
            lastHidden = System.currentTimeMillis();
        }
    }

    public boolean isConfirmed() {
        return (title != null);
    }


    @FXML
    public void submitForm() {
        if (titleTextfield.isDisabled()) { // Allow editing
            titleTextfield.setDisable(false);
            shortDescriptionTextArea.setDisable(false);
            notePageLink.setDisable(true);
            formButton.setText("Confirm Marker");
        } else { // Save new details!
            title = titleTextfield.getText();
            shortDescription = shortDescriptionTextArea.getText();
            if (linkedNotePage != null) // Update the note page if its open
                linkedNotePage.fetchMarkerInfo();
            titleTextfield.setDisable(true);
            shortDescriptionTextArea.setDisable(true);
            notePageLink.setDisable(false);
            formButton.setText("Edit Marker");
        }
    }

    public void iconClicked() {
        setFormVisible(true);
    }

    public void setLayoutController(LayoutController layout) { this.layoutController = layout; }

    public String getTitle() {return title;}

    public void setTitle(String newTitle) {
        title = newTitle;
        titleTextfield.setText(newTitle);
    }

    public String getShortDescription() { return shortDescription;}

    public void setShortDescription(String newDescription) {
        shortDescription = newDescription;
        shortDescriptionTextArea.setText(newDescription);
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    @FXML
    private void openTab() {
        layoutController.openTab(title);
    }
}
