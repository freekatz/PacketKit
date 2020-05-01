package gui.ctrl;

import gui.model.CaptureProperty;
import gui.model.FilterProperty;
import gui.model.Property;
import javafx.event.Event;

public interface View {

    String getType();

    void setType(String type);

    void close(Event event);

}
