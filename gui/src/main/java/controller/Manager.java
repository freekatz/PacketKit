package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import config.CaptureFilterConfig;
import config.CaptureNetworkInterfaceConfig;
import config.Config;
import config.SendNetworkInterfaceConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Setting;
import util.ConfigHandle;
import util.TableHandle;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Manager {
    private CaptureNetworkInterfaceConfig captureNetworkInterfaceConfig;
    private CaptureFilterConfig filterConfig;
    private SendNetworkInterfaceConfig sendNetworkInterfaceConfig;

    @FXML
    TableView<Config> captureConfigTable;

    @FXML
    Button captureAddButton;

    @FXML
    Button captureDeleteButton;

    @FXML
    Button captureStartButton;

    @FXML
    TableView<Config> sendConfigTable;

    @FXML
    Button sendAddButton;

    @FXML
    Button sendDeleteButton;

    @FXML
    Button sendStartButton;


    public Manager() {}

    public void initialize() throws IOException {
        TableHandle.UpdateTable(Setting.captureConfigFolder, new CaptureNetworkInterfaceConfig(), this.captureConfigTable);
        TableHandle.UpdateTable(Setting.sendConfigFolder, new SendNetworkInterfaceConfig(), this.sendConfigTable);
    }


    public void ReceiveCaptureConfig(CaptureNetworkInterfaceConfig captureNetworkInterfaceConfig) {
        this.captureNetworkInterfaceConfig = captureNetworkInterfaceConfig;
        if (!this.captureConfigTable.getItems().contains(captureNetworkInterfaceConfig))
            this.captureConfigTable.getItems().add(captureNetworkInterfaceConfig);
        this.captureConfigTable.refresh();
    }

    public void ReceiveSendConfig(SendNetworkInterfaceConfig sendNetworkInterfaceConfig) {
        this.sendNetworkInterfaceConfig = sendNetworkInterfaceConfig;
        if (!this.sendConfigTable.getItems().contains(sendNetworkInterfaceConfig))
            this.sendConfigTable.getItems().add(sendNetworkInterfaceConfig);
        this.sendConfigTable.refresh();
    }

    @FXML
    private void CaptureAddButtonOnClicked() throws IOException {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader();
        URL url = loader.getClassLoader().getResource("view/AddCaptureConfig.fxml");
        loader.setLocation(url);
        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        AddCaptureConfig addCaptureConfig = loader.getController();
        addCaptureConfig.setManager(this);
        stage.show();
    }

    @FXML
    private void CaptureDeleteButtonOnClicked() {
        if (this.captureConfigTable.getSelectionModel().getSelectedItem()!=null) {
            int index = this.captureConfigTable.getSelectionModel().getSelectedIndex();
            CaptureNetworkInterfaceConfig config = (CaptureNetworkInterfaceConfig) this.captureConfigTable.getItems().get(index);
            this.captureConfigTable.getItems().remove(index);
            File file = new File(Setting.captureConfigFolder + '/' + config.getName() + ".json");
            if (file!=null)
                file.delete();
            this.captureConfigTable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void CaptureStartButtonOnClicked() throws IOException {
        if (this.captureConfigTable.getSelectionModel().getSelectedItem()==null)
            return;
        CaptureNetworkInterfaceConfig config = (CaptureNetworkInterfaceConfig)this.captureConfigTable.getSelectionModel().getSelectedItem();

        Stage stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.WINDOW_MODAL);

        FXMLLoader loader = new FXMLLoader();
        URL url = loader.getClassLoader().getResource("view/StartCapture.fxml");
        loader.setLocation(url);
        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        StartCapture startCapture = loader.getController();
        startCapture.setManager(this);
        if (this.captureNetworkInterfaceConfig == null) {
            JsonMapper mapper = new JsonMapper();
            this.captureNetworkInterfaceConfig = mapper.readValue(new File(Setting.captureConfigFolder + '/' + config.getName() + ".json"), CaptureNetworkInterfaceConfig.class);
        }
        startCapture.setNetworkInterfaceConfig(this.captureNetworkInterfaceConfig);
        startCapture.InitializeConfig();
        stage.show();
    }

    @FXML
    private void SendAddButtonOnClicked() throws IOException {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader loader = new FXMLLoader();
        URL url = loader.getClassLoader().getResource("view/AddSendConfig.fxml");
        loader.setLocation(url);
        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        AddSendConfig addSendConfig = loader.getController();
        addSendConfig.setManager(this);
        stage.show();
    }

    @FXML
    private void SendDeleteButtonOnClicked() {
        if (this.sendConfigTable.getSelectionModel().getSelectedItem()!=null) {
            int index = this.sendConfigTable.getSelectionModel().getSelectedIndex();
            SendNetworkInterfaceConfig config = (SendNetworkInterfaceConfig) this.sendConfigTable.getSelectionModel().getSelectedItem();
            System.out.println(config.getName());
            File file = new File(Setting.sendConfigFolder + '/' + config.getName() + ".json");
            if (file!=null)
                file.delete();
            this.sendConfigTable.getItems().remove(index);
        }
    }

    @FXML
    private void SendStartButtonOnClicked() {

    }
}
