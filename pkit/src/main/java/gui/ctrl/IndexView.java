package gui.ctrl;

import gui.ctrl.bar.CaptureToolBar;
import gui.ctrl.bar.FilterBar;
import gui.ctrl.bar.CaptureMenuBar;
import gui.ctrl.bar.CaptureStatusBar;
import gui.ctrl.browser.PacketData;
import gui.ctrl.browser.PacketHeader;
import gui.ctrl.browser.PacketList;
import gui.ctrl.list.FileList;
import gui.ctrl.list.NIFList;
import gui.model.CaptureProperty;
import gui.model.FilterProperty;
import gui.model.SettingProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import util.*;
import util.job.AnalysisJob;
import util.job.OfflineJob;
import util.job.OnlineJob;
import util.job.SaveJob;
import util.nif.CNIF;

import java.util.Objects;

public class IndexView implements View{
    private FilterProperty filterProperty;
    private CaptureProperty captureProperty;
    private String pcapFile;
    private String nifName;
    private String savePath;

    private FileList fileListCtrl;
    private NIFList nifListCtrl;

    private PacketList packetListCtrl;
    private PacketHeader packetHeaderCtrl;
    private PacketData packetDataCtrl;

    private CaptureMenuBar captureMenuBarCtrl;
    private CaptureToolBar captureToolBarCtrl;
    private FilterBar filterBarCtrl;
    private CaptureStatusBar captureStatusBarCtrl;

    private Label status;

    private String type;

    private CNIF cnif;

    @FXML
    BorderPane pane;

    @FXML
    VBox topBox;

    VBox centerBox;
    SplitPane browserPane;

    public IndexView() {}

    public void initialize() {

        centerBox = new VBox();
        browserPane = new SplitPane();
        this.type = "index";
        // init two config and set statusbar
        browserPane.setOrientation(Orientation.VERTICAL);

        ViewHandle.InitializeTop(this);
        ViewHandle.InitializeCenter(this);
        ViewHandle.InitializeBottom(this);

        status = captureStatusBarCtrl.statusLabel;

        status.setText("ready");
    }

    public void StartCapture(String opt) {
        captureStatusBarCtrl.configButton.setDisable(true);
        // button logic
        if (!opt.equals("analysis")) {
            if (pcapFile != null) {
                for (int i = 0; i < 5; i++)
                    captureToolBarCtrl.getToolBar().getItems().get(i).setDisable(true);
                for (int i = 6; i < 9; i++)
                    captureToolBarCtrl.getToolBar().getItems().get(i).setDisable(false);

                for (int i = 2; i < 4; i++)
                    captureMenuBarCtrl.getFileMenu().getItems().get(i).setDisable(false);
            } else {
                captureToolBarCtrl.getToolBar().getItems().get(0).setDisable(true);
                captureToolBarCtrl.getToolBar().getItems().get(1).setDisable(false);
                captureToolBarCtrl.getToolBar().getItems().get(2).setDisable(false);
                for (int i = 3; i < 9; i++)
                    captureToolBarCtrl.getToolBar().getItems().get(i).setDisable(true);

                for (int i = 0; i < 5; i++)
                    captureMenuBarCtrl.getFileMenu().getItems().get(i).setDisable(true);

            }
        }
        // capture ctrl
        switch (opt) {
            case "online": {
                status.setText("capture");
                cnif = new CNIF(nifName, captureProperty);
                OnlineJob onlineJob = new OnlineJob(this);
                Thread thread = new Thread(onlineJob);
                thread.start();
                break;
            }
            case "offline": {
                status.setText("read");
                OfflineJob offlineJob = new OfflineJob(this);
                Thread thread = new Thread(offlineJob);
                thread.start();
                break;
            }
            case "analysis": {
                status.setText("analysis");
                String path;
                if (fileListCtrl.getFileList().getSelectionModel().getSelectedItem()==null && type.equals("index"))
                    return;
                else if (fileListCtrl.getFileList().getSelectionModel().getSelectedItem()!=null && type.equals("index"))
                    path = fileListCtrl.getFileList().getSelectionModel().getSelectedItem().replaceAll("\\(.*?\\)", "");
                else
                    path = Objects.requireNonNullElseGet(pcapFile, () -> SettingProperty.tempPcapFolder + "/tmp.pcapng");
                AnalysisJob analysisJob = new AnalysisJob(path);
                Thread thread = new Thread(analysisJob);
                thread.start();
                break;
            }
            case "apply": {
                status.setText("apply");
                String path = Objects.requireNonNullElseGet(pcapFile, () -> SettingProperty.tempPcapFolder + "/tmp.pcapng");
                OfflineJob offlineJob = new OfflineJob(this, path);
                Thread thread = new Thread(offlineJob);
                thread.start();
                break;
            }
            case "save": {
                String path = Objects.requireNonNullElseGet(pcapFile, () -> SettingProperty.tempPcapFolder + "/tmp.pcapng");
                SaveJob saveJob = new SaveJob(this, path, savePath, filterProperty.getExpression());
                Thread thread = new Thread(saveJob);
                thread.start();
            }
            default:
                break;
        }
    }

