package software.lachlanroberts;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;


public class NotePageController {
    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea shortDescriptionTextArea;
    @FXML
    private TextArea fullDescriptionTextArea;

    private MarkerController linkedMarker;
    private Tab tab;

    public void setLinkedMarker(MarkerController linkedMarker) {
        this.linkedMarker = linkedMarker;
        linkedMarker.setLinkedNotePage(this);
        fetchMarkerInfo();
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }

    public void fetchMarkerInfo() {
        if (tab != null)
            tab.setText(linkedMarker.getTitle());
        titleTextField.setText(linkedMarker.getTitle());
        shortDescriptionTextArea.setText(linkedMarker.getShortDescription());
        fullDescriptionTextArea.setText(linkedMarker.getFullDescription());
    }

    @FXML
    private void setMarkerInfo() {
        linkedMarker.setTitle(titleTextField.getText());
        tab.setText(titleTextField.getText());
        linkedMarker.setShortDescription(shortDescriptionTextArea.getText());
        linkedMarker.setFullDescription(fullDescriptionTextArea.getText());
    }

}
