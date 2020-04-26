package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.pcap4j.core.PcapHandle;

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

    public static PcapHandle.PcapDirection GetDirection(ComboBox<Label> direction) {
        PcapHandle.PcapDirection pcapDirection;
        switch (direction.getSelectionModel().getSelectedItem().getId()) {
            case "inLabel":
                pcapDirection = PcapHandle.PcapDirection.IN;
                break;
            case "outLabel":
                pcapDirection = PcapHandle.PcapDirection.OUT;
                break;
            case "allLabel":
                pcapDirection = PcapHandle.PcapDirection.INOUT;
                break;
            default:
                pcapDirection = PcapHandle.PcapDirection.INOUT;
                break;
        }

        return pcapDirection;
    }
}
