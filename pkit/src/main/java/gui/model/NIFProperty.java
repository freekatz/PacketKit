package gui.model;

import gui.model.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.pcap4j.core.PcapNetworkInterface;

public class NIFProperty implements Property {
    private final SimpleStringProperty name = new SimpleStringProperty(this, "name");
    private final SimpleStringProperty easyName = new SimpleStringProperty(this, "easy name");
    private final SimpleStringProperty desc = new SimpleStringProperty(this, "desc");
    private final SimpleStringProperty mac = new SimpleStringProperty(this, "mac");
    private final SimpleStringProperty ip = new SimpleStringProperty(this, "ip");
    private final SimpleBooleanProperty local = new SimpleBooleanProperty(this, "local");
    private final SimpleBooleanProperty loopback = new SimpleBooleanProperty(this, "loopback");
    private final SimpleBooleanProperty running = new SimpleBooleanProperty(this, "running");
    private final SimpleBooleanProperty up = new SimpleBooleanProperty(this, "up");

    public NIFProperty(PcapNetworkInterface nif) {
        this.setName(nif.getName());
        this.setDesc(nif.getDescription());
        this.setMac(nif.getLinkLayerAddresses().toString());
        if (nif.getAddresses().size()==2)
            this.setIp(nif.getAddresses().get(1).getAddress().toString());
        else if (nif.getAddresses().size()==1)
            this.setIp(nif.getAddresses().get(0).toString());
        else
            this.setIp("");
        this.setLocal(nif.isLocal());
        this.setLoopback(nif.isLoopBack());
        this.setRunning(nif.isRunning());
        this.setUp(nif.isUp());
    }

    public NIFProperty() {

    }

    public String getIp() {
        return ip.get();
    }

    public SimpleStringProperty ipProperty() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip.set(ip);
    }

    public boolean isUp() {
        return up.get();
    }

    public SimpleBooleanProperty upProperty() {
        return up;
    }

    public void setUp(boolean up) {
        this.up.set(up);
    }

    public boolean isRunning() {
        return running.get();
    }

    public SimpleBooleanProperty runningProperty() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running.set(running);
    }

    public boolean isLoopback() {
        return loopback.get();
    }

    public SimpleBooleanProperty loopbackProperty() {
        return loopback;
    }

    public void setLoopback(boolean loopback) {
        this.loopback.set(loopback);
    }

    public boolean isLocal() {
        return local.get();
    }

    public SimpleBooleanProperty localProperty() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local.set(local);
    }

    public String getMac() {
        return mac.get();
    }

    public SimpleStringProperty macProperty() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac.set(mac);
    }

    public String getDesc() {
        return desc.get();
    }

    public SimpleStringProperty descProperty() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc.set(desc);
    }

    public String getEasyName() {
        return easyName.get();
    }

    public SimpleStringProperty easyNameProperty() {
        return easyName;
    }

    public void setEasyName(String easyName) {
        this.easyName.set(easyName);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
