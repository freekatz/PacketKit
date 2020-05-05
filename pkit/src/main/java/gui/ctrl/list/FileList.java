package gui.ctrl.list;

import gui.ctrl.IndexView;
import gui.ctrl.View;
import gui.model.SettingProperty;
import gui.model.history.CapturePcapFileHistoryProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.FileHandle;
import util.ViewHandle;

import java.io.File;


public class FileList {
    SettingProperty settingProperty = new SettingProperty();

    View view;

    @FXML
    Button openButton;

    @FXML
    ListView<String> fileList;

    public FileList() {}

    public void initialize() {
        openButton.setTooltip(new Tooltip("open the pcap file"));

        ViewHandle.InitializePcapFileList(settingProperty.capturePcapFileHistory, fileList);

        fileList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    String item = fileList.getSelectionModel().getSelectedItem();
                    String pattern = "\\(.*?\\)";
                    item = item.replaceAll(pattern, "");
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
        for (int i = 2; i < indexView.getCaptureMenuBarCtrl().getRecentMenu().getItems().size(); i++) {
            RadioMenuItem item = (RadioMenuItem) indexView.getCaptureMenuBarCtrl().getRecentMenu().getItems().get(i);
            if (item.getText().contains(pcapFile))
                item.setSelected(true);
        }
        if (type.equals("index")) {
            indexView.setType("capture");
            indexView.setNifName(null);
            indexView.setPcapFile(pcapFile);
            ViewHandle.InitializeCaptureCenter(indexView);
        }
        indexView.StartCapture("offline");

    }

    @FXML
    private void OpenButtonOnClicked() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FileChooser fileChooser = new FileChooser();
        // setting
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PCAP files", "*.pcap"),
                new FileChooser.ExtensionFilter("PCAPNG files", "*.pcapng"),
                new FileChooser.ExtensionFilter("CAP files", "*.cap")
        );
        File file = fileChooser.showOpenDialog(stage);
        if (file==null) return;
        FileHandle.AddHistory(settingProperty.capturePcapFileHistory, file.getAbsolutePath(), CapturePcapFileHistoryProperty.class);
        IndexView indexView = (IndexView) view;
        ViewHandle.InitializePcapFileList(settingProperty.capturePcapFileHistory, indexView.getFileListCtrl().getFileList());
        ViewHandle.InitializeCapturePcapFileMenu(settingProperty.capturePcapFileHistory, indexView.getCaptureMenuBarCtrl().getRecentMenu());
        StartCapture(file.getAbsolutePath());
    }
}
