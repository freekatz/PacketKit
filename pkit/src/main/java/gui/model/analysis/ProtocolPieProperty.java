package gui.model.analysis;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.HashMap;

public class ProtocolPieProperty {
    SimpleObjectProperty<HashMap<String, Double>> data = new SimpleObjectProperty<>(this, "data");

    public ProtocolPieProperty() {
        HashMap<String, Double> data = new HashMap<>();
        this.data.setValue(data);
    }

    public HashMap<String, Double> getData() {
        return data.get();
    }

    public SimpleObjectProperty<HashMap<String, Double>> dataProperty() {
        return data;
    }

    public void setData(HashMap<String, Double> data) {
        this.data.set(data);
    }
}
