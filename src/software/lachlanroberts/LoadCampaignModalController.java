package software.lachlanroberts;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class LoadCampaignModalController {
    @FXML
    private Button cancelButton;
    @FXML
    private TextField saveFolderLocationTextField;

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
//        directoryChooser.setInitialDirectory();

        File directory = directoryChooser.showDialog(folderSelectStage);
        if (directory != null)
            saveFolderLocation = directory.toString();
            saveFolderLocationTextField.setText(saveFolderLocation);
    }

    @FXML
    private void loadSave() {
        if (saveFolderLocation != null) {
            String mapSavePath = saveFolderLocation + "/map.png";
            if (layoutController != null) {
                layoutController.loadCampaign(mapSavePath);
                closeModal();
            }
        }
    }

}
