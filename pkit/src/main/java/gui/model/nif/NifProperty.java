package gui.model.nif;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

public class NifProperty {
    private final SimpleStringProperty name = new SimpleStringProperty(this, "name");
    private final SimpleStringProperty easyName = new SimpleStringProperty(this, "easy name");
    private final SimpleStringProperty desc = new SimpleStringProperty(this, "desc");
    private final SimpleStringProperty mac = new SimpleStringProperty(this, "mac");
    private final SimpleStringProperty ip = new SimpleStringProperty(this, "ip");
    private final SimpleBooleanProperty local = new SimpleBooleanProperty(this, "local");
    private final SimpleBooleanProperty loopback = new SimpleBooleanProperty(this, "loopback");
    private final SimpleBooleanProperty running = new SimpleBooleanProperty(this, "running");
    private final SimpleBooleanProperty up = new SimpleBooleanProperty(this, "up");

    public NifProperty(String nifName) throws PcapNativeException {
        PcapNetworkInterface nif = Pcaps.getDevByName(nifName);
    }
}

