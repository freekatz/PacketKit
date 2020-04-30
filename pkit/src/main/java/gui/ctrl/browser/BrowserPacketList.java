package gui.ctrl.browser;

import gui.ctrl.View;
import gui.model.browser.PacketInfoProperty;
import gui.model.Property;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import util.ViewHandle;

public class BrowserPacketList {
    View view;

    @FXML
    TableView<Property> packetTable;

    public BrowserPacketList() {}

    public void initialize() {
        ViewHandle.InitializeTable(new PacketInfoProperty(), packetTable);
    }

    public void setView(View view) {
        this.view = view;
    }

    public TableView<Property> getPacketTable() {
        return packetTable;
    }
}
