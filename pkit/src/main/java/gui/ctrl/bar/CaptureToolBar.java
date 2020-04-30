package gui.ctrl.bar;

import gui.ctrl.CaptureConfigView;
import gui.ctrl.CaptureView;
import gui.ctrl.IndexView;
import gui.ctrl.View;
import gui.model.SettingProperty;
import javafx.event.Event;
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

public class CaptureToolBar {
    View view;

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
    }

    public void setView(View view) {
        this.view = view;
        if (view.getClass().equals(IndexView.class)) {
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
            forwardButton.setDisable(true);
        } else {
            startButton.setDisable(true);
            stopButton.setDisable(false);
            restartButton.setDisable(false);
            configButton.setDisable(false);
            openButton.setDisable(false);
            saveButton.setDisable(false);
            if (view.getPcapFile()!=null) {
                closeButton.setDisable(false);
                reloadButton.setDisable(false);
            }

            searchButton.setDisable(false);
            searchNextButton.setDisable(true);
            searchPreviousButton.setDisable(true);
            jumpButton.setDisable(false);
            jumpNextButton.setDisable(false);
            jumpPreviousButton.setDisable(false);
            jumpFirstButton.setDisable(false);
            jumpLastButton.setDisable(false);
            forwardButton.setDisable(true);
        }

    }

    @FXML
    private void StartButtonOnClicked(Event event) {
        startButton.setDisable(true);
        stopButton.setDisable(false);
        restartButton.setDisable(false);
        saveButton.setDisable(true);


        if (view.getClass().equals(IndexView.class)) {

            try {
                FXMLLoader loader = ViewHandle.GetLoader("gui/view/CaptureView.fxml");
                AnchorPane capturePane = loader.load();
                Stage stage = new Stage();

                Scene scene = new Scene(capturePane);
                stage.setScene(scene);

                CaptureView captureView = loader.getController();
                IndexView indexView = (IndexView) view;
                captureView.setFilterExpression(indexView.filterProperty.getExpression());
                captureView.setCaptureProperty(indexView.captureProperty);
                captureView.setPcapFile(null);
                captureView.setNifName(indexView.getNifName());
                captureView.setIndexView(indexView);

                System.out.println(indexView.filterProperty.getExpression());
                System.out.println(indexView.captureProperty.getName());

                stage.show();

                captureView.CaptureControl();
            } catch (IOException e) {
                e.printStackTrace();
            }

            view.close(event);
        } else {
            CaptureView captureView = (CaptureView) view;
            captureView.CaptureControl();
        }
    }

    @FXML
    private void StopButtonOnClicked() {
        startButton.setDisable(false);
        stopButton.setDisable(true);
        restartButton.setDisable(false);
        saveButton.setDisable(false);
        CaptureView captureView = (CaptureView) view;
    }

    @FXML
    private void RestartButtonOnClicked() {
        startButton.setDisable(true);
        stopButton.setDisable(false);
        restartButton.setDisable(false);
        saveButton.setDisable(true);
        CaptureView captureView = (CaptureView) view;
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
        if (view.getClass().equals(IndexView.class)){
            try {
                FXMLLoader loader = ViewHandle.GetLoader("gui/view/CaptureView.fxml");
                AnchorPane capturePane = loader.load();
                Stage stage1 = new Stage();

                Scene scene = new Scene(capturePane);
                stage1.setScene(scene);

                CaptureView captureView = loader.getController();
                IndexView indexView = (IndexView) view;
                captureView.setFilterExpression(indexView.filterProperty.getExpression());
                captureView.setCaptureProperty(indexView.captureProperty);
                captureView.setPcapFile(file.getAbsolutePath());
                captureView.setNifName(null);

                captureView.setIndexView(indexView);

                System.out.println(indexView.filterProperty.getExpression());
                System.out.println(indexView.captureProperty.getName());
                System.out.println(file.getAbsolutePath());

                stage.show();

                captureView.CaptureControl();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            CaptureView captureView = (CaptureView) view;
            captureView.setPcapFile(file.getAbsolutePath());
            captureView.CaptureControl();
        }
    }

    @FXML
    private void SaveButtonOnClicked() {
        // new task and new stage to save
        CaptureView captureView = (CaptureView) view;
    }

    @FXML
    private void CloseButtonOnClicked() {
        CaptureView captureView = (CaptureView) view;
        captureView.clear();
    }

    @FXML
    private void ReloadButtonOnClicked() {
        CaptureView captureView = (CaptureView) view;
        captureView.CaptureControl();
    }

    @FXML
    private void SearchButtonOnClicked() {

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
        CaptureView captureView = (CaptureView) view;
        captureView.getPacketListCtrl().getPacketTable().getSelectionModel().select(
                captureView.getPacketListCtrl().getPacketTable().getSelectionModel().getSelectedIndex()+1
        );
    }

    @FXML
    private void JumpPreviousButtonOnClicked() {
        CaptureView captureView = (CaptureView) view;
        captureView.getPacketListCtrl().getPacketTable().getSelectionModel().select(
                captureView.getPacketListCtrl().getPacketTable().getSelectionModel().getSelectedIndex()-1
        );
    }

    @FXML
    private void JumpFirstButtonOnClicked() {
        CaptureView captureView = (CaptureView) view;
        captureView.getPacketListCtrl().getPacketTable().getSelectionModel().select(0);
    }

    @FXML
    private void JumpLastButtonOnClicked() {
        CaptureView captureView = (CaptureView) view;
        captureView.getPacketListCtrl().getPacketTable().getSelectionModel().select(
                captureView.getPacketListCtrl().getPacketTable().getItems().size()-1
        );
    }

    @FXML
    private void ForwardButtonOnClicked() {
        // only table has packet, enabled
    }

}
