package gui.model.nif;

import gui.model.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SendProperty implements Property {
    private final SimpleStringProperty name = new SimpleStringProperty(this, "name");
    private final SimpleStringProperty comment = new SimpleStringProperty(this, "comment");
    private final SimpleIntegerProperty count = new SimpleIntegerProperty(this, "count");
    private final SimpleIntegerProperty retry = new SimpleIntegerProperty(this, "retry");
    private final SimpleIntegerProperty timeout = new SimpleIntegerProperty(this, "timeout");

    public int getTimeout() {
        return timeout.get();
    }

    public SimpleIntegerProperty timeoutProperty() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout.set(timeout);
    }

    public int getRetry() {
        return retry.get();
    }

    public SimpleIntegerProperty retryProperty() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry.set(retry);
    }

    public int getCount() {
        return count.get();
    }

    public SimpleIntegerProperty countProperty() {
        return count;
    }

    public void setCount(int count) {
        this.count.set(count);
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
