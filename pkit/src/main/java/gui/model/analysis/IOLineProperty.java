package gui.model.analysis;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.LinkedList;

public class IOLineProperty {
    SimpleLongProperty pointStart = new SimpleLongProperty(this, "pointStart");
    SimpleLongProperty pointInterval = new SimpleLongProperty(this, "pointInterval");
    SimpleIntegerProperty dataLength = new SimpleIntegerProperty(this, "dataLength");
    SimpleObjectProperty<LinkedList<LinkedList<Integer>>> data = new SimpleObjectProperty<>(this, "data");

    public IOLineProperty(long pointStart, long pointInterval) {
        this.pointStart.setValue(pointStart);
        this.pointInterval.setValue(pointInterval);
        this.dataLength.setValue(0);
        LinkedList<LinkedList<Integer>> data = new LinkedList<>();
        LinkedList<Integer> pn = new LinkedList<>();
        LinkedList<Integer> bn = new LinkedList<>();
        LinkedList<Integer> rpn = new LinkedList<>();
        LinkedList<Integer> spn = new LinkedList<>();
        data.add(pn);
        data.add(bn);
        data.add(rpn);
        data.add(spn);
        this.data.setValue(data);
    }

    public IOLineProperty(long pointStart) {
        this.pointStart.setValue(pointStart);
        this.pointInterval.setValue(1000);
        this.dataLength.setValue(0);
        LinkedList<LinkedList<Integer>> data = new LinkedList<>();
        LinkedList<Integer> pn = new LinkedList<>();
        LinkedList<Integer> bn = new LinkedList<>();
        data.add(pn);
        data.add(bn);
        this.data.setValue(data);
    }

    public LinkedList<LinkedList<Integer>> getData() {
        return data.get();
    }

    public SimpleObjectProperty<LinkedList<LinkedList<Integer>>> dataProperty() {
        return data;
    }

    public void setData(LinkedList<LinkedList<Integer>> data) {
        this.data.set(data);
    }

    public int getDataLength() {
        return dataLength.get();
    }

    public SimpleIntegerProperty dataLengthProperty() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength.set(dataLength);
    }

    public long getPointInterval() {
        return pointInterval.get();
    }

    public SimpleLongProperty pointIntervalProperty() {
        return pointInterval;
    }

    public void setPointInterval(long pointInterval) {
        this.pointInterval.set(pointInterval);
    }

    public long getPointStart() {
        return pointStart.get();
    }

    public SimpleLongProperty pointStartProperty() {
        return pointStart;
    }

    public void setPointStart(long pointStart) {
        this.pointStart.set(pointStart);
    }

}
