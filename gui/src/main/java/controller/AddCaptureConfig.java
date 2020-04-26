package controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.CaptureProperty;
import model.NIFMode;
import model.Property;
import model.SettingProperty;
import org.pcap4j.core.PcapNetworkInterface;

import java.io.File;
import java.io.IOException;

public class AddCaptureConfig {
    private Manager manager;
    private CaptureProperty captureProperty;

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
    CheckBox immediate;

    @FXML
    Button okButton;

    @FXML
    Button cancelButton;

    public AddCaptureConfig() {}

    public void initialize() {

    }

    public void InitializeConfig() {
        this.name.setText(captureProperty.getName());
        this.comment.setText(captureProperty.getComment());
        this.count.setText(String.valueOf(captureProperty.getCount()));
        this.length.setText(String.valueOf(captureProperty.getLength()));
        this.timeout.setText(String.valueOf(captureProperty.getTimeout()));
        this.buffer.setText(String.valueOf(captureProperty.getBuffer()));
        this.promiscuous.setSelected(captureProperty.getPromiscuous() == PcapNetworkInterface.PromiscuousMode.PROMISCUOUS);
        this.immediate.setSelected(captureProperty.getImmediate() == NIFMode.ImmediateMode.ImmediateMode);

    }

    private void SaveConfig() throws IOException {
        this.captureProperty.setName(name.getText());
        this.captureProperty.setComment(comment.getText());
        this.captureProperty.setCount(Integer.parseInt(count.getText()));
        this.captureProperty.setLength(Integer.parseInt(length.getText()));
        this.captureProperty.setTimeout(Integer.parseInt(timeout.getText()));
        this.captureProperty.setBuffer(Integer.parseInt(buffer.getText()));
        PcapNetworkInterface.PromiscuousMode promiscuousMode;
        if (promiscuous.isSelected())
            promiscuousMode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;
        else
            promiscuousMode = PcapNetworkInterface.PromiscuousMode.NONPROMISCUOUS;
        this.captureProperty.setPromiscuous(promiscuousMode);
        NIFMode.ImmediateMode immediateMode;
        if (immediate.isSelected())
            immediateMode = NIFMode.ImmediateMode.ImmediateMode;
        else
            immediateMode = NIFMode.ImmediateMode.DelayMode;
        this.captureProperty.setImmediate(immediateMode);


        JsonMapper mapper = new JsonMapper();
        File file = new File(SettingProperty.captureConfigFolder + '/' + name.getText() + ".json");
        mapper.writeValue(file, this.captureProperty);

    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public void setCaptureProperty(Property property) {
        if (property==null)
            this.captureProperty = new CaptureProperty();
        else
            this.captureProperty = (CaptureProperty) property;
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
        this.manager.ReceiveCaptureConfig(this.captureProperty);
    }
}
