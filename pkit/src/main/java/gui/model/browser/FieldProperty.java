package gui.model.browser;

import gui.model.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class FieldProperty implements Property {
    SimpleStringProperty name = new SimpleStringProperty(this, "name");
    SimpleStringProperty value = new SimpleStringProperty(this, "value");

    public FieldProperty(String field, String value) {
        this.nameProperty().setValue(field);
        this.valueProperty().setValue(value);
    }

    public String getValue() {
        return value.get();
    }

    public SimpleStringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public String getName() {
        return this.name.get();
    }

}
