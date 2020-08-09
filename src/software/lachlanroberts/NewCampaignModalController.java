package software.lachlanroberts;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewCampaignModalController {
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField fileTextField;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField saveFolderLocationTextField;

    File selectedFile;
    String saveFolderLocation;

    public void setLayoutController(LayoutController layoutController) {
        this.layoutController = layoutController;
    }

    LayoutController layoutController;

    public void closeModal() {
        Stage stage = (Stage)cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void openSaveFileExplorer() {
        Stage folderSelectStage = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a save directory");
        System.out.println(directoryChooser.getInitialDirectory());
//        directoryChooser.setInitialDirectory();
        File directory = directoryChooser.showDialog(folderSelectStage);
        if (directory != null)
            saveFolderLocation = directory.toString();
            saveFolderLocationTextField.setText(saveFolderLocation + titleTextField.getText()); // TODO: bind this to update with title field
    }

    @FXML
    private void openImageFileExplorer() {
        Stage fileSelectStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a map image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
        selectedFile = fileChooser.showOpenDialog(fileSelectStage);
        if (selectedFile != null)
            fileTextField.setText(selectedFile.getName());
    }

    @FXML
    private void createSave() {
        // TODO: check if files already exist
        if (selectedFile != null && saveFolderLocation != null) {
            saveFolderLocation += "/" + titleTextField.getText(); // Add a new folder to the directory

            Image map = new Image(selectedFile.toURI().toString());
            BufferedImage bufferedMap = SwingFXUtils.fromFXImage(map, null); // Convert to a buffered image so it can be saved
            String mapSavePath = saveFolderLocation + "/map.png";
            try {
                ImageIO.write(bufferedMap, "png", new File(mapSavePath)); // TODO Create folder is it does not exist
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (layoutController != null) {
                List<MarkerData> allMarkerData = new ArrayList<>();
                layoutController.loadCampaign(mapSavePath, saveFolderLocation, allMarkerData);
                closeModal();
            }
        } else {
            System.err.println("ERROR: Cannot create save file. Please select a map and a save folder location.");
        }
    }
}
