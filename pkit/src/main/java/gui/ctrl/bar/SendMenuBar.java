package gui.ctrl.bar;

import com.fasterxml.jackson.databind.json.JsonMapper;
import gui.ctrl.SendView;
import gui.ctrl.View;
import gui.model.SettingProperty;
import gui.model.history.CapturePcapFileHistoryProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.FileHandle;
import util.ViewHandle;

import java.io.File;
import java.io.IOException;

public class SendMenuBar {
    SettingProperty settingProperty = new SettingProperty();
    View view;

    @FXML
    public Menu fileMenu;


    @FXML
    public MenuItem importItem;

    @FXML
    public Menu recentMenu;

    @FXML
    public MenuItem clearHistoryItem;

    @FXML
    public MenuItem clearItem;

    @FXML
    public MenuItem exportItem;

    public SendMenuBar() {}

    public void initialize() {

        exportItem.setDisable(true);

        recentMenu.getItems().add(new SeparatorMenuItem());
        ViewHandle.InitializeSendPcapFileMenu(settingProperty.sendPcapFileHistory, recentMenu);

        for (int i = 2; i < recentMenu.getItems().size(); i++) {
            RadioMenuItem item = (RadioMenuItem) recentMenu.getItems().get(i);
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    SendView sendView = (SendView) view;
                    if (item.isSelected()) {
                        String pattern = "\\(.*?\\)";
                        sendView.setPcapFile(item.getText().replaceAll(pattern, ""));
                        sendView.clearBrowser();
                        sendView.StartImport();
                    }
                }
            });
        }
    }

    @FXML
    private void ImportItemOnClicked() {
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
        FileHandle.AddHistory(settingProperty.sendPcapFileHistory, file.getAbsolutePath(), CapturePcapFileHistoryProperty.class);
        SendView sendView = (SendView) view;
        sendView.setPcapFile(file.getAbsolutePath());

//        sendView.clearBrowser();
        sendView.StartImport();
    }

    @FXML
    private void ClearHistoryItemOnClicked() {
        JsonMapper mapper = new JsonMapper();
        CapturePcapFileHistoryProperty property = new CapturePcapFileHistoryProperty();
        try {
            mapper.writeValue(new File(settingProperty.sendPcapFileHistory), property);
        } catch (IOException e) {
            e.printStackTrace();
        }
        recentMenu.getItems().clear();
    }

    @FXML
    private void ClearItemOnClicked() {
        // 清空所有
        SendView sendView = (SendView) view;
        sendView.clearBrowser();
        sendView.setPcapFile(null);

        sendView.getSendToolBarCtrl().InitializeButtonStatus();
    }

    @FXML
    private void ExportItemOnClicked() {
        // new task and new stage to save
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FileChooser fileChooser = new FileChooser();
        // setting
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PCAP files", "*.pcap"),
                new FileChooser.ExtensionFilter("PCAPNG files", "*.pcapng"),
                new FileChooser.ExtensionFilter("CAP files", "*.cap")
        );
        SendView sendView = (SendView) view;
        File file = fileChooser.showSaveDialog(stage);
        if (file==null) return;
        sendView.setExportPath(file.getAbsolutePath());
        sendView.StartExport();
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
