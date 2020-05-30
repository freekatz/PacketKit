package gui.ctrl.bar;

import gui.ctrl.SendView;
import gui.ctrl.View;
import gui.ctrl.config.SendConfigView;
import gui.model.JobMode;
import gui.model.SettingProperty;
import gui.model.history.CapturePcapFileHistoryProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import job.OfflineJob;
import util.FileHandle;
import util.ViewHandle;

import java.io.File;
import java.io.IOException;

public class SendToolBar {
    // save 时是保存 packet json

    SendView view;

    @FXML
    public ToolBar captureToolBar;

    @FXML
    public Button importButton;

    @FXML
    public Button addButton;

    @FXML
    public Button clearButton;

    @FXML
    public Button removeButton;

    @FXML
    public Button exportButton;

    @FXML
    public Button stopButton;

    @FXML
    public Button configButton;


    @FXML
    public Button sendButton;

    @FXML
    public Button sendAllButton;

    @FXML
    public Button forwardButton;

    @FXML
    public Button forwardAllButton;


    @FXML
    public ComboBox<String> nifBox;

    public SendToolBar() {}

    public void initialize() {

        ViewHandle.InitializeNifComboBox(nifBox);

        nifBox.setValue(nifBox.getItems().get(0));

        nifBox.setTooltip(new Tooltip("select the froward target nif"));

        Image importImage = new Image(getClass().getResourceAsStream(SettingProperty.sendIconFolder + "/import.png"));
        importButton.setGraphic(new ImageView(importImage));
        importButton.setTooltip(new Tooltip("import pcap file"));
        Image addImage = new Image(getClass().getResourceAsStream(SettingProperty.sendIconFolder + "/add.png"));
        addButton.setGraphic(new ImageView(addImage));
        addButton.setTooltip(new Tooltip("add packet template"));
        Image clearImage = new Image(getClass().getResourceAsStream(SettingProperty.sendIconFolder + "/clear.png"));
        clearButton.setGraphic(new ImageView(clearImage));
        clearButton.setTooltip(new Tooltip("warning: clear packet list"));
        Image removeImage = new Image(getClass().getResourceAsStream(SettingProperty.sendIconFolder + "/remove.png"));
        removeButton.setGraphic(new ImageView(removeImage));
        removeButton.setTooltip(new Tooltip("warning: remove current packet"));
        Image exportImage = new Image(getClass().getResourceAsStream(SettingProperty.sendIconFolder + "/export.png"));
        exportButton.setGraphic(new ImageView(exportImage));
        exportButton.setTooltip(new Tooltip("export current list"));
        Image stopImage = new Image(getClass().getResourceAsStream(SettingProperty.sendIconFolder + "/stop.png"));
        stopButton.setGraphic(new ImageView(stopImage));
        stopButton.setTooltip(new Tooltip("stop send or forward"));
        Image configImage = new Image(getClass().getResourceAsStream(SettingProperty.sendIconFolder + "/config.png"));
        configButton.setGraphic(new ImageView(configImage));
        configButton.setTooltip(new Tooltip("manage the send config"));
        Image sendImage = new Image(getClass().getResourceAsStream(SettingProperty.sendIconFolder + "/send.png"));
        sendButton.setGraphic(new ImageView(sendImage));
        sendButton.setTooltip(new Tooltip("send current packet"));
        Image sendAllImage = new Image(getClass().getResourceAsStream(SettingProperty.sendIconFolder + "/sendAll.png"));
        sendAllButton.setGraphic(new ImageView(sendAllImage));
        sendAllButton.setTooltip(new Tooltip("send all packet"));
        Image forwardImage = new Image(getClass().getResourceAsStream(SettingProperty.sendIconFolder + "/forward.png"));
        forwardButton.setGraphic(new ImageView(forwardImage));
        forwardButton.setTooltip(new Tooltip("forward current packet to target nif"));
        Image forwardAllImage = new Image(getClass().getResourceAsStream(SettingProperty.sendIconFolder + "/forwardAll.png"));
        forwardAllButton.setGraphic(new ImageView(forwardAllImage));
        forwardAllButton.setTooltip(new Tooltip("forward all packet to target nif"));

        InitializeButtonStatus();
    }

