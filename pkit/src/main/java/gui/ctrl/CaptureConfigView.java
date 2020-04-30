package gui.ctrl;

import gui.ctrl.bar.CaptureToolBar;
import gui.ctrl.bar.IndexStatusBar;
import gui.model.CaptureProperty;
import gui.model.Property;
import gui.model.SettingProperty;
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

public class CaptureConfigView {

    IndexStatusBar indexStatusBar;

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


    public CaptureConfigView() {}

    public void initialize() {
        ViewHandle.InitializeTable(new CaptureProperty(), configTable);
        ViewHandle.UpdateConfigTable(SettingProperty.captureConfig, new CaptureProperty(), configTable);
        this.InitializeColumn();
    }

    private void InitializeColumn() {
        configTable.getColumns().forEach(column -> {
            TableColumn<CaptureProperty, String> column1 = (TableColumn) column;
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
                    column1.setOnEditCommit((TableColumn.CellEditEvent<CaptureProperty, String> t) -> {
                        CaptureProperty property = (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        );
                        FileHandle.DeleteConfig(SettingProperty.captureConfig, property);
                        if (t.getNewValue()!=null)
                            property.setName(t.getNewValue());
                        else property.setName("default");
                        FileHandle.SaveConfig(SettingProperty.captureConfig, property);
                    });
                    break;
                case "comment":
                    column1.setOnEditCommit((TableColumn.CellEditEvent<CaptureProperty, String> t) -> {
                        CaptureProperty property = (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        );
                        if (t.getNewValue()!=null)
                            property.setComment(t.getNewValue());
                        else property.setComment("");
                        FileHandle.SaveConfig(SettingProperty.captureConfig, property);
                    });
                    break;
                case "count":
                    column1.setOnEditCommit((TableColumn.CellEditEvent<CaptureProperty, String> t) -> {
                        CaptureProperty property = (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        );
                        if (t.getNewValue()!=null)
                            property.setCount(Integer.parseInt(t.getNewValue()));
                        else property.setCount(-1);
                        FileHandle.SaveConfig(SettingProperty.captureConfig, property);
                    });
                    break;
                case "length":
                    column1.setOnEditCommit((TableColumn.CellEditEvent<CaptureProperty, String> t) -> {
                        CaptureProperty property = (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        );
                        if (t.getNewValue()!=null)
                            property.setLength(Integer.parseInt(t.getNewValue()));
                        else property.setLength(262144);
                        FileHandle.SaveConfig(SettingProperty.captureConfig, property);
                    });
                    break;
                case "timeout":
                    column1.setOnEditCommit((TableColumn.CellEditEvent<CaptureProperty, String> t) -> {
                        CaptureProperty property = (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        );
                        if (t.getNewValue()!=null)
                            property.setTimeout(Integer.parseInt(t.getNewValue()));
                        else property.setTimeout(10000);
                        FileHandle.SaveConfig(SettingProperty.captureConfig, property);
                    });
                    break;
                case "buffer":
                    column1.setOnEditCommit((TableColumn.CellEditEvent<CaptureProperty, String> t) -> {
                        CaptureProperty property = (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        );
                        if (t.getNewValue()!=null)
                            property.setBuffer(Integer.parseInt(t.getNewValue()));
                        else property.setBuffer(1024*1024*20);
                        FileHandle.SaveConfig(SettingProperty.captureConfig, property);
                    });
                    break;
                    // 混杂、监控、立即等等模式完成
//                case "promiscuous":
//                    column1.setOnEditCommit((TableColumn.CellEditEvent<CaptureProperty, String> t) -> {
//                        CaptureProperty property = (t.getTableView().getItems().get(
//                                t.getTablePosition().getRow())
//                        );
//                        if (t.getNewValue()!=null)
//                            property.setCount(Integer.parseInt(t.getNewValue()));
//                        else property.setCount(-1);
//                        FileHandle.SaveConfig(SettingProperty.captureConfig, property);
//                    });
//                    break;
//                case "immediate":
//                    column1.setOnEditCommit((TableColumn.CellEditEvent<CaptureProperty, String> t) -> {
//                        CaptureProperty property = (t.getTableView().getItems().get(
//                                t.getTablePosition().getRow())
//                        );
//                        if (t.getNewValue()!=null)
//                            property.setCount(Integer.parseInt(t.getNewValue()));
//                        else property.setCount(-1);
//                        FileHandle.SaveConfig(SettingProperty.captureConfig, property);
//                    });
//                    break;
            }

        });

    }

    public void setIndexStatusBar(IndexStatusBar bar) {
        this.indexStatusBar = bar;
    }


    @FXML
    private void AddButtonOnClicked() {
        CaptureProperty property = new CaptureProperty();
        property.setName("new config");
        property.setComment("new config");
        property.setCount(-1);
        property.setLength(262214);
        property.setBuffer(1024*1024*20);
        property.setTimeout(10000);

        configTable.getItems().add(property);
        FileHandle.SaveConfig(SettingProperty.captureConfig, property);
    }

    @FXML
    private void DeleteButtonOnClicked() {
        CaptureProperty property = (CaptureProperty) configTable.getSelectionModel().getSelectedItem();
        FileHandle.DeleteConfig(SettingProperty.captureConfig, property);
        configTable.getItems().remove(configTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void CopyButtonOnClicked() {
        CaptureProperty property = (CaptureProperty) configTable.getSelectionModel().getSelectedItem();
        if (property != null) {
            CaptureProperty newProperty = (CaptureProperty) property.clone();
            newProperty.setName(property.getName()+"(copy)");
            configTable.getItems().add(newProperty);
            FileHandle.SaveConfig(SettingProperty.captureConfig, newProperty);
        }
    }

    @FXML
    private void OkButtonOnClicked(Event event) {
        indexStatusBar.UpdateContextMenu();
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

}
