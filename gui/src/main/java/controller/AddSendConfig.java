package controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.NIFProperty;
import model.Property;
import model.SendProperty;
import model.SettingProperty;
import util.TableHandle;

import java.io.File;
import java.io.IOException;

public class AddSendConfig {
    private Manager manager;
    private SendProperty sendProperty;

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
    TableView<Property> target;

    @FXML
    Button okButton;

    @FXML
    Button cancelButton;

    public AddSendConfig() {}

    public void initialize() {
        try {
            TableHandle.InitializeTable(new NIFProperty(), target);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TableHandle.UpdateNIFTable(this.target);
    }

    public void InitializeConfig() {
        this.name.setText(this.sendProperty.getName());
        this.comment.setText(this.sendProperty.getComment());
        this.count.setText(String.valueOf(this.sendProperty.getCount()));
        this.retry.setText(String.valueOf(this.sendProperty.getRetry()));
        this.timeout.setText(String.valueOf(this.sendProperty.getTimeout()));
        this.target.getItems().forEach( item -> {
            if (((NIFProperty)item).getName().equals(sendProperty.getTarget()))
                this.target.getSelectionModel().select(item);
        });
    }

    private void SaveConfig() throws IOException {
        this.sendProperty.setName(name.getText());
        this.sendProperty.setComment(comment.getText());
        this.sendProperty.setCount(Integer.parseInt(count.getText()));
        this.sendProperty.setRetry(Integer.parseInt(retry.getText()));
        this.sendProperty.setTimeout(Integer.parseInt(timeout.getText()));
        this.sendProperty.setTarget(((NIFProperty) this.target.getSelectionModel().getSelectedItem()).getName());

        JsonMapper mapper = new JsonMapper();
        File file = new File(SettingProperty.sendConfigFolder + '/' + name.getText() + ".json");
        mapper.writeValue(file, this.sendProperty);

    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public void setSendProperty(Property property) {
        if (property==null)
            this.sendProperty = new SendProperty();
        else
            this.sendProperty = (SendProperty) property;
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
        this.manager.ReceiveSendConfig(this.sendProperty);
    }
}
