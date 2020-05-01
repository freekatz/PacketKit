package gui.ctrl.browser;

import gui.ctrl.View;
import gui.model.Property;
import javafx.fxml.FXML;
import javafx.scene.control.TreeView;

public class PacketHeader {
    View view;

    @FXML
    TreeView<Property> headerTree;

    public PacketHeader() {
    }

    public void initialize() {
    }

    public void setView(View view) {
        this.view = view;
    }

    public TreeView<Property> getHeaderTree() {
        return headerTree;
    }
}
