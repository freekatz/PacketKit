package gui.ctrl.browser;

import gui.ctrl.View;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class PacketData {
    View view;

    @FXML
    BorderPane pane;

    @FXML
    ListView<HBox> dataArea;


    public PacketData() {
    }

    public void initialize() {
        dataArea.setPadding(Insets.EMPTY);
    }

    public void setView(View view) {
        this.view = view;
    }

    public ListView<HBox> getDataArea() {
        return dataArea;
    }

}
