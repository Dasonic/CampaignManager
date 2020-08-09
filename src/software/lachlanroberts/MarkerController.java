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

import java.io.Serializable;

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


    private MarkerData data = new MarkerData();

    // Marker status variables
    private long lastHidden = 0;

    private LayoutController layoutController;


    private NotePageController linkedNotePage;

    @FXML
    private void initialize() {
        Image newImg = new Image("map-pin.png");
        icon.setImage(newImg);

        data.imgOffsetX = icon.getImage().getWidth() / 2;
        data.imgOffsetY = icon.getImage().getHeight();
    }

    public MarkerData getData() {
        return data;
    }

    public void setData(MarkerData data) {
        this.data = data;
        updateForm();
        setLayout(1); // Calculate where to display the marker
        notePageLink.setDisable(false); // Allow the link to be opened
    }
    public void setLinkedNotePage(NotePageController linkedNotePage) {
        this.linkedNotePage = linkedNotePage;
    }

    public void setZoomLevel(double zoomLevel) {
        setLayout(zoomLevel);
    }

    private void setLayout(double zoomLevel) {
        double x = data.pointX * zoomLevel;
        double y = data.pointY * zoomLevel;

        // Set the icon
        iconAnchor.setLayoutX(x - data.imgOffsetX);
        iconAnchor.setLayoutY(y - data.imgOffsetY);

        // Set the form
        formVbox.setLayoutX(x - data.formVboxXOffset);
        formVbox.setLayoutY(y - data.formVboxYOffset);
    }

    // Calculate where to display the form
    public void setMarkerPoint(double x, double y, double imageWidth, double imageHeight, double zoomLevel) {
        data.pointX = x;
        data.pointY = y;

        // Set the form to next to the icon, depending on how close to the edge
        // Base case
//        formVboxYOffset = formVbox.getHeight() / 2;
        data.formVboxYOffset = 320 / 2.0; // getHeight returns incorrect value, so form height is hard coded
        data.formVboxXOffset = -data.imgOffsetX * 2;
        // Too close to bottom of screen
        if ((y + formVbox.getHeight()) >= imageHeight) {
            data.formVboxYOffset *= 2;
        // Too close to top of screen
        } else if (y < formVbox.getHeight() / 2) {
            data.formVboxYOffset = data.imgOffsetY;
            if (y < data.imgOffsetY) { // Too close to top of screen to create marker
                group.setVisible(false);
                System.err.println("WARNING: Marker too close to top, setting to invisible...");
            }
        }
        // Too close to right side of screen
        if ((x - data.formVboxXOffset + formVbox.getWidth()) > imageWidth) {
            data.formVboxXOffset = -data.formVboxXOffset + formVbox.getWidth();
            if (x > imageWidth - data.imgOffsetX) { // Too close to right of screen to create marker
                group.setVisible(false);
                System.err.println("WARNING: Marker too close to right side, setting to invisible...");
            }
        }
        setLayout(zoomLevel);
    }

    private void updateForm() {
        titleTextfield.setText(data.title);
        shortDescriptionTextArea.setText(data.shortDescription);
    }

    public void setFormVisible(boolean value) {
        if (System.currentTimeMillis() > (100 + lastHidden)) { // Stop the visibility being changed instantly
            if (!value) { // Remove unsaved changes when hiding
                updateForm();
                titleTextfield.setDisable(true);
                shortDescriptionTextArea.setDisable(true);
                formButton.setText("Edit Marker");
            }
            formVbox.setVisible(value);
            lastHidden = System.currentTimeMillis();
        }
    }

    public boolean isConfirmed() {
        return (data.title != null);
    }


    @FXML
    public void submitForm() {
        if (titleTextfield.isDisabled()) { // Allow editing
            titleTextfield.setDisable(false);
            shortDescriptionTextArea.setDisable(false);
            notePageLink.setDisable(true);
            formButton.setText("Confirm Marker");
        } else { // Save new details!
            data.title = titleTextfield.getText();
            data.shortDescription = shortDescriptionTextArea.getText();
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

    public String getTitle() {return data.title;}

    public void setTitle(String newTitle) {
        data.title = newTitle;
        titleTextfield.setText(newTitle);
    }

    public String getShortDescription() { return data.shortDescription;}

    public void setShortDescription(String newDescription) {
        data.shortDescription = newDescription;
        shortDescriptionTextArea.setText(newDescription);
    }

    public String getFullDescription() {
        return data.fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.data.fullDescription = fullDescription;
    }

    @FXML
    private void openTab() {
        layoutController.openTab(data.title);
    }
}
