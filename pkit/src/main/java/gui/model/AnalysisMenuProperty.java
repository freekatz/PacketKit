package gui.model;

import javafx.beans.property.SimpleObjectProperty;

import java.util.LinkedHashMap;

public class AnalysisMenuProperty implements Property{
    SimpleObjectProperty<LinkedHashMap<String, String>> welcome = new SimpleObjectProperty<>(this, "welcome");
    SimpleObjectProperty<LinkedHashMap<String, String>> traffic = new SimpleObjectProperty<>(this, "traffic");
    SimpleObjectProperty<LinkedHashMap<String, String>> communication = new SimpleObjectProperty<>(this, "communication");
    SimpleObjectProperty<LinkedHashMap<String, String>> relation = new SimpleObjectProperty<>(this, "relation");
//    SimpleObjectProperty<LinkedHashMap<String, String>> description = new SimpleObjectProperty<>(this, "description");


    public LinkedHashMap<String, String> getWelcome() {
        return welcome.get();
    }

    public SimpleObjectProperty<LinkedHashMap<String, String>> welcomeProperty() {
        return welcome;
    }

    public void setWelcome(LinkedHashMap<String, String> welcome) {
        this.welcome.set(welcome);
    }

//    public LinkedHashMap<String, String> getDescription() {
//        return description.get();
//    }
//
//    public SimpleObjectProperty<LinkedHashMap<String, String>> descriptionProperty() {
//        return description;
//    }
//
//    public void setDescription(LinkedHashMap<String, String> description) {
//        this.description.set(description);
//    }

    public LinkedHashMap<String, String> getRelation() {
        return relation.get();
    }

    public SimpleObjectProperty<LinkedHashMap<String, String>> relationProperty() {
        return relation;
    }

    public void setRelation(LinkedHashMap<String, String> relation) {
        this.relation.set(relation);
    }

    public LinkedHashMap<String, String> getCommunication() {
        return communication.get();
    }

    public SimpleObjectProperty<LinkedHashMap<String, String>> communicationProperty() {
        return communication;
    }

    public void setCommunication(LinkedHashMap<String, String> communication) {
        this.communication.set(communication);
    }

    public LinkedHashMap<String, String> getTraffic() {
        return traffic.get();
    }

    public SimpleObjectProperty<LinkedHashMap<String, String>> trafficProperty() {
        return traffic;
    }

    public void setTraffic(LinkedHashMap<String, String> traffic) {
        this.traffic.set(traffic);
    }

    @Override
    public String getName() {
        return null;
    }
}
