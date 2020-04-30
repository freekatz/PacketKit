package gui.model;

import javafx.beans.property.SimpleStringProperty;

public class FilterProperty implements Property, Cloneable{

    private final SimpleStringProperty name = new SimpleStringProperty(this, "name");
    private final SimpleStringProperty expression = new SimpleStringProperty(this, "expression");
    private final SimpleStringProperty comment = new SimpleStringProperty(this, "comment");

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
            return (FilterProperty) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
