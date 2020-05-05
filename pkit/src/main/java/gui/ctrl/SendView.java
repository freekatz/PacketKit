package gui.ctrl;

import com.sun.javafx.collections.ObservableSequentialListWrapper;
import gui.ctrl.bar.*;
import gui.ctrl.browser.PacketData;
import gui.ctrl.browser.PacketHeader;
import gui.ctrl.browser.PacketList;
import gui.model.SettingProperty;
import gui.model.browser.PacketProperty;
import gui.model.config.SendProperty;
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
import org.pcap4j.core.PcapNetworkInterface;
import util.ViewHandle;
import util.job.*;
import util.nif.SNIF;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class SendView implements View{

    SettingProperty settingProperty = new SettingProperty();
    public ArrayList<PacketProperty> packetPropertyArrayList = new ArrayList<>();
    IndexView indexView;

    SendProperty sendProperty;
    PacketProperty packetProperty;

    private PacketList packetListCtrl;
    private PacketHeader packetHeaderCtrl;
    private PacketData packetDataCtrl;

    private SendMenuBar sendMenuBarCtrl;
    private SendToolBar sendToolBarCtrl;
    private SendStatusBar sendStatusBarCtrl;

    String type;

    private String exportPath;

    public Label status;

    String pcapFile;
    String nifName;
    String dstNifName;

    SNIF snif;

    @FXML
    BorderPane pane;

    @FXML
    VBox topBox;

    @FXML
    SplitPane browserPane;


    public SendView() {

    }

    public void initialize() {
        this.type = "send";

        ViewHandle.InitializeSendTop(this);
        ViewHandle.InitializeSendCenter(this);
        ViewHandle.InitializeSendBottom(this);

        status = sendStatusBarCtrl.statusLabel;

        status.setText("ready");
    }

    public void StartImport() {
        // 读取文件
        // 按钮，菜单变化

        clearBrowser();
        sendToolBarCtrl.removeButton.setDisable(false);
        sendToolBarCtrl.exportButton.setDisable(false);
        sendMenuBarCtrl.exportItem.setDisable(false);

        status.setText("read");
        OfflineJob offlineJob = new OfflineJob(this);
        Thread thread = new Thread(offlineJob);
        thread.start();
    }

    public void StartExport() {
        // save array list 的 json
        // 也可导入 json
        status.setText("export");
        ExportJob exportJob = new ExportJob(this, settingProperty.packetTemplatePath , exportPath);
        Thread thread = new Thread(exportJob);
        thread.start();
    }

    public void StartSend(String opt) {

        status.setText("send");

        sendToolBarCtrl.importButton.setDisable(true);
        sendToolBarCtrl.addButton.setDisable(true);
        sendToolBarCtrl.clearButton.setDisable(true);
        sendToolBarCtrl.stopButton.setDisable(false);
        sendToolBarCtrl.configButton.setDisable(true);
        sendToolBarCtrl.sendButton.setDisable(true);
        sendToolBarCtrl.sendAllButton.setDisable(true);
        sendToolBarCtrl.forwardButton.setDisable(true);
        sendToolBarCtrl.forwardAllButton.setDisable(true);
        sendToolBarCtrl.nifBox.setDisable(true);

        snif = new SNIF(nifName);
        SendJob sendJob = new SendJob(this, opt);
        Thread thread = new Thread(sendJob);
        thread.start();

        sendToolBarCtrl.InitializeButtonStatus();
        sendToolBarCtrl.forwardAllButton.setDisable(false);
        sendToolBarCtrl.forwardButton.setDisable(false);
        sendToolBarCtrl.sendAllButton.setDisable(false);
        sendToolBarCtrl.sendButton.setDisable(false);
        sendToolBarCtrl.removeButton.setDisable(false);
        sendToolBarCtrl.clearButton.setDisable(false);
    }

    public void StartForward(String opt) {

        status.setText("forward");

        sendToolBarCtrl.importButton.setDisable(true);
        sendToolBarCtrl.addButton.setDisable(true);
        sendToolBarCtrl.clearButton.setDisable(true);
        sendToolBarCtrl.stopButton.setDisable(false);
        sendToolBarCtrl.configButton.setDisable(true);
        sendToolBarCtrl.sendButton.setDisable(true);
        sendToolBarCtrl.sendAllButton.setDisable(true);
        sendToolBarCtrl.forwardButton.setDisable(true);
        sendToolBarCtrl.forwardAllButton.setDisable(true);
        sendToolBarCtrl.nifBox.setDisable(true);

        snif = new SNIF(nifName);
        dstNifName = sendToolBarCtrl.nifBox.getValue();
        ForwardJob forwardJob = new ForwardJob(this, opt, dstNifName);
        Thread thread = new Thread(forwardJob);
        thread.start();

        sendToolBarCtrl.InitializeButtonStatus();
        sendToolBarCtrl.forwardAllButton.setDisable(false);
        sendToolBarCtrl.forwardButton.setDisable(false);
        sendToolBarCtrl.sendAllButton.setDisable(false);
        sendToolBarCtrl.sendButton.setDisable(false);
        sendToolBarCtrl.removeButton.setDisable(false);
        sendToolBarCtrl.clearButton.setDisable(false);
    }

    public void Stop() {

        status.setText("stop");

        sendToolBarCtrl.InitializeButtonStatus();
        sendToolBarCtrl.removeButton.setDisable(false);
        sendToolBarCtrl.exportButton.setDisable(false);
        sendToolBarCtrl.sendButton.setDisable(false);
        sendToolBarCtrl.sendAllButton.setDisable(false);
        sendToolBarCtrl.forwardButton.setDisable(false);
        sendToolBarCtrl.forwardAllButton.setDisable(false);


        snif.handle.close();

    }

    public void clearBrowser() {

        status.setText("clear");

        sendToolBarCtrl.InitializeButtonStatus();

        packetPropertyArrayList.clear();
        this.getPacketListCtrl().getPacketTable().getItems().clear();
        this.getPacketHeaderCtrl().getHeaderTreeTable().getRoot().getChildren().clear(); // clean tree
//        this.getPacketDataCtrl().getDataArea().getItems().clear();
    }

    public SNIF getSnif() {
        return snif;
    }

    public void setSnif(SNIF snif) {
        this.snif = snif;
    }

    public SettingProperty getSettingProperty() {
        return settingProperty;
    }

    public void setSettingProperty(SettingProperty settingProperty) {
        this.settingProperty = settingProperty;
    }

    public String getDstNifName() {
        return dstNifName;
    }

    public void setDstNifName(String dstNifName) {
        this.dstNifName = dstNifName;
    }

    public IndexView getIndexView() {
        return indexView;
    }

    public void setIndexView(IndexView indexView) {
        this.indexView = indexView;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void close(Event event) {
        Stage stage = (Stage)((Button)(event).getSource()).getScene().getWindow();
        stage.close();
    }

    public String getPcapFile() {
        return pcapFile;
    }

    public void setPcapFile(String pcapFile) {
        this.pcapFile = pcapFile;
    }

    public String getNifName() {
        return nifName;
    }

    public void setNifName(String nifName) {
        this.nifName = nifName;
    }

    public PacketProperty getPacketProperty() {
        return packetProperty;
    }

    public void setPacketProperty(PacketProperty packetProperty) {
        this.packetProperty = packetProperty;
        sendToolBarCtrl.sendButton.setDisable(false);
        sendToolBarCtrl.sendAllButton.setDisable(false);
        sendToolBarCtrl.forwardButton.setDisable(false);
        sendToolBarCtrl.forwardAllButton.setDisable(false);
    }

    public SplitPane getBrowserPane() {
        return browserPane;
    }

    public void setBrowserPane(SplitPane browserPane) {
        this.browserPane = browserPane;
    }

    public VBox getTopBox() {
        return topBox;
    }

    public void setTopBox(VBox topBox) {
        this.topBox = topBox;
    }

    public BorderPane getPane() {
        return pane;
    }

    public void setPane(BorderPane pane) {
        this.pane = pane;
    }

    public SendStatusBar getSendStatusBarCtrl() {
        return sendStatusBarCtrl;
    }

    public void setSendStatusBarCtrl(SendStatusBar sendStatusBarCtrl) {
        this.sendStatusBarCtrl = sendStatusBarCtrl;
    }

    public SendToolBar getSendToolBarCtrl() {
        return sendToolBarCtrl;
    }

    public void setSendToolBarCtrl(SendToolBar sendToolBarCtrl) {
        this.sendToolBarCtrl = sendToolBarCtrl;
    }

    public SendMenuBar getSendMenuBarCtrl() {
        return sendMenuBarCtrl;
    }

    public void setSendMenuBarCtrl(SendMenuBar sendMenuBarCtrl) {
        this.sendMenuBarCtrl = sendMenuBarCtrl;
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

    public SendProperty getSendProperty() {
        return sendProperty;
    }

    public void setSendProperty(SendProperty sendProperty) {
        this.sendProperty = sendProperty;
    }

    public void setExportPath(String exportPath) {
        this.exportPath = exportPath;
    }
}
