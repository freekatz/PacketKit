package gui.ctrl;

import javafx.event.Event;

public interface View {

    String getType();

    void setType(String type);

    void close(Event event);

}
