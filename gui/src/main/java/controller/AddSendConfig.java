package controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import config.CaptureNetworkInterfaceConfig;
import config.Config;
import config.SendNetworkInterfaceConfig;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Setting;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import util.ConfigHandle;
import util.NIFHandle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class AddSendConfig {
    private Manager manager;
    private SendNetworkInterfaceConfig config;

    @FXML
    TextField name;

    @FXML
    TextArea comment;

    @FXML
    TextField count;

    @FXML
    TextField retry;

    @FXML
    TextField timeout;

    @FXML
    ComboBox<Label> dstNIF;

    @FXML
    Button okButton;

    @FXML
    Button cancelButton;

    public AddSendConfig() {}

    public void initialize() {
        NIFHandle.InitializeNIF(this.dstNIF);
    }

    private void SaveConfig() throws IOException {
        this.config = new SendNetworkInterfaceConfig();
        this.config.Initial();
        this.config.setName(name.getText());
        this.config.setComment(comment.getText());
        this.config.setCount(Integer.parseInt(count.getText()));
        this.config.setRetryCount(Integer.parseInt(retry.getText()));
        this.config.setTimeoutMillis(Integer.parseInt(timeout.getText()));
        this.config.setDstNif(this.dstNIF.getSelectionModel().getSelectedItem().getId());
        JsonMapper mapper = new JsonMapper();
        File file = new File(Setting.sendConfigFolder + '/' + name.getText() + ".json");
        mapper.writeValue(file, this.config);

    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @FXML
    private void cancelButtonOnClicked(Event event) {
        Stage stage = (Stage)((Button)(event).getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void okButtonOnClicked(Event event) throws IOException {
        this.SaveConfig();
        Stage stage = (Stage)((Button)(event).getSource()).getScene().getWindow();
        stage.close();
        this.manager.ReceiveSendConfig(this.config);
    }
}
