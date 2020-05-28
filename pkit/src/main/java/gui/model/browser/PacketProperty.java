package gui.model.browser;

import gui.model.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class PacketProperty implements Property {
    SimpleStringProperty name = new SimpleStringProperty(this, "name");
    SimpleObjectProperty<PacketInfoProperty> info = new SimpleObjectProperty<>(this, "info");
    SimpleObjectProperty<PacketHeaderProperty> header = new SimpleObjectProperty<>(this, "header");
    SimpleObjectProperty<PacketDataProperty> data = new SimpleObjectProperty<>(this, "data");

    public PacketDataProperty getData() {
        return data.get();
    }

    public SimpleObjectProperty<PacketDataProperty> dataProperty() {
        return data;
    }

    public void setData(PacketDataProperty data) {
        this.data.set(data);
    }

    public PacketHeaderProperty getHeader() {
        return header.get();
    }

    public SimpleObjectProperty<PacketHeaderProperty> headerProperty() {
        return header;
    }

    public void setHeader(PacketHeaderProperty header) {
        this.header.set(header);
    }

    public PacketInfoProperty getInfo() {
        return info.get();
    }

    public SimpleObjectProperty<PacketInfoProperty> infoProperty() {
        return info;
    }

    public void setInfo(PacketInfoProperty info) {
        this.info.set(info);
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
}
