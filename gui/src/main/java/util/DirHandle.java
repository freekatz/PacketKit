package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class DirHandle {

    public static void InitializeDirection(ComboBox<Label> box) {
        ObservableList<Label> ob = FXCollections.observableArrayList();
        Label inLabel = new Label("入方向");
        inLabel.setId("inLabel");
        Label outLabel = new Label("出方向");
        outLabel.setId("outLabel");
        Label allLabel = new Label("所有方向");
        ob.addAll(inLabel, outLabel, allLabel);
        allLabel.setId("allLabel");
        box.setItems(ob);
        box.setValue(allLabel);
    }
}
