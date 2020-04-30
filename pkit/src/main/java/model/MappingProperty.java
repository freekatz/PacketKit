package model;

import javafx.beans.property.SimpleStringProperty;

public class MappingProperty implements Property{

    private final SimpleStringProperty key = new SimpleStringProperty(this, "key");
    private final SimpleStringProperty value = new SimpleStringProperty(this, "value");

    public String getValue() {
        return value.get();
    }

    public SimpleStringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    public String getKey() {
        return key.get();
    }

    public SimpleStringProperty keyProperty() {
        return key;
    }

    public void setKey(String key) {
        this.key.set(key);
    }
}
