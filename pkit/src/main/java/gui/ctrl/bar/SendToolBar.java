package gui.ctrl.bar;

import gui.ctrl.SendView;
import gui.ctrl.View;
import gui.ctrl.config.SendConfigView;
import gui.model.SettingProperty;
import gui.model.history.CapturePcapFileHistoryProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import util.FileHandle;
import util.PacketHandle;
import util.ViewHandle;
import util.job.OfflineJob;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class SendToolBar {
    SettingProperty settingProperty = new SettingProperty();

    // save 时是保存 packet json

    View view;


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

        Image importImage = new Image(getClass().getResourceAsStream(settingProperty.sendIconFolder + "/import.png"));
        importButton.setGraphic(new ImageView(importImage));
        importButton.setTooltip(new Tooltip("import pcap file"));
        Image addImage = new Image(getClass().getResourceAsStream(settingProperty.sendIconFolder + "/add.png"));
        addButton.setGraphic(new ImageView(addImage));
        addButton.setTooltip(new Tooltip("add packet template"));
        Image clearImage = new Image(getClass().getResourceAsStream(settingProperty.sendIconFolder + "/clear.png"));
        clearButton.setGraphic(new ImageView(clearImage));
        clearButton.setTooltip(new Tooltip("warning: clear packet list"));
        Image removeImage = new Image(getClass().getResourceAsStream(settingProperty.sendIconFolder + "/remove.png"));
        removeButton.setGraphic(new ImageView(removeImage));
        removeButton.setTooltip(new Tooltip("warning: remove current packet"));
        Image exportImage = new Image(getClass().getResourceAsStream(settingProperty.sendIconFolder + "/export.png"));
        exportButton.setGraphic(new ImageView(exportImage));
        exportButton.setTooltip(new Tooltip("export current list"));
        Image stopImage = new Image(getClass().getResourceAsStream(settingProperty.sendIconFolder + "/stop.png"));
        stopButton.setGraphic(new ImageView(stopImage));
        stopButton.setTooltip(new Tooltip("stop send or forward"));
        Image configImage = new Image(getClass().getResourceAsStream(settingProperty.sendIconFolder + "/config.png"));
        configButton.setGraphic(new ImageView(configImage));
        configButton.setTooltip(new Tooltip("manage the send config"));
        Image sendImage = new Image(getClass().getResourceAsStream(settingProperty.sendIconFolder + "/send.png"));
        sendButton.setGraphic(new ImageView(sendImage));
        sendButton.setTooltip(new Tooltip("send current packet"));
        Image sendAllImage = new Image(getClass().getResourceAsStream(settingProperty.sendIconFolder + "/sendAll.png"));
        sendAllButton.setGraphic(new ImageView(sendAllImage));
        sendAllButton.setTooltip(new Tooltip("send all packet"));
        Image forwardImage = new Image(getClass().getResourceAsStream(settingProperty.sendIconFolder + "/forward.png"));
        forwardButton.setGraphic(new ImageView(forwardImage));
        forwardButton.setTooltip(new Tooltip("forward current packet to target nif"));
        Image forwardAllImage = new Image(getClass().getResourceAsStream(settingProperty.sendIconFolder + "/forwardAll.png"));
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

        FileHandle.AddHistory(settingProperty.sendPcapFileHistory, file.getAbsolutePath(), CapturePcapFileHistoryProperty.class);
        SendView sendView = (SendView) view;
        sendView.setPcapFile(file.getAbsolutePath());
        ViewHandle.InitializeSendPcapFileMenu(settingProperty.sendPcapFileHistory, sendView.getSendMenuBarCtrl().recentMenu);
        sendView.StartImport();
    }

    @FXML
    private void AddButtonOnClicked(ActionEvent actionEvent) {
        // todo 添加默认数据包到包浏览器中，再自行修改

        SendView sendView = (SendView) view;

        sendView.getSendToolBarCtrl().removeButton.setDisable(false);
        sendView.getSendToolBarCtrl().exportButton.setDisable(false);

        sendView.getSendToolBarCtrl().sendButton.setDisable(false);
        sendView.getSendToolBarCtrl().sendAllButton.setDisable(false);
        sendView.getSendToolBarCtrl().forwardButton.setDisable(false);
        sendView.getSendToolBarCtrl().forwardAllButton.setDisable(false);

        sendView.status.setText("read");

        OfflineJob offlineJob = new OfflineJob(sendView, settingProperty.packetTemplatePath);
        Thread thread = new Thread(offlineJob);
        thread.start();
    }

    @FXML
    private void ClearButtonOnClicked(ActionEvent actionEvent) {
        // 清空所有
        SendView sendView = (SendView) view;
        sendView.clearBrowser();
        sendView.setPcapFile(null);

    }

    @FXML
    private void RemoveButtonOnClicked(ActionEvent actionEvent) {
        // 删除选中的
        SendView sendView = (SendView) view;

        int index = sendView.getPacketListCtrl().getPacketTable().getSelectionModel().getSelectedIndex();

        sendView.getPacketListCtrl().getPacketTable().getItems().remove(index);
        sendView.packetPropertyArrayList.remove(index);
        sendView.getPacketHeaderCtrl().getRoot().getChildren().clear();

        if (sendView.getPacketListCtrl().getPacketTable().getItems().size()==0)
            removeButton.setDisable(true);

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
        SendView sendView = (SendView) view;

        sendView.setExportPath(file.getAbsolutePath());
        sendView.StartExport();

    }

    @FXML
    private void StopButtonOnClicked(ActionEvent actionEvent) {
        SendView sendView = (SendView) view;
        sendView.Stop();
    }

    @FXML
    private void ConfigButtonOnClicked(ActionEvent actionEvent) {
        SendView sendView = (SendView) view;
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane managerPane = loader.load(loader.getClassLoader().getResourceAsStream("view/config/SendConfigView.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(managerPane);
            stage.setScene(scene);

            SendConfigView sendConfigView = loader.getController();
            sendConfigView.setSendStatusBar(sendView.getSendStatusBarCtrl());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendView.getSendStatusBarCtrl().UpdateContextMenu();
    }

    @FXML
    private void SendButtonOnClicked(ActionEvent actionEvent) {
        SendView sendView = (SendView) view;

        sendView.StartSend("one");
    }

    @FXML
    private void SendAllButtonOnClicked(ActionEvent actionEvent) {
        SendView sendView = (SendView) view;

        sendView.StartSend("all");
    }

    @FXML
    private void ForwardButtonOnClicked(ActionEvent actionEvent) {

        SendView sendView = (SendView) view;

        sendView.StartForward("one");
    }

    @FXML
    private void ForwardAllButtonOnClicked(ActionEvent actionEvent) {

        SendView sendView = (SendView) view;

        sendView.StartForward("all");
    }

    @FXML
    private void NifBoxOnSelected() {
        SendView sendView = (SendView) view;
        if (sendView.packetPropertyArrayList.size()>0) {
            forwardButton.setDisable(false);
            forwardAllButton.setDisable(false);
        }
        System.out.println(nifBox.getValue());
        sendView.setDstNifName(nifBox.getValue());
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

}
