package gui.model.browser;

import gui.model.Property;
import gui.model.config.SendProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class PacketDataProperty implements Property, Cloneable {
    SimpleStringProperty name = new SimpleStringProperty(this, "name");
    SimpleObjectProperty<char[]> txt = new SimpleObjectProperty<>(this, "txt");
    SimpleObjectProperty<String[]> hex = new SimpleObjectProperty<>(this, "hex");

    public String[] getHex() {
        return hex.get();
    }

    public SimpleObjectProperty<String[]> hexProperty() {
        return hex;
    }

    public void setHex(String[] hex) {
        this.hex.set(hex);
    }

    public char[] getTxt() {
        return txt.get();
    }

    public SimpleObjectProperty<char[]> txtProperty() {
        return txt;
    }

    public void setTxt(char[] txt) {
        this.txt.set(txt);
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
            return (PacketDataProperty) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
