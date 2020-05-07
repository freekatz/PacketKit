package gui.model.analysis;

import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SankeyProperty {
    SimpleObjectProperty<LinkedHashMap<String, Integer>> data = new SimpleObjectProperty<>(this, "data");

    public SankeyProperty() {
        LinkedHashMap<String, Integer> data = new LinkedHashMap<>();
        this.data.setValue(data);
    }

    public LinkedHashMap<String, Integer> getData() {
        return data.get();
    }

    public SimpleObjectProperty<LinkedHashMap<String, Integer>> dataProperty() {
        return data;
    }

    public void setData(LinkedHashMap<String, Integer> data) {
        this.data.set(data);
    }
}