    public void InitializeButtonStatus() {
        importButton.setDisable(false);
        addButton.setDisable(false);
        clearButton.setDisable(false);
        removeButton.setDisable(true);
        exportButton.setDisable(true);
        stopButton.setDisable(true);
        configButton.setDisable(false);
        sendButton.setDisable(true);
        sendAllButton.setDisable(true);
        forwardButton.setDisable(true);
        forwardAllButton.setDisable(true);
        nifBox.setDisable(false);
    }


    @FXML
    private void ImportButtonOnClicked(ActionEvent actionEvent) {
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

        FileHandle.AddHistory(SettingProperty.sendPcapFileHistory, file.getAbsolutePath(), CapturePcapFileHistoryProperty.class);
        view.setPcapFile(file.getAbsolutePath());
        ViewHandle.InitializeSendPcapFileMenu(SettingProperty.sendPcapFileHistory, view.getSendMenuBarCtrl().recentMenu);
        view.JobScheduler(JobMode.ImportJob);
    }

    @FXML
    private void AddButtonOnClicked(ActionEvent actionEvent) {

        view.getSendToolBarCtrl().removeButton.setDisable(false);
        view.getSendToolBarCtrl().exportButton.setDisable(false);

        view.getSendToolBarCtrl().sendButton.setDisable(false);
        view.getSendToolBarCtrl().sendAllButton.setDisable(false);
        view.getSendToolBarCtrl().forwardButton.setDisable(false);
        view.getSendToolBarCtrl().forwardAllButton.setDisable(false);

        OfflineJob offlineJob = new OfflineJob(view, SettingProperty.packetTemplatePath);
        Thread thread = new Thread(offlineJob);
        thread.start();
    }

    @FXML
    private void ClearButtonOnClicked(ActionEvent actionEvent) {
        // 清空所有
        view.clearBrowser();
        view.setPcapFile(null);

    }

    @FXML
    private void RemoveButtonOnClicked(ActionEvent actionEvent) {
        // 删除选中的
        int index = view.getPacketListCtrl().getPacketTable().getSelectionModel().getSelectedIndex();

        view.getPacketListCtrl().getPacketTable().getItems().remove(index);
        view.packetPropertyArrayList.remove(index);
        view.getPacketHeaderCtrl().getRoot().getChildren().clear();

        if (view.getPacketListCtrl().getPacketTable().getItems().size()==0) {
            view.clearBrowser();
        }

    }

    // todo index he send 都加入导出为 json，就是在 offline 基础上去掉抓包的阶段
    @FXML
    private void ExportButtonOnClicked(ActionEvent actionEvent) {
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
        File file = fileChooser.showSaveDialog(stage);
        if (file==null) return;

        view.setExportPath(file.getAbsolutePath());
        view.JobScheduler(JobMode.ExportJob);

    }

    @FXML
    private void StopButtonOnClicked(ActionEvent actionEvent) {
        view.JobStop();
    }

    @FXML
    private void ConfigButtonOnClicked(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane managerPane = loader.load(loader.getClassLoader().getResourceAsStream(SettingProperty.sendConfigView));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(managerPane);
            stage.setScene(scene);

            SendConfigView sendConfigView = loader.getController();
            sendConfigView.setSendStatusBar(view.getSendStatusBarCtrl());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        view.getSendStatusBarCtrl().UpdateContextMenu();
    }

    @FXML
    private void SendButtonOnClicked(ActionEvent actionEvent) {
        view.JobScheduler(JobMode.SendOneJob);
    }

    @FXML
    private void SendAllButtonOnClicked(ActionEvent actionEvent) {
        view.JobScheduler(JobMode.SendAllJob);
    }

    @FXML
    private void ForwardButtonOnClicked(ActionEvent actionEvent) {
        view.JobScheduler(JobMode.ForwardAllJob);
    }

    @FXML
    private void ForwardAllButtonOnClicked(ActionEvent actionEvent) {
        view.JobScheduler(JobMode.ForwardAllJob);
    }

    @FXML
    private void NifBoxOnSelected() {
        if (view.packetPropertyArrayList.size()>0) {
            forwardButton.setDisable(false);
            forwardAllButton.setDisable(false);
        }
        view.setDstNifName(nifBox.getValue());
    }

    public SendView getView() {
        return view;
    }

    public void setView(SendView view) {
        this.view = view;
    }

}
