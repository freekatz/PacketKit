package gui.ctrl.bar;

import gui.ctrl.AnalysisView;
import gui.ctrl.IndexView;
import gui.ctrl.View;
import gui.model.SettingProperty;
import gui.model.history.PcapFileHistoryProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.FileHandle;
import util.ViewHandle;

import java.io.File;
import java.io.IOException;

public class CaptureToolBar {
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
        Image startImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/x-capture-start.png"));
        startButton.setGraphic(new ImageView(startImage));
        Image stopImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/x-capture-stop.png"));
        stopButton.setGraphic(new ImageView(stopImage));
        Image restartImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/x-capture-restart.png"));
        restartButton.setGraphic(new ImageView(restartImage));
        Image returnImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/x-capture-start.png"));
        returnButton.setGraphic(new ImageView(returnImage));
        Image configImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/x-capture-options.png"));
        configButton.setGraphic(new ImageView(configImage));
        Image openImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/x-capture-start.png"));
        openButton.setGraphic(new ImageView(openImage));
        Image saveImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/x-capture-file-save.png"));
        saveButton.setGraphic(new ImageView(saveImage));
        Image closeImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/x-capture-file-close.png"));
        closeButton.setGraphic(new ImageView(closeImage));
        Image reloadImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/x-capture-file-reload.png"));
        reloadButton.setGraphic(new ImageView(reloadImage));
        Image forwardImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/x-capture-start.png"));
        forwardButton.setGraphic(new ImageView(forwardImage));
        Image analysisImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/x-capture-start.png"));
        analysisButton.setGraphic(new ImageView(analysisImage));

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
        analysisButton.setDisable(false);
    }

    @FXML
    private void StartButtonOnClicked() {
        startButton.setDisable(true);
        stopButton.setDisable(false);
        restartButton.setDisable(false);
        returnButton.setDisable(true);

        IndexView indexView = (IndexView) view;

        indexView.clearBrowser();
        indexView.StartCapture("online");
        // start capture
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
            ViewHandle.InitializeCenter(indexView);
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
        ViewHandle.InitializeCenter(indexView);

        for (int i = 0; i < 5; i++)
            indexView.getCaptureMenuBarCtrl().getFileMenu().getItems().get(i).setDisable(false);
    }

    @FXML
    private void ConfigButtonOnClicked() {
        try {
            FXMLLoader loader = ViewHandle.GetLoader("gui/view/CaptureConfigView.fxml");
            AnchorPane managerPane = loader.load();
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
        FileChooser fileChooser = new FileChooser();
        // setting
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PCAP files", "*.pcap"),
                new FileChooser.ExtensionFilter("PCAPNG files", "*.pcapng"),
                new FileChooser.ExtensionFilter("CAP files", "*.cap")
        );
        File file = fileChooser.showOpenDialog(stage);
        if (file==null) return;
        FileHandle.AddHistory(SettingProperty.pcapFileHistory, file.getAbsolutePath(), PcapFileHistoryProperty.class);
        String type = view.getType();
        IndexView indexView = (IndexView) view;
        ViewHandle.InitializePcapFileList(SettingProperty.pcapFileHistory, indexView.getFileListCtrl().getFileList());
        ViewHandle.InitializePcapFileMenu(SettingProperty.pcapFileHistory, indexView.getCaptureMenuBarCtrl().getRecentMenu());
        indexView.setPcapFile(file.getAbsolutePath());
        indexView.setNifName(null);
        for (int i = 2; i < indexView.getCaptureMenuBarCtrl().getRecentMenu().getItems().size(); i++) {
            RadioMenuItem item = (RadioMenuItem) indexView.getCaptureMenuBarCtrl().getRecentMenu().getItems().get(i);
            if (item.getText().contains(file.getAbsolutePath()))
                item.setSelected(true);
        }
        if (type.equals("index")) {
            indexView.setType("capture");
            ViewHandle.InitializeCenter(indexView);
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PCAP files", "*.pcap"),
                new FileChooser.ExtensionFilter("PCAPNG files", "*.pcapng"),
                new FileChooser.ExtensionFilter("CAP files", "*.cap")
        );
        IndexView indexView = (IndexView) view;
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
        ViewHandle.InitializeCenter(indexView);

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
        // only table has packet, enabled todo
        IndexView indexView = (IndexView) view;
    }

    @FXML
    private void AnalysisButtonOnClicked() {
        try {
            FXMLLoader loader = ViewHandle.GetLoader("gui/view/AnalysisView.fxml");
            AnchorPane managerPane = loader.load();
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
