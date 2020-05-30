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
import gui.model.JobMode;
import gui.model.SettingProperty;
import gui.model.ViewType;
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

    //  主界面的两个列表视图的控制器: 离线文件历史列表及网卡列表
    private FileList fileListCtrl;
    private NIFList nifListCtrl;

    //  捕获界面的包浏览器三个组件的控制器: 包信息列表, 包首部及包字节
    private PacketList packetListCtrl;
    private PacketHeader packetHeaderCtrl;
    private PacketData packetDataCtrl;

    //  捕获界面的菜单栏, 工具栏, 过滤器栏及状态栏的控制器
    private CaptureMenuBar captureMenuBarCtrl;
    private CaptureToolBar captureToolBarCtrl;
    private FilterBar filterBarCtrl;
    private CaptureStatusBar captureStatusBarCtrl;

    //  捕获和过滤器配置
    private FilterProperty filterProperty;
    private CaptureProperty captureProperty;

    //  打开的离线文件, 只在离线模式下可用, 在线模式下为 null
    private String pcapFile;

    //  网卡的唯一标识, 只在在线模式下可用, 离线模式下为 null
    private String nifName;

    //  当前数据流的保存路径
    private String savePath;

    //  当前系统运行状态
    private Label status;

    //  当前界面的类型: index 及 capture
    private ViewType type;

    //  网卡对象, 根据 nifName 生成的是默认网卡, 其它需要用到网卡的作业使用临时对象, 故不在此声明
    private CNIF cnif;

    // 当前的数据流, 保存的是解析之后的数据包结构信息
    public ArrayList<PacketProperty> packetPropertyArrayList = new ArrayList<>();

    @FXML
    BorderPane pane;

    @FXML
    VBox topBox;

    VBox centerBox;
    SplitPane browserPane;

    public IndexView() {

        this.type = ViewType.IndexView;
    }

    public void initialize() {

        // 初始化 centre box
        centerBox = new VBox();
        browserPane = new SplitPane();
        browserPane.setOrientation(Orientation.VERTICAL);

        //  初始化 top box
        ViewHandle.InitializeCaptureTop(this);
        ViewHandle.InitializeCaptureCenter(this);
        ViewHandle.InitializeCaptureBottom(this);

        // 初始化 bottom box
        status = captureStatusBarCtrl.statusLabel;
        status.setText("ready");

    }

    @Override
    public void JobScheduler(JobMode jobMode) {
        packetHeaderCtrl.setEdit(false);
        if (jobMode.equals(JobMode.OnlineJob)||jobMode.equals(JobMode.OfflineJob)) {
            packetPropertyArrayList.clear();
            packetHeaderCtrl.getRoot().getChildren().clear();

            captureToolBarCtrl.getToolBar().getItems().get(13).setDisable(false);
            captureMenuBarCtrl.getAnalysisItem().setDisable(false);

            //  离线模式调度时更新工具栏及菜单栏按钮项目状态
            if (jobMode.equals(JobMode.OfflineJob)) {
                for (int i = 0; i < 5; i++)
                    captureToolBarCtrl.getToolBar().getItems().get(i).setDisable(true);
                for (int i = 6; i < 10; i++)
                    captureToolBarCtrl.getToolBar().getItems().get(i).setDisable(false);
                for (int i = 2; i < 4; i++)
                    captureMenuBarCtrl.getFileMenu().getItems().get(i).setDisable(false);
            }
            //  在线模式调度时更新工具栏及菜单栏按钮项目状态
            else {
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
        switch (jobMode) {
            case OnlineJob: {
                status.setText("capturing");
                cnif = new CNIF(nifName, captureProperty);
                OnlineJob onlineJob = new OnlineJob(this);
                Thread thread = new Thread(onlineJob);
                thread.start();
                break;
            }
            case OfflineJob: {
                status.setText("reading");
                OfflineJob offlineJob = new OfflineJob(this);
                Thread thread = new Thread(offlineJob);
                thread.start();
                break;
            }
            case AnalysisJob: {
                status.setText("analysis");

                //  获取分析模板文件的路径
                String path;
                if (fileListCtrl.getFileList().getSelectionModel().getSelectedItem()==null && type.equals(ViewType.IndexView))
                    return;
                else if (fileListCtrl.getFileList().getSelectionModel().getSelectedItem()!=null && type.equals(ViewType.IndexView))
                    path = fileListCtrl.getFileList().getSelectionModel().getSelectedItem().replaceAll("\\(.*?\\)", "");
                else
                    path = Objects.requireNonNullElseGet(pcapFile, () -> SettingProperty.tempPcapFolder + "/tmp.pcapng");

                AnalysisJob analysisJob;
                if (this.cnif == null)
                    analysisJob = new AnalysisJob(path, null);
                else analysisJob = new AnalysisJob(path, this.cnif.handle);

                Thread thread = new Thread(analysisJob);
                thread.start();
                break;
            }
            case ApplyJob: {
                status.setText("apply");
                String path;
                path = Objects.requireNonNullElseGet(pcapFile, () -> SettingProperty.tempPcapFolder + "/tmp.pcapng");
                OfflineJob offlineJob = new OfflineJob(this, path);
                Thread thread = new Thread(offlineJob);
                thread.start();
                break;
            }
            case SaveJob: {
                String path;
                path = Objects.requireNonNullElseGet(pcapFile, () -> SettingProperty.tempPcapFolder + "/tmp.pcapng");
                SaveJob saveJob = new SaveJob(this, path, savePath, filterProperty.getExpression());
                Thread thread = new Thread(saveJob);
                thread.start();
            }
            default:
                break;
        }
    }

    @Override
    public void JobStop() {
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
    public ViewType getType() {
        return type;
    }

    @Override
    public void setType(ViewType type) {
        this.type = type;
        if (type.equals(ViewType.CaptureView)) {
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
