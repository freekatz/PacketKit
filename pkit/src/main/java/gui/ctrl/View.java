package gui.ctrl;

import gui.model.CaptureProperty;
import gui.model.FilterProperty;
import gui.model.Property;
import javafx.event.Event;

public interface View {

    void setCaptureProperty(CaptureProperty captureProperty);

    CaptureProperty getCaptureProperty();

    void setFilterProperty(FilterProperty filterProperty);

    FilterProperty getFilterProperty();

    void close(Event event);

    String getNifName();

    void setNifName(String nifName);

    String getPcapFile();

    void setPcapFile(String pcapFile);

    String getFilterExpression();

    void setFilterExpression(String filterExpression);
}
