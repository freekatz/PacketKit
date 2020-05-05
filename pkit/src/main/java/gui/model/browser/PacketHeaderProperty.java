package gui.model.browser;

import gui.model.Property;
import gui.model.config.FilterProperty;
import gui.model.packet.PPacket;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.LinkedList;

public class PacketHeaderProperty implements Property, Cloneable {
    SimpleStringProperty name = new SimpleStringProperty(this, "name");
    SimpleObjectProperty<LinkedList<PPacket>> header = new SimpleObjectProperty<>(this, "header");

    public PacketHeaderProperty() {
        LinkedList<PPacket> header = new LinkedList<>();
        this.header.setValue(header);
    }

    public LinkedList<PPacket> getHeader() {
        return header.get();
    }

    public SimpleObjectProperty<LinkedList<PPacket>> headerProperty() {
        return header;
    }

    public void setHeader(LinkedList<PPacket> header) {
        this.header.set(header);
    }

    @Override
    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public Property clone() {
        try {
            return (PacketHeaderProperty) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
