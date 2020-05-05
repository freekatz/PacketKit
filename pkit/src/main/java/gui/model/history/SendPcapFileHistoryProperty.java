package gui.model.history;

import gui.model.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.HashSet;

public class SendPcapFileHistoryProperty implements Property {
    SimpleObjectProperty<HashSet<String>> history = new SimpleObjectProperty<>(this, "history");
    SimpleStringProperty name = new SimpleStringProperty(this, "name");


    public SendPcapFileHistoryProperty() {
        HashSet<String> history = new HashSet<>();
        this.history.setValue(history);
    }

    public HashSet<String> getHistory() {
        return history.get();
    }

    public SimpleObjectProperty<HashSet<String>> historyProperty() {
        return history;
    }

    public void setHistory(HashSet<String> history) {
        this.history.set(history);
    }

    @Override
    public String getName() {
        return "sendPcapFileHistory";
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
