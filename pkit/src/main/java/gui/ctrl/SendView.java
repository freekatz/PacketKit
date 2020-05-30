package gui.ctrl;

import gui.ctrl.bar.SendMenuBar;
import gui.ctrl.bar.SendStatusBar;
import gui.ctrl.bar.SendToolBar;
import gui.ctrl.browser.PacketData;
import gui.ctrl.browser.PacketHeader;
import gui.ctrl.browser.PacketList;
import gui.model.JobMode;
import gui.model.SettingProperty;
import gui.model.ViewType;
import gui.model.browser.PacketProperty;
import gui.model.config.SendProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import job.*;
import nif.CNIF;
import nif.SNIF;
import util.ViewHandle;

import java.util.ArrayList;
import java.util.Objects;

public class SendView implements View{
    IndexView indexView;

    private PacketList packetListCtrl;
    private PacketHeader packetHeaderCtrl;
    private PacketData packetDataCtrl;

    private SendMenuBar sendMenuBarCtrl;
    private SendToolBar sendToolBarCtrl;
    private SendStatusBar sendStatusBarCtrl;

    SendProperty sendProperty;
    PacketProperty packetProperty;


    String pcapFile;
    String nifName;
    String dstNifName;

    private String exportPath;
    public Label status;

    ViewType type;

    SNIF snif;
    public ArrayList<PacketProperty> packetPropertyArrayList = new ArrayList<>();

    @FXML
    BorderPane pane;

    @FXML
    VBox topBox;

    @FXML
    SplitPane browserPane;


    public SendView() {
        this.type = ViewType.SendView;
    }

    public void initialize() {

        ViewHandle.InitializeSendTop(this);
        ViewHandle.InitializeSendCenter(this);
        ViewHandle.InitializeSendBottom(this);

        status = sendStatusBarCtrl.statusLabel;

        status.setText("ready");
    }

    @Override
    public ViewType getType() {
        return type;
    }

    @Override
    public void setType(ViewType type) {
        this.type = type;
    }

    @Override
    public void JobScheduler(JobMode jobMode) {
        if (jobMode.equals(JobMode.SendOneJob)||jobMode.equals(JobMode.ForwardOneJob)||jobMode.equals(JobMode.SendAllJob)||jobMode.equals(JobMode.ForwardAllJob)) {
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
        }

        // capture ctrl
        switch (jobMode) {
            case SendOneJob: {
                status.setText("send one");

                snif = new SNIF(nifName);
                SendJob sendJob = new SendJob(this, "one");
                Thread thread = new Thread(sendJob);
                thread.start();

                sendToolBarCtrl.InitializeButtonStatus();
                sendToolBarCtrl.forwardAllButton.setDisable(false);
                sendToolBarCtrl.forwardButton.setDisable(false);
                sendToolBarCtrl.sendAllButton.setDisable(false);
                sendToolBarCtrl.sendButton.setDisable(false);
                sendToolBarCtrl.removeButton.setDisable(false);
                sendToolBarCtrl.clearButton.setDisable(false);

                break;
            }
            case SendAllJob: {
                status.setText("send all");

                snif = new SNIF(nifName);
                SendJob sendJob = new SendJob(this, "all");
                Thread thread = new Thread(sendJob);
                thread.start();

                sendToolBarCtrl.InitializeButtonStatus();
                sendToolBarCtrl.forwardAllButton.setDisable(false);
                sendToolBarCtrl.forwardButton.setDisable(false);
                sendToolBarCtrl.sendAllButton.setDisable(false);
                sendToolBarCtrl.sendButton.setDisable(false);
                sendToolBarCtrl.removeButton.setDisable(false);
                sendToolBarCtrl.clearButton.setDisable(false);

                break;
            }
            case ForwardOneJob: {
                status.setText("forward one");

                snif = new SNIF(nifName);
                dstNifName = sendToolBarCtrl.nifBox.getValue();
                ForwardJob forwardJob = new ForwardJob(this, "one", dstNifName);
                Thread thread = new Thread(forwardJob);
                thread.start();

                sendToolBarCtrl.InitializeButtonStatus();
                sendToolBarCtrl.forwardAllButton.setDisable(false);
                sendToolBarCtrl.forwardButton.setDisable(false);
                sendToolBarCtrl.sendAllButton.setDisable(false);
                sendToolBarCtrl.sendButton.setDisable(false);
                sendToolBarCtrl.removeButton.setDisable(false);
                sendToolBarCtrl.clearButton.setDisable(false);

                break;
            }
            case ForwardAllJob: {
                status.setText("forward all");

                snif = new SNIF(nifName);
                dstNifName = sendToolBarCtrl.nifBox.getValue();
                ForwardJob forwardJob = new ForwardJob(this, "all", dstNifName);
                Thread thread = new Thread(forwardJob);
                thread.start();

                sendToolBarCtrl.InitializeButtonStatus();
                sendToolBarCtrl.forwardAllButton.setDisable(false);
                sendToolBarCtrl.forwardButton.setDisable(false);
                sendToolBarCtrl.sendAllButton.setDisable(false);
                sendToolBarCtrl.sendButton.setDisable(false);
                sendToolBarCtrl.removeButton.setDisable(false);
                sendToolBarCtrl.clearButton.setDisable(false);

                break;
            }
            case ImportJob: {
                status.setText("import");

                clearBrowser();
                sendToolBarCtrl.removeButton.setDisable(false);
                sendToolBarCtrl.exportButton.setDisable(false);
                sendMenuBarCtrl.exportItem.setDisable(false);

                OfflineJob offlineJob = new OfflineJob(this);
                Thread thread = new Thread(offlineJob);
                thread.start();
                break;
            }
            case ExportJob: {
                status.setText("export");

                ExportJob exportJob = new ExportJob(this, SettingProperty.packetTemplatePath , exportPath);
                Thread thread = new Thread(exportJob);
                thread.start();
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void JobStop() {

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

    @Override
    public void close(Event event) {
        Stage stage = (Stage)((Button)(event).getSource()).getScene().getWindow();
        stage.close();
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
        sendToolBarCtrl.clearButton.setDisable(false);
        sendToolBarCtrl.removeButton.setDisable(false);
        sendToolBarCtrl.exportButton.setDisable(false);
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
