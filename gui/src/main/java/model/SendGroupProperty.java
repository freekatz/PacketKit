package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

// send group
public class SendGroupProperty implements Property{
    private final SimpleStringProperty name = new SimpleStringProperty(this, "name");
    private final SimpleStringProperty comment = new SimpleStringProperty(this, "comment");
    private final SimpleIntegerProperty size = new SimpleIntegerProperty(this, "size");
    private final SimpleStringProperty folder = new SimpleStringProperty(this, "folder");

    public String getFolder() {
        return folder.get();
    }

    public SimpleStringProperty folderProperty() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder.set(folder);
    }

    public int getSize() {
        return size.get();
    }

    public SimpleIntegerProperty sizeProperty() {
        return size;
    }

    public void setSize(int size) {
        this.size.set(size);
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
