package gui.ctrl;

import gui.model.JobMode;
import gui.model.ViewType;
import javafx.event.Event;

public interface View {

    ViewType getType();

    void setType(ViewType type);

    void JobScheduler(JobMode jobMode);

    void JobStop();

    void close(Event event);

}
