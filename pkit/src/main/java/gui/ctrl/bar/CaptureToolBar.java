package gui.ctrl.bar;

import gui.ctrl.IndexView;
import gui.ctrl.SendView;
import gui.ctrl.View;
import gui.ctrl.single.AnalysisView;
import gui.model.SettingProperty;
import gui.model.browser.PacketProperty;
import gui.model.history.CapturePcapFileHistoryProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import job.BrowserJob;
import org.pcap4j.core.PcapNetworkInterface;
import util.FileHandle;
import util.ViewHandle;

import java.io.File;
import java.io.IOException;

public class CaptureToolBar {
    SettingProperty settingProperty = new SettingProperty();

    View view;

    @FXML
    ToolBar toolBar;

    @FXML
    Button startButton;

    @FXML
    Button stopButton;

    @FXML
    Button restartButton;

    @FXML
    Button returnButton;

    @FXML
    Button configButton;

    @FXML
    Button openButton;

    @FXML
    Button saveButton;

    @FXML
    Button closeButton;

    @FXML
    Button reloadButton;

    @FXML
    Button forwardButton;

    @FXML
    Button analysisButton;


    public CaptureToolBar() {}

    public void initialize() {
        this.InitializeButton();
    }

    private void InitializeButton() {
        Image startImage = new Image(getClass().getResourceAsStream(settingProperty.captureIconFolder + "/start.png"));
        startButton.setGraphic(new ImageView(startImage));
        startButton.setTooltip(new Tooltip("start capture"));
        Image stopImage = new Image(getClass().getResourceAsStream(settingProperty.captureIconFolder + "/stop.png"));
        stopButton.setGraphic(new ImageView(stopImage));
        stopButton.setTooltip(new Tooltip("stop capture"));
        Image restartImage = new Image(getClass().getResourceAsStream(settingProperty.captureIconFolder + "/restart.png"));
        restartButton.setGraphic(new ImageView(restartImage));
        restartButton.setTooltip(new Tooltip("restart the capture"));
        Image returnImage = new Image(getClass().getResourceAsStream(settingProperty.captureIconFolder + "/return.png"));
        returnButton.setGraphic(new ImageView(returnImage));
        returnButton.setTooltip(new Tooltip("return the index view"));
        Image configImage = new Image(getClass().getResourceAsStream(settingProperty.captureIconFolder + "/config.png"));
        configButton.setGraphic(new ImageView(configImage));
        configButton.setTooltip(new Tooltip("manage the capture config"));
        Image openImage = new Image(getClass().getResourceAsStream(settingProperty.captureIconFolder + "/open.png"));
        openButton.setGraphic(new ImageView(openImage));
        openButton.setTooltip(new Tooltip("open the pcap file"));
        Image saveImage = new Image(getClass().getResourceAsStream(settingProperty.captureIconFolder + "/save.png"));
        saveButton.setGraphic(new ImageView(saveImage));
        saveButton.setTooltip(new Tooltip("save current list"));
        Image closeImage = new Image(getClass().getResourceAsStream(settingProperty.captureIconFolder + "/close.png"));
        closeButton.setGraphic(new ImageView(closeImage));
        closeButton.setTooltip(new Tooltip("close the pcap file"));
        Image reloadImage = new Image(getClass().getResourceAsStream(settingProperty.captureIconFolder + "/reload.png"));
        reloadButton.setGraphic(new ImageView(reloadImage));
        reloadButton.setTooltip(new Tooltip("reload the pcap file"));
        Image forwardImage = new Image(getClass().getResourceAsStream(settingProperty.captureIconFolder + "/send.png"));
        forwardButton.setGraphic(new ImageView(forwardImage));
        forwardButton.setTooltip(new Tooltip("forward the current packet and open the send view"));
        Image analysisImage = new Image(getClass().getResourceAsStream(settingProperty.captureIconFolder + "/analysis.png"));
        analysisButton.setGraphic(new ImageView(analysisImage));
        analysisButton.setTooltip(new Tooltip("open the analysis view"));

        InitializeButtonStatus();
    }

    public void setView(View view) {
        this.view = view;
    }

    public void InitializeButtonStatus() {
        startButton.setDisable(false);
        stopButton.setDisable(true);
        restartButton.setDisable(true);
        returnButton.setDisable(true);
        configButton.setDisable(false);
        openButton.setDisable(false);
        saveButton.setDisable(true);
        closeButton.setDisable(true);
        reloadButton.setDisable(true);
        forwardButton.setDisable(false);
        analysisButton.setDisable(true);
    }

    @FXML
    private void StartButtonOnClicked() {
        startButton.setDisable(true);
        stopButton.setDisable(false);
        restartButton.setDisable(false);
        returnButton.setDisable(true);

        IndexView indexView = (IndexView) view;

        if (indexView.getType().equals("capture"))
            indexView.clearBrowser();
        else indexView.setType("capture");
        ViewHandle.InitializeCaptureCenter(indexView);
        indexView.StartCapture("online");
    }

