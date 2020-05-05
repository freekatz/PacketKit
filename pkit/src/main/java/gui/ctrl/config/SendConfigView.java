package gui.ctrl.config;

import gui.ctrl.SendView;
import gui.ctrl.View;
import gui.ctrl.bar.SendStatusBar;
import gui.model.config.CaptureProperty;
import gui.model.Property;
import gui.model.SettingProperty;
import gui.model.config.SendProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import util.FileHandle;
import util.ViewHandle;

import java.util.List;

public class SendConfigView implements View {

    SendView sendView;

    SettingProperty settingProperty = new SettingProperty();

    String type;

    SendStatusBar sendStatusBar;

    @FXML
    TableView<Property> configTable;

    @FXML
    Button addButton;

    @FXML
    Button deleteButton;

    @FXML
    Button copyButton;

    @FXML
    Button okButton;

    @FXML
    Button cancelButton;

    @FXML
    Button helpButton;


    public SendConfigView() {}

    public void initialize() {
        ViewHandle.InitializeTable(new SendProperty(), configTable);

        ViewHandle.UpdateConfigTable(settingProperty.sendConfig, new SendProperty(), configTable);
        this.InitializeColumn();
    }

    private void InitializeColumn() {
        configTable.getColumns().forEach(column -> {
            TableColumn<SendProperty, String> column1 = (TableColumn) column;
            column1.setCellValueFactory(new PropertyValueFactory<>(column.getId()));
            column1.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter() {
                @Override
                public String toString(Object o) {
                    return String.valueOf(o);
                }

                @Override
                public Object fromString(String s) {
                    return s;
                }

            }));
            switch (column1.getId()) {
                case "name":
                    column1.setOnEditCommit((TableColumn.CellEditEvent<SendProperty, String> t) -> {
                        SendProperty property = (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        );
                        FileHandle.DeleteConfig(settingProperty.sendConfig, property);
                        if (t.getNewValue()!=null)
                            property.setName(t.getNewValue());
                        else property.setName("default");
                        FileHandle.SaveConfig(settingProperty.sendConfig, property);
                    });
                    break;
                case "comment":
                    column1.setOnEditCommit((TableColumn.CellEditEvent<SendProperty, String> t) -> {
                        SendProperty property = (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        );
                        if (t.getNewValue()!=null)
                            property.setComment(t.getNewValue());
                        else property.setComment("");
                        FileHandle.SaveConfig(settingProperty.sendConfig, property);
                    });
                    break;
                case "count":
                    column1.setOnEditCommit((TableColumn.CellEditEvent<SendProperty, String> t) -> {
                        SendProperty property = (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        );
                        if (t.getNewValue()!=null)
                            property.setCount(Integer.parseInt(t.getNewValue()));
                        else property.setCount(-1);
                        FileHandle.SaveConfig(settingProperty.sendConfig, property);
                    });
                    break;
                case "retry":
                    column1.setOnEditCommit((TableColumn.CellEditEvent<SendProperty, String> t) -> {
                        SendProperty property = (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        );
                        if (t.getNewValue()!=null)
                            property.setRetry(Integer.parseInt(t.getNewValue()));
                        else property.setRetry(1);
                        FileHandle.SaveConfig(settingProperty.sendConfig, property);
                    });
                    break;
                case "timeout":
                    column1.setOnEditCommit((TableColumn.CellEditEvent<SendProperty, String> t) -> {
                        SendProperty property = (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        );
                        if (t.getNewValue()!=null)
                            property.setTimeout(Integer.parseInt(t.getNewValue()));
                        else property.setTimeout(10000);
                        FileHandle.SaveConfig(settingProperty.sendConfig, property);
                    });
                    break;
            }

        });

    }

    public void setSendStatusBar(SendStatusBar sendStatusBar) {
        this.sendStatusBar = sendStatusBar;
    }

    public void setSendView(SendView sendView) {
        this.sendView = sendView;
    }


    @FXML
    private void AddButtonOnClicked() {
        SendProperty property = new SendProperty();
        property.setName("new config");
        property.setComment("new config");
        property.setCount(1);
        property.setRetry(1);
        property.setTimeout(1000);

        configTable.getItems().add(property);
        FileHandle.SaveConfig(settingProperty.sendConfig, property);
    }

    @FXML
    private void DeleteButtonOnClicked() {
        SendProperty property = (SendProperty) configTable.getSelectionModel().getSelectedItem();
        FileHandle.DeleteConfig(settingProperty.sendConfig, property);
        configTable.getItems().remove(configTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void CopyButtonOnClicked() {
        SendProperty property = (SendProperty) configTable.getSelectionModel().getSelectedItem();
        if (property != null) {
            SendProperty newProperty = (SendProperty) property.clone();
            newProperty.setName(property.getName()+"(copy)");
            configTable.getItems().add(newProperty);
            FileHandle.SaveConfig(settingProperty.sendConfig, newProperty);
        }
    }

    @FXML
    private void OkButtonOnClicked(Event event) {
        sendStatusBar.UpdateContextMenu();
        Stage stage = (Stage)((Button)(event).getSource()).getScene().getWindow();
        stage.close();
        // apply 过滤器
    }

    @FXML
    private void CancelButtonOnClicked(Event event) {
        Stage stage = (Stage)((Button)(event).getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void HelpButtonOnClicked() {
    }


    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void close(Event event) {

    }
}
