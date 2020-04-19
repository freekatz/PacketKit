package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import java.util.List;

public class NIFHandle {

    public static void InitializeNIF(ComboBox<Label> box) {
        List nifList = null;
        try {
            nifList = Pcaps.findAllDevs();
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }
        PcapNetworkInterface nif;
        ObservableList<Label> ob = FXCollections.observableArrayList();
        if (nifList != null && nifList.size() != 0) {
            for (int i = 0; i < nifList.size(); i++) {
                nif = (PcapNetworkInterface) nifList.get(i);
                Label label = new Label(nif.getName());
                label.setId(nif.getName());
                ob.add(label);
            }
            box.setItems(ob);
            box.setValue(ob.get(0));
        }
    }
}
