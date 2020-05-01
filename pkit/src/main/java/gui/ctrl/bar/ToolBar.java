package gui.ctrl.bar;

import gui.ctrl.AnalysisView;
import gui.ctrl.IndexView;
import gui.ctrl.View;
import gui.model.SettingProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class ToolBar {
    View view;

    @FXML
    javafx.scene.control.ToolBar toolBar;

    @FXML
    Button startButton;

    @FXML
    Button stopButton;

    @FXML
    Button restartButton;

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
    Button searchButton;

    @FXML
    Button searchNextButton;

    @FXML
    Button searchPreviousButton;

    @FXML
    Button jumpButton;

    @FXML
    Button jumpNextButton;

    @FXML
    Button jumpPreviousButton;

    @FXML
    Button jumpFirstButton;

    @FXML
    Button jumpLastButton;

    @FXML
    Button forwardButton;

    @FXML
    Button analysisButton;


    public ToolBar() {}

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
        Image searchImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/edit-find.template.png"));
        searchButton.setGraphic(new ImageView(searchImage));
        Image searchNextImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/x-capture-start.png"));
        searchNextButton.setGraphic(new ImageView(searchNextImage));
        Image searchPreviousImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/x-capture-start.png"));
        searchPreviousButton.setGraphic(new ImageView(searchPreviousImage));
        Image jumpImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/go-jump.png"));
        jumpButton.setGraphic(new ImageView(jumpImage));
        Image jumpNextImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/go-next.png"));
        jumpNextButton.setGraphic(new ImageView(jumpNextImage));
        Image jumpPreviousImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/go-previous.png"));
        jumpPreviousButton.setGraphic(new ImageView(jumpPreviousImage));
        Image jumpFirstImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/go-first.png"));
        jumpFirstButton.setGraphic(new ImageView(jumpFirstImage));
        Image jumpLastImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/go-last.png"));
        jumpLastButton.setGraphic(new ImageView(jumpLastImage));
        Image forwardImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder1 + "/x-capture-start.png"));
        forwardButton.setGraphic(new ImageView(forwardImage));

        startButton.setDisable(false);
        stopButton.setDisable(true);
        restartButton.setDisable(true);
        configButton.setDisable(false);
        openButton.setDisable(false);
        saveButton.setDisable(true);
        closeButton.setDisable(true);
        reloadButton.setDisable(true);
        searchButton.setDisable(true);
        searchNextButton.setDisable(true);
        searchPreviousButton.setDisable(true);
        jumpButton.setDisable(true);
        jumpNextButton.setDisable(true);
        jumpPreviousButton.setDisable(true);
        jumpFirstButton.setDisable(true);
        jumpLastButton.setDisable(true);
        forwardButton.setDisable(false);
    }

    public void setView(View view) {
        this.view = view;
    }

    @FXML
    private void StartButtonOnClicked() {
        startButton.setDisable(true);
        stopButton.setDisable(false);
        restartButton.setDisable(false);

        String type = view.getType();
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
        saveButton.setDisable(false);

        IndexView indexView = (IndexView) view;
        indexView.StopCapture();
        if (indexView.getPacketListCtrl().getPacketTable().getItems().size()==0) {
            indexView.setType("index");
            ViewHandle.InitializeCenter(indexView);
        }
    }

    @FXML
    private void RestartButtonOnClicked() {
        startButton.setDisable(true);
        stopButton.setDisable(false);
        restartButton.setDisable(false);

        IndexView indexView = (IndexView) view;
        indexView.clearBrowser();
        indexView.StartCapture("online");
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
        FileHandle.AddLine(SettingProperty.pcapFileHistory, file.getAbsolutePath());
        String type = view.getType();
        IndexView indexView = (IndexView) view;
        indexView.setPcapFile(file.getAbsolutePath());
        indexView.setNifName(null);
        if (type.equals("index")) {
            indexView.setType("capture");
            ViewHandle.InitializeCenter(indexView);
        }

        indexView.clearBrowser();
        indexView.StartCapture("offline");
        // start capture
    }

    @FXML
    private void SaveButtonOnClicked() {
        // new task and new stage to save
    }

    @FXML
    private void CloseButtonOnClicked() {
        IndexView indexView = (IndexView) view;
        indexView.clearBrowser();

    }

    @FXML
    private void ReloadButtonOnClicked() {
        IndexView indexView = (IndexView) view;
        indexView.clearBrowser();
        indexView.StartCapture("offline");
    }

    @FXML
    private void SearchButtonOnClicked() {
        // search button logic
    }

    @FXML
    private void SearchNextButtonOnClicked() {

    }
    @FXML
    private void SearchPreviousButtonOnClicked() {

    }

    @FXML
    private void JumpButtonOnClicked() {
        // new stage
        // table auto roll
    }

    @FXML
    private void JumpNextButtonOnClicked() {
        IndexView indexView = (IndexView) view;
        indexView.getPacketListCtrl().getPacketTable().getSelectionModel().select(
                indexView.getPacketListCtrl().getPacketTable().getSelectionModel().getSelectedIndex()+1
        );
    }

    @FXML
    private void JumpPreviousButtonOnClicked() {
        IndexView indexView = (IndexView) view;
        indexView.getPacketListCtrl().getPacketTable().getSelectionModel().select(
                indexView.getPacketListCtrl().getPacketTable().getSelectionModel().getSelectedIndex()-1
        );
    }

    @FXML
    private void JumpFirstButtonOnClicked() {
        IndexView indexView = (IndexView) view;
        indexView.getPacketListCtrl().getPacketTable().getSelectionModel().select(0);
    }

    @FXML
    private void JumpLastButtonOnClicked() {
        IndexView indexView = (IndexView) view;
        indexView.getPacketListCtrl().getPacketTable().getSelectionModel().select(
                indexView.getPacketListCtrl().getPacketTable().getItems().size()-1
        );
    }

    @FXML
    private void ForwardButtonOnClicked() {
        // only table has packet, enabled
        IndexView indexView = (IndexView) view;
        if (indexView.getPacketListCtrl()!=null)
            System.out.println("forward");
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

    public javafx.scene.control.ToolBar getToolBar() {
        return toolBar;
    }

}
