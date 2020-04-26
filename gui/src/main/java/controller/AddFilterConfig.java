package controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.FilterProperty;
import model.Property;
import model.SettingProperty;
import org.pcap4j.core.PcapHandle;
import util.DirHandle;

import java.io.File;
import java.io.IOException;

public class AddFilterConfig {
    private StartCapture startCapture;
    private FilterProperty filterProperty;

    @FXML
    TextField name;

    @FXML
    TextField expression;

    @FXML
    TextArea comment;

    @FXML
    ComboBox<Label> direction;


    public AddFilterConfig() {}

    public void initialize() {
        DirHandle.InitializeDirection(this.direction);

    }

    public void InitializeConfig() {
        this.name.setText(filterProperty.getName());
        this.expression.setText(filterProperty.getExpression());
        this.comment.setText(filterProperty.getComment());
        this.direction.getItems().forEach( item -> {
            if (item.getText().equals(filterProperty.getDirection().toString()))
                this.direction.getSelectionModel().select(item);
        });

    }

    private void SaveConfig() throws IOException {
        this.filterProperty.setName(name.getText());
        this.filterProperty.setComment(comment.getText());
        this.filterProperty.setExpression(expression.getText());
        PcapHandle.PcapDirection pcapDirection;
        switch (direction.getSelectionModel().getSelectedItem().getId()) {
            case "inLabel":
                pcapDirection = PcapHandle.PcapDirection.IN;
                break;
            case "outLabel":
                pcapDirection = PcapHandle.PcapDirection.OUT;
                break;
            case "allLabel":
                pcapDirection = PcapHandle.PcapDirection.INOUT;
                break;
            default:
                pcapDirection = PcapHandle.PcapDirection.INOUT;
                break;
        }
        this.filterProperty.setDirection(pcapDirection);
        JsonMapper mapper = new JsonMapper();
        File file = new File(SettingProperty.filterConfigFolder + '/' + name.getText() + ".json");
        mapper.writeValue(file, this.filterProperty);

    }

    public void setStartCapture(StartCapture startCapture) {
        this.startCapture = startCapture;
    }

    public void setFilterProperty(Property property) {
        if (property==null)
            this.filterProperty = new FilterProperty();
        else
            this.filterProperty = (FilterProperty) property;
    }

    @FXML
    private void OkButtonOnClicked(Event event) throws IOException {
        this.SaveConfig();
        Stage stage = (Stage)((Button)(event).getSource()).getScene().getWindow();
        stage.close();
        this.startCapture.ReceiveFilterConfig(this.filterProperty);
    }

    @FXML
    private void CancelButtonOnClicked(Event event) {
        Stage stage = (Stage)((Button)(event).getSource()).getScene().getWindow();
        stage.close();
    }
}
