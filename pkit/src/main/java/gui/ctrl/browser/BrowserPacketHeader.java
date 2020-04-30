package gui.ctrl.browser;

import gui.ctrl.View;
import gui.model.Property;
import javafx.fxml.FXML;
import javafx.scene.control.TreeView;

public class BrowserPacketHeader {
    View view;

    @FXML
    TreeView<Property> headerTree;

    public BrowserPacketHeader() {
    }

    public void initialize() {
        System.out.println(this.toString());
    }

    public void setView(View view) {
        this.view = view;
    }

    public TreeView<Property> getHeaderTree() {
        return headerTree;
    }
}
