package model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.pcap4j.core.PcapHandle;

public class FilterProperty implements Property{

    private final SimpleStringProperty name = new SimpleStringProperty(this, "name");
    private final SimpleStringProperty expression = new SimpleStringProperty(this, "expression");
    private final SimpleStringProperty comment = new SimpleStringProperty(this, "comment");
    private final SimpleObjectProperty<PcapHandle.PcapDirection> direction = new SimpleObjectProperty<>(this, "direction");

    public PcapHandle.PcapDirection getDirection() {
        return direction.get();
    }

    public SimpleObjectProperty<PcapHandle.PcapDirection> directionProperty() {
        return direction;
    }

    public void setDirection(PcapHandle.PcapDirection direction) {
        this.direction.set(direction);
    }

    public String getComment() {
        return comment.get();
    }

    public SimpleStringProperty commentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public String getExpression() {
        return expression.get();
    }

    public SimpleStringProperty expressionProperty() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression.set(expression);
    }

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
