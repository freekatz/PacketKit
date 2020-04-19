package controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import config.CaptureFilterConfig;
import config.CaptureNetworkInterfaceConfig;
import config.Config;
import config.SendNetworkInterfaceConfig;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Setting;
import nif.NetworkInterfaceMode;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import util.TableHandle;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class AddCaptureConfig {
    private Manager manager;
    private CaptureNetworkInterfaceConfig networkInterfaceConfig;
    private CaptureFilterConfig filterConfig;

    @FXML
    TextField name;

    @FXML
    TextArea comment;

    @FXML
    TextField count;

    @FXML
    TextField length;

    @FXML
    TextField timeout;

    @FXML
    TextField buffer;

    @FXML
    CheckBox promiscuous;

    @FXML
    CheckBox rfmon;

    @FXML
    CheckBox precision;

    @FXML
    CheckBox immediate;

    @FXML
    TableView<Config> filterConfigTable;

    @FXML
    Button addButton;

    @FXML
    Button deleteButton;

    @FXML
    Button okButton;

    @FXML
    Button cancelButton;

    public AddCaptureConfig() {}

    public void initialize() throws IOException {
        TableHandle.UpdateTable(Setting.filterConfigFolder, new CaptureFilterConfig(), this.filterConfigTable);

    }

    private void SaveConfig() throws IOException {
        this.networkInterfaceConfig = new CaptureNetworkInterfaceConfig();
        this.networkInterfaceConfig.Initial();
        this.networkInterfaceConfig.setName(name.getText());
        this.networkInterfaceConfig.setComment(comment.getText());
        this.networkInterfaceConfig.setCount(Integer.parseInt(count.getText()));
        this.networkInterfaceConfig.setSnapshotLength(Integer.parseInt(length.getText()));
        this.networkInterfaceConfig.setTimeoutMillis(Integer.parseInt(timeout.getText()));
        this.networkInterfaceConfig.setBufferSize(Integer.parseInt(buffer.getText()));
        PcapNetworkInterface.PromiscuousMode promiscuousMode;
        if (promiscuous.isSelected())
            promiscuousMode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;
        else
            promiscuousMode = PcapNetworkInterface.PromiscuousMode.NONPROMISCUOUS;
        this.networkInterfaceConfig.setPromiscuousMode(promiscuousMode);
        NetworkInterfaceMode.RfmonMode rfmonMode;
        if (rfmon.isSelected())
            rfmonMode = NetworkInterfaceMode.RfmonMode.RfmonMode;
        else
            rfmonMode = NetworkInterfaceMode.RfmonMode.NoRfmonMode;
        this.networkInterfaceConfig.setRfmonMode(rfmonMode);
        PcapHandle.TimestampPrecision timestampPrecision;
        if (precision.isSelected())
            timestampPrecision = PcapHandle.TimestampPrecision.NANO;
        else
            timestampPrecision = PcapHandle.TimestampPrecision.MICRO;
        this.networkInterfaceConfig.setTimestampPrecision(timestampPrecision);
        NetworkInterfaceMode.ImmediateMode immediateMode;
        if (immediate.isSelected())
            immediateMode = NetworkInterfaceMode.ImmediateMode.ImmediateMode;
        else
            immediateMode = NetworkInterfaceMode.ImmediateMode.DelayMode;
        this.networkInterfaceConfig.setImmediateMode(immediateMode);

        if (this.filterConfig == null)
            this.filterConfig = (CaptureFilterConfig) this.filterConfigTable.getSelectionModel().getSelectedItem();
        System.out.println(this.filterConfig.toString());
        this.networkInterfaceConfig.setFilterConfig(this.filterConfig);

        JsonMapper mapper = new JsonMapper();
        File file = new File(Setting.captureConfigFolder + '/' + name.getText() + ".json");
        mapper.writeValue(file, this.networkInterfaceConfig);

    }

    public void ReceiveFilterConfig(CaptureFilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @FXML
    private void AddButtonOnClicked() throws IOException {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader loader = new FXMLLoader();
        URL url = loader.getClassLoader().getResource("view/AddFilterConfig.fxml");
        loader.setLocation(url);
        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        AddFilterConfig addFilterConfig = loader.getController();
        // TODO: 2020/4/19 filter manager where
        addFilterConfig.setCaptureConfig(this);
        stage.show();
    }

    @FXML
    private void DeleteButtonOnClicked() {
        if (this.filterConfigTable.getSelectionModel().getSelectedItem()!=null) {
            int index = this.filterConfigTable.getSelectionModel().getSelectedIndex();
            CaptureFilterConfig config = (CaptureFilterConfig) this.filterConfigTable.getSelectionModel().getSelectedItem();
            System.out.println(config.getName());
            File file = new File(Setting.filterConfigFolder + '/' + config.getName() + ".json");
            if (file!=null)
                file.delete();
            this.filterConfigTable.getItems().remove(index);
        }
    }

    @FXML
    private void CancelButtonOnClicked(Event event) {
        Stage stage = (Stage)((Button)(event).getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void OkButtonOnClicked(Event event) throws IOException {
        this.SaveConfig();
        Stage stage = (Stage)((Button)(event).getSource()).getScene().getWindow();
        stage.close();
        this.manager.ReceiveCaptureConfig(this.networkInterfaceConfig);
    }
}
