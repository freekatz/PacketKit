package gui.ctrl;

import gui.ctrl.bar.IndexStatusBar;
import gui.ctrl.list.IndexFileList;
import gui.ctrl.list.IndexNIFList;
import gui.model.CaptureProperty;
import gui.model.FilterProperty;
import gui.model.Property;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import util.ViewHandle;

import java.io.IOException;

public class IndexView implements View{
    public FilterProperty filterProperty;
    public CaptureProperty captureProperty;
    public String pcapFile;
    public String nifName;

    @FXML
    BorderPane pane;

    @FXML
    VBox topBox;

    @FXML
    VBox centerBox;

    public IndexView() {}

    public void initialize() {

        // init two config and set statusbar
        ViewHandle.InitializeCaptureTopBox(topBox, this);
        try {
            FXMLLoader nifListLoader = ViewHandle.GetLoader("gui/view/list/IndexNIFList.fxml");
            AnchorPane nifListPane = nifListLoader.load();
            nifListPane.setMaxWidth(Double.MAX_VALUE);
            IndexNIFList indexNIFList = nifListLoader.getController();
            indexNIFList.setView(this);
            FXMLLoader fileListLoader = ViewHandle.GetLoader("gui/view/list/IndexFileList.fxml");
            AnchorPane fileListPane = fileListLoader.load();
            fileListPane.setMaxWidth(Double.MAX_VALUE);
            IndexFileList indexFileList = fileListLoader.getController();
            indexFileList.setView(this);
            centerBox.getChildren().addAll(fileListPane, nifListPane);
            centerBox.setSpacing(10);

            FXMLLoader statusBarLoader = ViewHandle.GetLoader("gui/view/bar/IndexStatusBar.fxml");
            AnchorPane statusBatPane = statusBarLoader.load();
            statusBatPane.setMaxWidth(Double.MAX_VALUE);
            IndexStatusBar indexStatusBar = statusBarLoader.getController();
            indexStatusBar.setView(this);
            pane.setBottom(statusBatPane);
            pane.getBottom().setLayoutX(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void setCaptureProperty(CaptureProperty captureProperty) {
        this.captureProperty = captureProperty;
    }

    @Override
    public CaptureProperty getCaptureProperty() {
        return captureProperty;
    }

    @Override
    public void setFilterProperty(FilterProperty filterProperty) {
        this.filterProperty = filterProperty;
    }

    @Override
    public FilterProperty getFilterProperty() {
        return filterProperty;
    }


    @Override
    public void close(Event event) {
        if (event.getSource().getClass().equals(Button.class)) {
            Stage stage = (Stage)((Button)(event).getSource()).getScene().getWindow();
            stage.close();
        }
        else if (event.getSource().getClass().equals(ListView.class)){
            Stage stage = (Stage)((ListView)(event).getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void setNifName(String nifName) {
        this.nifName = nifName;
    }

    @Override
    public String getNifName() {
        return nifName;
    }

    @Override
    public String getPcapFile() {
        return pcapFile;
    }

    @Override
    public void setPcapFile(String pcapFile) {
        this.pcapFile = pcapFile;
    }

    @Override
    public String getFilterExpression() {
        return filterProperty.getExpression();
    }

    @Override
    public void setFilterExpression(String filterExpression) {
        filterProperty.setExpression(filterExpression);
    }
}
