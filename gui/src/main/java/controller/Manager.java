package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;
import util.TableHandle;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Manager {
    private CaptureProperty captureProperty;
    private SendProperty sendProperty;

    @FXML
    TableView<Property> captureConfigTable;

    @FXML
    Button captureAddButton;

    @FXML
    Button captureEditButton;

    @FXML
    Button captureDeleteButton;

    @FXML
    Button captureStartButton;

    @FXML
    CheckBox offline;

    @FXML
    TableView<Property> captureNIFTable;

    @FXML
    TextField pcapFile;

    @FXML
    TableView<Property> sendConfigTable;

    @FXML
    Button sendAddButton;

    @FXML
    Button sendEditButton;

    @FXML
    Button sendDeleteButton;

    @FXML
    Button sendStartButton;

    @FXML
    TableView<Property> sendNIFTable;


    public Manager() {}

    public void initialize() {
        try {
            TableHandle.InitializeTable(new NIFProperty(), captureNIFTable);
            TableHandle.InitializeTable(new NIFProperty(), sendNIFTable);
            TableHandle.InitializeTable(new CaptureProperty(), captureConfigTable);
            TableHandle.InitializeTable(new SendProperty(), sendConfigTable);
            TableHandle.UpdateConfigTable(SettingProperty.captureConfigFolder, new CaptureProperty(), captureConfigTable);
            TableHandle.UpdateConfigTable(SettingProperty.sendConfigFolder, new SendProperty(), sendConfigTable);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TableHandle.UpdateNIFTable(captureNIFTable);
        TableHandle.UpdateNIFTable(sendNIFTable);
        this.OfflineOnChecked();
    }


    public void ReceiveCaptureConfig(CaptureProperty captureProperty) {
        if (this.captureProperty != captureProperty)
            this.captureConfigTable.getItems().add(captureProperty);
    }

    public void ReceiveSendConfig(SendProperty sendProperty) {
         if (this.sendProperty != sendProperty)
            this.sendConfigTable.getItems().add(sendProperty);
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
        addCaptureConfig.setCaptureProperty(null);
        stage.show();
    }

    @FXML
    private void CaptureEditButtonOnClicked() throws IOException {
        if (captureConfigTable.getSelectionModel().getSelectedItem()==null)
            return;
        this.captureProperty = (CaptureProperty) captureConfigTable.getSelectionModel().getSelectedItem();
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
        addCaptureConfig.setCaptureProperty(this.captureConfigTable.getSelectionModel().getSelectedItem());
        addCaptureConfig.InitializeConfig();
        File file = new File(SettingProperty.captureConfigFolder + '/' + this.captureProperty.getName() + ".json");
        file.delete();
        stage.show();
    }

    @FXML
    private void CaptureDeleteButtonOnClicked() {
        if (this.captureConfigTable.getSelectionModel().getSelectedItem()!=null) {
            int index = this.captureConfigTable.getSelectionModel().getSelectedIndex();
            CaptureProperty captureProperty = (CaptureProperty) this.captureConfigTable.getItems().get(index);
            this.captureConfigTable.getItems().remove(index);
            File file = new File(SettingProperty.captureConfigFolder + '/' + captureProperty.getName() + ".json");
            if (file!=null)
                file.delete();
            this.captureConfigTable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void CaptureStartButtonOnClicked() throws IOException {
        if (!offline.isSelected())
            if (captureConfigTable.getSelectionModel().getSelectedItem()==null)
                return;
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
        if (offline.isSelected())
            startCapture.setPcapPath(pcapFile.getText());
        else {
            this.captureProperty = (CaptureProperty) this.captureConfigTable.getSelectionModel().getSelectedItem();
            startCapture.setCaptureProperty(this.captureProperty);
            startCapture.setNifName(((NIFProperty) captureNIFTable.getSelectionModel().getSelectedItem()).getName());
        }
        startCapture.setOffline(offline.isSelected());
        stage.show();
    }


    @FXML
    private void OfflineOnChecked() {
        captureNIFTable.setDisable(offline.isSelected());
        pcapFile.setDisable(!offline.isSelected());
        captureConfigTable.setDisable(offline.isSelected());
        if (offline.isSelected())
            this.captureProperty = null;
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
        addSendConfig.setSendProperty(null);
        stage.show();
    }

    @FXML
    private void SendEditButtonOnClicked() throws IOException {
        if (this.sendConfigTable.getSelectionModel().getSelectedItem()==null)
            return;
        this.sendProperty = (SendProperty) this.sendConfigTable.getSelectionModel().getSelectedItem();
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
        addSendConfig.setSendProperty(this.sendConfigTable.getSelectionModel().getSelectedItem());
        addSendConfig.InitializeConfig();
        File file = new File(SettingProperty.sendConfigFolder + '/' + this.sendProperty.getName() + ".json");
        file.delete();
        stage.show();
    }

    @FXML
    private void SendDeleteButtonOnClicked() {
        if (this.sendConfigTable.getSelectionModel().getSelectedItem()!=null) {
            int index = this.sendConfigTable.getSelectionModel().getSelectedIndex();
            SendProperty sendProperty = (SendProperty) this.sendConfigTable.getSelectionModel().getSelectedItem();
            File file = new File(SettingProperty.sendConfigFolder + '/' + sendProperty.getName() + ".json");
            if (file!=null)
                file.delete();
            this.sendConfigTable.getItems().remove(index);
        }
    }

    @FXML
    private void SendStartButtonOnClicked() {

    }
}
