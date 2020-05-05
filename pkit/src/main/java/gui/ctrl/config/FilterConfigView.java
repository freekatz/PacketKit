package gui.ctrl.config;

import gui.ctrl.View;
import gui.ctrl.bar.FilterBar;
import gui.model.config.FilterProperty;
import gui.model.Property;
import gui.model.SettingProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import util.FileHandle;
import util.ViewHandle;

public class FilterConfigView implements View {
    SettingProperty settingProperty = new SettingProperty();

    FilterBar filterBar;

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


    public FilterConfigView() {}

    public void initialize() {
        ViewHandle.InitializeTable(new FilterProperty(), configTable);
        ViewHandle.UpdateConfigTable(settingProperty.filterConfig, new FilterProperty(), configTable);
        this.InitializeColumn();
    }

    private void InitializeColumn() {
        configTable.getColumns().forEach(column -> {
            TableColumn<FilterProperty, String> column1 = (TableColumn) column;
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
                    column1.setOnEditCommit((TableColumn.CellEditEvent<FilterProperty, String> t) -> {
                        FilterProperty property = (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        );
                        FileHandle.DeleteConfig(settingProperty.filterConfig, property);
                        if (t.getNewValue()!=null)
                            property.setName(t.getNewValue());
                        else property.setName("default");
                        FileHandle.SaveConfig(settingProperty.filterConfig, property);
                    });
                    break;
                case "expression":
                    column1.setOnEditCommit((TableColumn.CellEditEvent<FilterProperty, String> t) -> {
                        FilterProperty property = (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        );
                        if (t.getNewValue()!=null)
                            property.setExpression(t.getNewValue());
                        else property.setExpression("");
                        FileHandle.SaveConfig(settingProperty.filterConfig, property);
                    });
                    break;
                case "comment":
                    column1.setOnEditCommit((TableColumn.CellEditEvent<FilterProperty, String> t) -> {
                        FilterProperty property = (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        );
                        if (t.getNewValue()!=null)
                            property.setComment(t.getNewValue());
                        else property.setComment("");
                        FileHandle.SaveConfig(settingProperty.filterConfig, property);
                    });
                    break;
            }

        });

    }

    public void setFilterBar(FilterBar bar) {
        this.filterBar = bar;
    }

    @FXML
    private void AddButtonOnClicked() {
        FilterProperty property = new FilterProperty();
        property.setName("new config");
        property.setExpression("tcp");
        property.setComment("new config");

        configTable.getItems().add(property);
        FileHandle.SaveConfig(settingProperty.filterConfig, property);
    }

    @FXML
    private void DeleteButtonOnClicked() {
        FilterProperty property = (FilterProperty) configTable.getSelectionModel().getSelectedItem();
        FileHandle.DeleteConfig(settingProperty.filterConfig, property);
        configTable.getItems().remove(configTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void CopyButtonOnClicked() {
        FilterProperty property = (FilterProperty) configTable.getSelectionModel().getSelectedItem();
        if (property != null) {
            FilterProperty newProperty = (FilterProperty) property.clone();
            newProperty.setName(property.getName()+"(copy)");
            configTable.getItems().add(newProperty);
            FileHandle.SaveConfig(settingProperty.filterConfig, newProperty);
        }
    }

    @FXML
    private void OkButtonOnClicked(Event event) {
        filterBar.UpdateContextMenu();
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
        return null;
    }

    @Override
    public void setType(String type) {

    }

    @Override
    public void close(Event event) {

    }
}