    @FXML
    private void StopButtonOnClicked() {
        startButton.setDisable(false);
        stopButton.setDisable(true);
        restartButton.setDisable(false);
        returnButton.setDisable(false);
        saveButton.setDisable(false);

        IndexView indexView = (IndexView) view;
        indexView.StopCapture();
        if (indexView.getPacketListCtrl().getPacketTable().getItems().size()==0) {
            indexView.setType("index");
            ViewHandle.InitializeCaptureCenter(indexView);
            ReturnButtonOnClicked();
        }
    }

    @FXML
    private void RestartButtonOnClicked() {
        startButton.setDisable(true);
        stopButton.setDisable(false);
        restartButton.setDisable(false);
        returnButton.setDisable(true);

        IndexView indexView = (IndexView) view;
        indexView.clearBrowser();
        indexView.StartCapture("online");
    }


    @FXML
    private void ReturnButtonOnClicked() {
        InitializeButtonStatus();


        IndexView indexView = (IndexView) view;
        indexView.clearBrowser();
        indexView.setNifName(null);
        indexView.setType("index");
        ViewHandle.InitializeCaptureCenter(indexView);

        for (int i = 0; i < 5; i++)
            indexView.getCaptureMenuBarCtrl().getFileMenu().getItems().get(i).setDisable(false);
    }

    @FXML
    private void ConfigButtonOnClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane managerPane = loader.load(loader.getClassLoader().getResourceAsStream("view/config/CaptureConfigView.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(managerPane);
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        String type = view.getType();
        IndexView indexView = (IndexView) view;
        ViewHandle.InitializePcapFileList(settingProperty.capturePcapFileHistory, indexView.getFileListCtrl().getFileList());
        ViewHandle.InitializeCapturePcapFileMenu(settingProperty.capturePcapFileHistory, indexView.getCaptureMenuBarCtrl().getRecentMenu());
        indexView.setPcapFile(file.getAbsolutePath());
        indexView.setNifName(null);
        for (int i = 2; i < indexView.getCaptureMenuBarCtrl().getRecentMenu().getItems().size(); i++) {
            RadioMenuItem item = (RadioMenuItem) indexView.getCaptureMenuBarCtrl().getRecentMenu().getItems().get(i);
            if (item.getText().contains(file.getAbsolutePath()))
                item.setSelected(true);
        }
        if (type.equals("index")) {
            indexView.setType("capture");
            ViewHandle.InitializeCaptureCenter(indexView);
        }

        indexView.clearBrowser();
        indexView.StartCapture("offline");
    }

    @FXML
    private void SaveButtonOnClicked() {
        String type = view.getType();
        if (type.equals("index"))
            return;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PCAP files", "*.pcap"),
                new FileChooser.ExtensionFilter("PCAPNG files", "*.pcapng"),
                new FileChooser.ExtensionFilter("CAP files", "*.cap")
        );
        IndexView indexView = (IndexView) view;
        if (indexView.getPcapFile()!=null)
            fileChooser.setInitialFileName(indexView.getPcapFile().split("/")[indexView.getPcapFile().split("/").length-1]);
        File file = fileChooser.showSaveDialog(stage);
        if (file==null) return;
        indexView.setSavePath(file.getAbsolutePath());
        indexView.StartCapture("save");
    }

    @FXML
    private void CloseButtonOnClicked() {
        IndexView indexView = (IndexView) view;
        indexView.clearBrowser();
        indexView.setPcapFile(null);
        indexView.setType("index");
        ViewHandle.InitializeCaptureCenter(indexView);

        InitializeButtonStatus();

    }

    @FXML
    private void ReloadButtonOnClicked() {
        IndexView indexView = (IndexView) view;
        indexView.clearBrowser();
        indexView.StartCapture("offline");
    }


    @FXML
    private void ForwardButtonOnClicked() {
        IndexView indexView = (IndexView) view;

        String nifName;
        if (indexView.getNifName()==null)
            nifName = ((PcapNetworkInterface)ViewHandle.GetPcapNIFList().get(0)).getName();
        else nifName = indexView.getNifName();

        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane managerPane = loader.load(loader.getClassLoader().getResourceAsStream("view/SendView.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(managerPane);
            stage.setScene(scene);

            SendView sendView = loader.getController();
            if (view.getType().equals("capture")) {
                if (indexView.getPacketListCtrl().getPacketTable().getSelectionModel().getSelectedItem() != null) {
                    PacketProperty packetProperty = indexView.packetPropertyArrayList.get(indexView.getPacketListCtrl().getPacketTable().getSelectionModel().getSelectedIndex());
                    sendView.setPacketProperty(packetProperty);
                    sendView.packetPropertyArrayList.add(packetProperty);
                    sendView.getPacketListCtrl().getPacketTable().getItems().add(packetProperty.getInfo());
                    // 第一个包初始化
                    BrowserJob job = new BrowserJob(packetProperty, sendView);
                    Thread thread = new Thread(job);
                    thread.start();
                }
            }


            sendView.setNifName(nifName);

            sendView.setIndexView(indexView);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void AnalysisButtonOnClicked() {

        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane managerPane = loader.load(loader.getClassLoader().getResourceAsStream("view/single/AnalysisView.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(managerPane);
            stage.setScene(scene);

            AnalysisView analysisView = loader.getController();
            analysisView.setView(view);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ToolBar getToolBar() {
        return toolBar;
    }

}
