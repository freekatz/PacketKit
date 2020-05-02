package gui.ctrl.browser;

import gui.ctrl.View;
import gui.model.Property;
import javafx.fxml.FXML;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;

public class PacketHeader {
    View view;

    @FXML
    TreeTableView<String> headerTreeTable;

    @FXML
    TreeTableColumn<String, String> fieldColumn;

    @FXML
    TreeTableColumn<String, String> valueColumn;

    public PacketHeader() {
    }

    public void initialize() {
    }

    public void setView(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public TreeTableColumn<String, String> getValueColumn() {
        return valueColumn;
    }

    public void setValueColumn(TreeTableColumn<String, String> valueColumn) {
        this.valueColumn = valueColumn;
    }

    public TreeTableColumn<String, String> getFieldColumn() {
        return fieldColumn;
    }

    public void setFieldColumn(TreeTableColumn<String, String> fieldColumn) {
        this.fieldColumn = fieldColumn;
    }

    public TreeTableView<String> getHeaderTreeTable() {
        return headerTreeTable;
    }

    public void setHeaderTreeTable(TreeTableView<String> headerTreeTable) {
        this.headerTreeTable = headerTreeTable;
    }
}
