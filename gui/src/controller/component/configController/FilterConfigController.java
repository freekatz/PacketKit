package controller.component.configController;

import config.CaptureFilterConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.pcap4j.core.PcapHandle;

public class FilterConfigController implements ConfigController {

    private CaptureFilterConfig config;

    @FXML
    TextField nameTextField;

    @FXML
    TextField filterTextField;

    @FXML
    ComboBox<Label> directionComboBox;

    @FXML
    TextArea commentTextArea;

    public FilterConfigController() {

    }

    public void initialize() {
        this.config = new CaptureFilterConfig();
        this.InitializeDirection();
    }

    @FXML
    private void FilterAutoComplete() {
        // TODO: 2020/4/16 在此处实现自动补全
        System.out.println(filterTextField.getText());
    }

    private void InitializeDirection() {
        ObservableList<Label> ob = FXCollections.observableArrayList();
        Label inLabel = new Label("入方向");
        inLabel.setId("inLabel");
        Label outLabel = new Label("出方向");
        outLabel.setId("outLabel");
        Label allLabel = new Label("所有方向");
        allLabel.setId("allLabel");
        ob.addAll(inLabel, outLabel, allLabel);
        directionComboBox.setItems(ob);
        directionComboBox.setValue(allLabel);

    }

    public CaptureFilterConfig getConfig() {
        return this.config;
    }

    @Override
    public void dump() {
        this.config.Initial();
        this.config.setName(nameTextField.getText());
        this.config.setFilter(filterTextField.getText());
        String directionId = directionComboBox.getSelectionModel().getSelectedItem().getId();
        switch (directionId) {
            case "inLabel":
                this.config.setDirection(PcapHandle.PcapDirection.IN);
                break;
            case "outLabel":
                this.config.setDirection(PcapHandle.PcapDirection.OUT);
                break;
            case "allLabel":
                this.config.setDirection(PcapHandle.PcapDirection.INOUT);
                break;
            default:
                this.config.setDirection(PcapHandle.PcapDirection.INOUT);
                break;
        }
        this.config.setComment(commentTextArea.getText());
    }
}
