package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.pcap4j.core.PcapNetworkInterface;

public class CaptureProperty implements Property{
    private final SimpleStringProperty name = new SimpleStringProperty(this, "name");
    private final SimpleStringProperty comment = new SimpleStringProperty(this, "comment");
    private final SimpleIntegerProperty count = new SimpleIntegerProperty(this, "count");
    private final SimpleIntegerProperty length = new SimpleIntegerProperty(this, "length");
    private final SimpleIntegerProperty timeout = new SimpleIntegerProperty(this, "timeout");
    private final SimpleIntegerProperty buffer = new SimpleIntegerProperty(this, "buffer");
    private final SimpleObjectProperty<PcapNetworkInterface.PromiscuousMode> promiscuous = new SimpleObjectProperty<>(this, "promiscuous");
    private final SimpleObjectProperty<NIFMode.ImmediateMode> immediate = new SimpleObjectProperty<>(this, "immediate");


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

    public NIFMode.ImmediateMode getImmediate() {
        return immediate.get();
    }

    public SimpleObjectProperty<NIFMode.ImmediateMode> immediateProperty() {
        return immediate;
    }

    public void setImmediate(NIFMode.ImmediateMode immediate) {
        this.immediate.set(immediate);
    }

    public PcapNetworkInterface.PromiscuousMode getPromiscuous() {
        return promiscuous.get();
    }

    public SimpleObjectProperty<PcapNetworkInterface.PromiscuousMode> promiscuousProperty() {
        return promiscuous;
    }

    public void setPromiscuous(PcapNetworkInterface.PromiscuousMode promiscuous) {
        this.promiscuous.set(promiscuous);
    }

    public int getBuffer() {
        return buffer.get();
    }

    public SimpleIntegerProperty bufferProperty() {
        return buffer;
    }

    public void setBuffer(int buffer) {
        this.buffer.set(buffer);
    }

    public int getTimeout() {
        return timeout.get();
    }

    public SimpleIntegerProperty timeoutProperty() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout.set(timeout);
    }

    public int getLength() {
        return length.get();
    }

    public SimpleIntegerProperty lengthProperty() {
        return length;
    }

    public void setLength(int length) {
        this.length.set(length);
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
}
