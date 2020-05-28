package gui.ctrl;

import gui.ctrl.bar.CaptureMenuBar;
import gui.ctrl.bar.CaptureStatusBar;
import gui.ctrl.bar.CaptureToolBar;
import gui.ctrl.bar.FilterBar;
import gui.ctrl.browser.PacketData;
import gui.ctrl.browser.PacketHeader;
import gui.ctrl.browser.PacketList;
import gui.ctrl.list.FileList;
import gui.ctrl.list.NIFList;
import gui.model.SettingProperty;
import gui.model.browser.PacketProperty;
import gui.model.config.CaptureProperty;
import gui.model.config.FilterProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import job.AnalysisJob;
import job.OfflineJob;
import job.OnlineJob;
import job.SaveJob;
import nif.CNIF;
import util.ViewHandle;

import java.util.ArrayList;
import java.util.Objects;

public class IndexView implements View{
    SettingProperty settingProperty = new SettingProperty();

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

    public ArrayList<PacketProperty> packetPropertyArrayList = new ArrayList<>();

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

        ViewHandle.InitializeCaptureTop(this);
        ViewHandle.InitializeCaptureCenter(this);
        ViewHandle.InitializeCaptureBottom(this);

        status = captureStatusBarCtrl.statusLabel;

        status.setText("ready");
    }

    public void StartCapture(String opt) {
        // todo button 改为 item
        packetHeaderCtrl.setEdit(false);
        if (opt.equals("online")||opt.equals("offline")) {
            packetPropertyArrayList.clear();
            packetHeaderCtrl.getRoot().getChildren().clear();

            captureToolBarCtrl.getToolBar().getItems().get(13).setDisable(false);
            captureMenuBarCtrl.getAnalysisItem().setDisable(false);

            if (pcapFile != null) {
                for (int i = 0; i < 5; i++)
                    captureToolBarCtrl.getToolBar().getItems().get(i).setDisable(true);
                // TODO: 2020/5/4 不要忘记分割条也算一个 item
                for (int i = 6; i < 10; i++)
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
                    path = Objects.requireNonNullElseGet(pcapFile, () -> settingProperty.tempPcapFolder + "/tmp.pcapng");
                AnalysisJob analysisJob;
                if (this.cnif == null)
                    analysisJob = new AnalysisJob(path, null);
                else analysisJob = new AnalysisJob(path, this.cnif.handle);
                Thread thread = new Thread(analysisJob);
                thread.start();
                break;
            }
            case "apply": {
                status.setText("apply");
                String path;
                if (pcapFile!=null)
                    path = pcapFile;
                else path = settingProperty.tempPcapFolder + "/tmp.pcapng";
                OfflineJob offlineJob = new OfflineJob(this, path);
                Thread thread = new Thread(offlineJob);
                thread.start();
                break;
            }
            case "save": {
                String path;
                if (pcapFile!=null)
                    path = pcapFile;
                else path = settingProperty.tempPcapFolder + "/tmp.pcapng";
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
        // TODO: 2020/5/3 将界面类型切换涉及的界面元素更改都放到这里
        this.type = type;
        if (type.equals("capture")) {
            captureStatusBarCtrl.configButton.setDisable(true);
        }
        else {
            captureMenuBarCtrl.getAnalysisItem().setDisable(true);
            captureStatusBarCtrl.configButton.setDisable(false);
        }
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
        packetPropertyArrayList.clear();
        this.getPacketListCtrl().getPacketTable().getItems().clear();
        this.getPacketHeaderCtrl().getHeaderTreeTable().getRoot().getChildren().clear(); // clean tree
        this.getPacketDataCtrl().getDataArea().getItems().clear();
    }
}
