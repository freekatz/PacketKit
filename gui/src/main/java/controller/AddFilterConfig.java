package controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import config.CaptureFilterConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Setting;
import org.pcap4j.core.PcapHandle;
import util.DirHandle;

import java.io.File;
import java.io.IOException;

public class AddFilterConfig {
    private AddCaptureConfig addCaptureConfig;
    private CaptureFilterConfig config;

    @FXML
    TextField name;

    @FXML
    TextField filter;

    @FXML
    TextArea comment;

    @FXML
    ComboBox<Label> direction;


    public AddFilterConfig() {}

    public void initialize() {
        DirHandle.InitializeDirection(this.direction);

    }



    private void SaveConfig() throws IOException {
        this.config = new CaptureFilterConfig();
        this.config.Initial();
        this.config.setName(name.getText());
        this.config.setComment(comment.getText());
        this.config.setFilter(filter.getText());
        PcapHandle.PcapDirection pcapDirection;
        switch (direction.getSelectionModel().getSelectedItem().getText()) {
            case "入方向":
                pcapDirection = PcapHandle.PcapDirection.IN;
                break;
            case "出方向":
                pcapDirection = PcapHandle.PcapDirection.OUT;
                break;
            case "出入方向":
                pcapDirection = PcapHandle.PcapDirection.INOUT;
                break;
            default:
                pcapDirection = PcapHandle.PcapDirection.INOUT;
                break;
        }
        this.config.setDirection(pcapDirection);
        JsonMapper mapper = new JsonMapper();
        File file = new File(Setting.filterConfigFolder + '/' + name.getText() + ".json");
        mapper.writeValue(file, this.config);

    }

    public void setCaptureConfig(AddCaptureConfig captureConfig) {
        this.addCaptureConfig = captureConfig;
    }

    @FXML
    private void OkButtonOnClicked(Event event) throws IOException {
        this.SaveConfig();
        Stage stage = (Stage)((Button)(event).getSource()).getScene().getWindow();
        stage.close();
        this.addCaptureConfig.ReceiveFilterConfig(this.config);
    }

    @FXML
    private void CancelButtonOnClicked(Event event) {
        Stage stage = (Stage)((Button)(event).getSource()).getScene().getWindow();
        stage.close();
    }
}
