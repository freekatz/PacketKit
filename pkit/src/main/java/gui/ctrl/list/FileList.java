package gui.ctrl.list;

import gui.ctrl.IndexView;
import gui.ctrl.View;
import gui.model.SettingProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.FileHandle;
import util.ViewHandle;

import java.io.File;


public class FileList {
    View view;

    @FXML
    Button openButton;

    @FXML
    ListView<String> fileList;

    public FileList() {}

    public void initialize() {
        ViewHandle.InitializeList(SettingProperty.pcapFileHistory, fileList);

        fileList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    String item = fileList.getSelectionModel().getSelectedItem();
                    String pattern = "\\(.*?\\)";
                    item = item.replaceAll(pattern, "");
                    File file = new File(item);
                    FileHandle.AddLine(SettingProperty.pcapFileHistory, file.getAbsolutePath());
                    ViewHandle.InitializeList(SettingProperty.pcapFileHistory, fileList);
                    StartCapture(item);
                }
            }
        });
    }

    public void setView(View view) {
        this.view = view;
        String type = view.getType();
        if (type.equals("index")) {
            IndexView indexView = (IndexView) view;
            if (indexView.getNifName() == null && fileList.getItems().size() > 0)
                indexView.setPcapFile(fileList.getItems().get(0));

        }

    }

    public ListView<String> getFileList() {
        return fileList;
    }

    public void setFileList(ListView<String> fileList) {
        this.fileList = fileList;
    }

    private void StartCapture(String pcapFile) {
        String type = view.getType();
        IndexView indexView = (IndexView) view;
        if (type.equals("index")) {
            indexView.setType("capture");
            indexView.setNifName(null);
            indexView.setPcapFile(pcapFile);
            ViewHandle.InitializeCenter(indexView);
        }
        indexView.StartCapture("offline");

    }

    @FXML
    private void OpenButtonOnClicked() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        // setting
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PCAP files", "*.pcap"),
                new FileChooser.ExtensionFilter("PCAPNG files", "*.pcapng"),
                new FileChooser.ExtensionFilter("CAP files", "*.cap")
        );
        File file = fileChooser.showOpenDialog(stage);
        if (file==null) return;
        FileHandle.AddLine(SettingProperty.pcapFileHistory, file.getAbsolutePath());
        StartCapture(file.getAbsolutePath());
    }
}
