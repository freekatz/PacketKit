package gui.model.analysis;

import javafx.beans.property.SimpleObjectProperty;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Ipv4StatBarProperty {
    SimpleObjectProperty<LinkedHashMap<String, Double>> data = new SimpleObjectProperty<>(this, "data");

    public Ipv4StatBarProperty() {
        LinkedHashMap<String, Double> data = new LinkedHashMap<>();
        this.data.setValue(data);
    }

    public LinkedHashMap<String, Double> getData() {
        return data.get();
    }

    public SimpleObjectProperty<LinkedHashMap<String, Double>> dataProperty() {
        return data;
    }

    public void setData(LinkedHashMap<String, Double> data) {
        this.data.set(data);
    }
}
