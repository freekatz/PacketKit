package gui.ctrl.browser;

import gui.ctrl.View;
import gui.model.browser.PacketInfoProperty;
import gui.model.Property;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import util.ViewHandle;

public class PacketList {
    View view;

    @FXML
    TableView<Property> packetTable;

    public PacketList() {}

    public void initialize() {
        ViewHandle.InitializeTable(new PacketInfoProperty(), packetTable);
        packetTable.getColumns().get(packetTable.getColumns().size()-1).setVisible(false);


        packetTable.getColumns().get(0).setPrefWidth(50);
        packetTable.getColumns().get(1).setPrefWidth(200);
        packetTable.getColumns().get(2).setPrefWidth(200);
        packetTable.getColumns().get(3).setPrefWidth(150);
        packetTable.getColumns().get(4).setPrefWidth(50);
        packetTable.getColumns().get(5).setPrefWidth(50);
        packetTable.getColumns().get(6).setPrefWidth(600);


    }

    public void setView(View view) {
        this.view = view;
    }

    public TableView<Property> getPacketTable() {
        return packetTable;
    }
}
