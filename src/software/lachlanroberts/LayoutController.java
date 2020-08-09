package software.lachlanroberts;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class LayoutController {
    @FXML
    private TabPane tabPane;
    @FXML
    private ImageView zoomInImg;
    @FXML
    private ImageView zoomOutImg;
    @FXML
    private Button zoomInButton;
    @FXML
    private Button zoomOutButton;
    @FXML
    private Tab worldMapTab;

    private WorldPageController worldPageController;
    private double zoomLevel = 1;
    private String saveFolderLocation = null;

    @FXML
    private void initialize() {
        // Set up the zoom icons
        Image zoomInIcon = new Image("zoom-in.png");
        Image zoomOutIcon = new Image("zoom-out.png");
        zoomInImg.setImage(zoomInIcon);
        zoomOutImg.setImage(zoomOutIcon);
        zoomInButton.setGraphic(zoomInImg);
        zoomOutButton.setGraphic(zoomOutImg);
    }

    // Create a new scene (modal) to display newCampaignModal
    public void newClicked() throws IOException {
        Stage newCampaignModal = new Stage();
        newCampaignModal.setTitle("New Campaign");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewCampaignModal.fxml"));
        Parent root = fxmlLoader.load();
        NewCampaignModalController newCampaignModalController = fxmlLoader.getController();
        Scene newCampaignScene = new Scene(root, 600, 400);
        newCampaignModal.setScene(newCampaignScene);
        newCampaignModalController.setLayoutController(this);
        newCampaignModal.showAndWait();
    }

    // Create a new scene (modal) to display loadCampaignModal
    public void openClicked() throws IOException {
        Stage loadCampaignModal = new Stage();
        loadCampaignModal.setTitle("Load Campaign");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoadCampaignModal.fxml"));
        Parent root = fxmlLoader.load();
        LoadCampaignModalController loadCampaignModalController = fxmlLoader.getController();
        Scene loadCampaignScene = new Scene(root, 600, 200);
        loadCampaignModal.setScene(loadCampaignScene);
        loadCampaignModalController.setLayoutController(this);
        loadCampaignModal.showAndWait();
    }


    public void loadCampaign(String mapURL, String saveFolderLocation, List<MarkerData>markerData) {
        this.saveFolderLocation = saveFolderLocation;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WorldPage.fxml"));
        try {
            worldMapTab.setContent(loader.load());
            worldPageController = loader.getController();
            worldPageController.parent = this;
            worldPageController.setMap(mapURL);

            for (MarkerData data : markerData) {
                worldPageController.loadMarker(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void zoomOut() {
        if (zoomLevel != 1 && worldPageController != null) {
            zoomLevel--;
            zoom();
        }
    }

    public void zoomIn() {
        if (worldPageController != null) {
            zoomLevel++;
            zoom();
        }
    }
    public void zoom() {
        worldPageController.setZoomLevel(zoomLevel);
    }

    private Tab createNotePageTab(MarkerController marker) {
        Tab newPageTab = new Tab(marker.getTitle());
        newPageTab.setClosable(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NotePage.fxml"));
        NotePageController newPageController;
        try {
            newPageTab.setContent(loader.load());
            newPageController = loader.getController();
            newPageController.setTab(newPageTab);
            newPageController.setLinkedMarker(marker);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newPageTab;
    }


    public void openTab(String tab_name) {
        Tab tabFound = null;
        // First check if the tab is open
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals(tab_name)) { // Switch to the tab and end function
                tabFound = tab;
            }
        }
        // If not, create a new tab
        if (tabFound == null) {
            MarkerController markerFound = worldPageController.findMarker(tab_name);
            if (markerFound != null) {
                tabFound = createNotePageTab(markerFound);
                tabPane.getTabs().add(tabFound);
            }
            else { // If a marker isn't found, print error and abort tab opening
                System.err.println("ERROR: Marker not found, tab opening failed.");
                return;
            }
        }
        // Switch to tab
        tabPane.getSelectionModel().select(tabFound);
    }

    public void saveClicked(ActionEvent actionEvent) {
        // Get the data from all the markers to write to a file
        List<MarkerController> allMarkerControllers = worldPageController.getAllMarkerControllers();
        List<MarkerData> allMarkerData = new ArrayList<>();
        for (MarkerController markerController : allMarkerControllers) {
            allMarkerData.add(markerController.getData());
        }
        
        try {
            FileOutputStream writeData = new FileOutputStream(saveFolderLocation + "/Markers.ser");
            ObjectOutputStream writeStream = new ObjectOutputStream(writeData);

            writeStream.writeObject(allMarkerData);
            writeStream.flush();
            writeStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}