package gui.ctrl.browser;

import gui.ctrl.View;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class PacketData {
    View view;

    @FXML
    BorderPane pane;

    @FXML
    ListView<String> indexList;

    @FXML
    TextArea hexArea;

    @FXML
    TextArea txtArea;


    public PacketData() {
    }

    public void initialize() {

    }

    public void setView(View view) {
        this.view = view;
    }

    public ListView<String> getIndexList() {
        return indexList;
    }

    public TextArea getTxtArea() {
        return txtArea;
    }

    public TextArea getHexArea() {
        return hexArea;
    }
}
