package gui.ctrl;

import gui.bak.model.SettingProperty;
import gui.ctrl.browser.BrowserPacketData;
import gui.ctrl.browser.BrowserPacketHeader;
import gui.ctrl.browser.BrowserPacketList;
import gui.model.CaptureProperty;
import gui.model.FilterProperty;
import gui.model.Property;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import util.Job;
import util.ViewHandle;
import util.nif.CNIF;

import java.io.File;
import java.io.IOException;

public class CaptureView implements View{
    IndexView indexView;
    String filterExpression;
    CaptureProperty captureProperty;
    String pcapFile;
    String nifName;
    BrowserPacketList packetListCtrl;
    BrowserPacketHeader packetHeaderCtrl;
    BrowserPacketData packetDataCtrl;

    CNIF cnif;

    Job.OfflineJob offlineJob;
    Job.OnlineJob onlineJob;

    @FXML
    BorderPane pane;

    @FXML
    VBox topBox;

    @FXML
    SplitPane browserPane;

    public CaptureView() {}

    public void initialize() {
        ViewHandle.InitializeCaptureTopBox(topBox, this);
        this.InitializeBrowser();
        try {
            FXMLLoader statusBarLoader = ViewHandle.GetLoader("gui/view/bar/IndexStatusBar.fxml");
            AnchorPane statusBatPane = statusBarLoader.load();
            statusBatPane.setMaxWidth(Double.MAX_VALUE);
            pane.setBottom(statusBatPane);
            pane.getBottom().setLayoutX(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void InitializeBrowser() {
        try {
            FXMLLoader packetListLoader = ViewHandle.GetLoader("gui/view/browser/BrowserPacketList.fxml");
            AnchorPane packetListPane = packetListLoader.load();
            packetListPane.setMaxWidth(Double.MAX_VALUE);
            packetListCtrl = packetListLoader.getController();
            FXMLLoader packetHeaderLoader = ViewHandle.GetLoader("gui/view/browser/BrowserPacketHeader.fxml");
            AnchorPane packetHeaderPane = packetHeaderLoader.load();
            packetHeaderPane.setMaxWidth(Double.MAX_VALUE);
            packetHeaderCtrl = packetHeaderLoader.getController();
            FXMLLoader packetDataLoader = ViewHandle.GetLoader("gui/view/browser/BrowserPacketData.fxml");
            AnchorPane packetDataPane = packetDataLoader.load();
            packetDataPane.setMaxWidth(Double.MAX_VALUE);
            packetDataCtrl = packetDataLoader.getController();

            browserPane.getItems().addAll(packetListPane, packetHeaderPane, packetDataPane);

            for (int i = 0; i < browserPane.getDividers().size(); i++) {
                browserPane.getDividers().get(i).setPosition((i + 1.0) / browserPane.getItems().size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public IndexView getIndexView() {
        return indexView;
    }

    public void setIndexView(IndexView indexView) {
        this.indexView = indexView;
    }

    public CNIF getCnif() {
        return cnif;
    }

    public void setCnif(CNIF cnif) {
        this.cnif = cnif;
    }

    public void setNifName(String nifName) {
        this.nifName = nifName;
    }

    public void setPcapFile(String pcapFile) {
        this.pcapFile = pcapFile;
    }

    public void setCaptureProperty(CaptureProperty captureProperty) {
        this.captureProperty = captureProperty;
    }

    public void setFilterExpression(String filterExpression) {
        this.filterExpression = filterExpression;
    }

    public String getNifName() {
        return nifName;
    }

    public SplitPane getBrowserPane() {
        return browserPane;
    }

    public String getPcapFile() {
        return pcapFile;
    }

    public CaptureProperty getCaptureProperty() {
        return captureProperty;
    }

    @Override
    public void setFilterProperty(FilterProperty filterProperty) {
        this.filterExpression = filterProperty.getExpression();
    }

    @Override
    public FilterProperty getFilterProperty() {
        FilterProperty t = new FilterProperty();
        t.setExpression(filterExpression);
        return t;
    }

    public String getFilterExpression() {
        return filterExpression;
    }

    public BrowserPacketData getPacketDataCtrl() {
        return packetDataCtrl;
    }

    public BrowserPacketHeader getPacketHeaderCtrl() {
        return packetHeaderCtrl;
    }

    public BrowserPacketList getPacketListCtrl() {
        return packetListCtrl;
    }

    public void clear() {
        // clear table...
        packetListCtrl.getPacketTable().getItems().clear();
//        packetHeaderCtrl.getHeaderTree(); // clear tree
        packetDataCtrl.getIndexList().getItems().clear();
        packetDataCtrl.getHexArea().setText("");
        packetDataCtrl.getTxtArea().setText("");

    }

    public void CaptureControl() {
        this.clear();
        if (nifName==null) {
            if (cnif==null)
            cnif = new CNIF(pcapFile);
            offlineJob = new Job.OfflineJob(this);
            Thread thread = new Thread(offlineJob);
            thread.start();
        }
        else {
            if (cnif!=null) {
                cnif.handle.close();
                cnif.dumper.close();
            }
            cnif = new CNIF(nifName, captureProperty);
            onlineJob = new Job.OnlineJob(this);
            Thread thread = new Thread(onlineJob);
            thread.start();
        }
    }


    @Override
    public void close(Event event) {

    }
}