    public void StopCapture() {
        captureStatusBarCtrl.configButton.setDisable(false);
        cnif.handle.close();
        cnif.dumper.close();
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getStatus() {
        return status.getText();
    }

    public void setStatus(String status) {
        this.status.setText(status);
    }

    public CNIF getCnif() {
        return cnif;
    }

    public void setCnif(CNIF cnif) {
        this.cnif = cnif;
    }

    public CaptureStatusBar getCaptureStatusBarCtrl() {
        return captureStatusBarCtrl;
    }

    public void setCaptureStatusBarCtrl(CaptureStatusBar captureStatusBarCtrl) {
        this.captureStatusBarCtrl = captureStatusBarCtrl;
    }

    public FilterBar getFilterBarCtrl() {
        return filterBarCtrl;
    }

    public void setFilterBarCtrl(FilterBar filterBarCtrl) {
        this.filterBarCtrl = filterBarCtrl;
    }

    public CaptureToolBar getCaptureToolBarCtrl() {
        return captureToolBarCtrl;
    }

    public void setCaptureToolBarCtrl(CaptureToolBar captureToolBarCtrl) {
        this.captureToolBarCtrl = captureToolBarCtrl;
    }

    public CaptureMenuBar getCaptureMenuBarCtrl() {
        return captureMenuBarCtrl;
    }

    public void setCaptureMenuBarCtrl(CaptureMenuBar captureMenuBarCtrl) {
        this.captureMenuBarCtrl = captureMenuBarCtrl;
    }

    public NIFList getNifListCtrl() {
        return nifListCtrl;
    }

    public void setNifListCtrl(NIFList nifListCtrl) {
        this.nifListCtrl = nifListCtrl;
    }

    public FileList getFileListCtrl() {
        return fileListCtrl;
    }

    public void setFileListCtrl(FileList fileListCtrl) {
        this.fileListCtrl = fileListCtrl;
    }

    public SplitPane getBrowserPane() {
        return browserPane;
    }

    public VBox getCenterBox() {
        return centerBox;
    }

    public VBox getTopBox() {
        return topBox;
    }

    public BorderPane getPane() {
        return pane;
    }

    public PacketData getPacketDataCtrl() {
        return packetDataCtrl;
    }

    public void setPacketDataCtrl(PacketData packetDataCtrl) {
        this.packetDataCtrl = packetDataCtrl;
    }

    public PacketHeader getPacketHeaderCtrl() {
        return packetHeaderCtrl;
    }

    public void setPacketHeaderCtrl(PacketHeader packetHeaderCtrl) {
        this.packetHeaderCtrl = packetHeaderCtrl;
    }

    public PacketList getPacketListCtrl() {
        return packetListCtrl;
    }

    public void setPacketListCtrl(PacketList packetListCtrl) {
        this.packetListCtrl = packetListCtrl;
    }

    public String getNifName() {
        return nifName;
    }

    public void setNifName(String nifName) {
        this.nifName = nifName;
    }

    public String getPcapFile() {
        return pcapFile;
    }

    public void setPcapFile(String pcapFile) {
        this.pcapFile = pcapFile;
    }

    public CaptureProperty getCaptureProperty() {
        return captureProperty;
    }

    public void setCaptureProperty(CaptureProperty captureProperty) {
        this.captureProperty = captureProperty;
    }

    public FilterProperty getFilterProperty() {
        return filterProperty;
    }

    public void setFilterProperty(FilterProperty filterProperty) {
        this.filterProperty = filterProperty;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
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

    public void clearBrowser() {
        this.getPacketListCtrl().getPacketTable().getItems().clear();
//        indexView.getPacketHeaderCtrl().getHeaderTree() // clean tree
        this.getPacketDataCtrl().getIndexList().getItems().clear();
        this.getPacketDataCtrl().getHexArea().setText("");
        this.getPacketDataCtrl().getTxtArea().setText("");
    }
}
