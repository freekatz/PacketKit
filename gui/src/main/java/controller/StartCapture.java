package controller;

import config.CaptureFilterConfig;
import config.CaptureNetworkInterfaceConfig;
import group.CapturePacketGroup;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import nif.CaptureNetworkInterface;
import nif.NetworkInterfaceMode;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import util.DirHandle;
import util.NIFHandle;
import util.TableHandle;

import java.io.IOException;
import java.util.HashMap;

public class StartCapture {
    private Manager manager;
    private CaptureNetworkInterfaceConfig networkInterfaceConfig;
    private CaptureFilterConfig filterConfig;
    private CaptureNetworkInterface networkInterface;
    private CapturePacketGroup packetGroup;

    @FXML
    CheckBox offline;

    @FXML
    ComboBox<Label> nif;

    @FXML
    TextField pcap;

    @FXML
    TextField count;

    @FXML
    TextField length;

    @FXML
    TextField timeout;

    @FXML
    TextField buffer;

    @FXML
    CheckBox promiscuous;

    @FXML
    CheckBox rfmon;

    @FXML
    CheckBox precision;

    @FXML
    CheckBox immediate;

    @FXML
    TextField filter;

    @FXML
    ComboBox<Label> direction;

    @FXML
    Button applyButton;

    @FXML
    Button saveButton;

    @FXML
    Button startButton;

    @FXML
    TableView<HashMap<String, String>> statTable;

    // TODO: 2020/4/19 Packet info model
    @FXML
    TableView<String> packetTable;

    public StartCapture() {}

    public void initialize() {
        NIFHandle.InitializeNIF(this.nif);
        DirHandle.InitializeDirection(this.direction);

    }

    public void InitializeConfig() {
        if (this.networkInterfaceConfig!=null) {
            count.setText(String.valueOf(this.networkInterfaceConfig.getCount()));
            length.setText(String.valueOf(this.networkInterfaceConfig.getSnapshotLength()));
            timeout.setText(String.valueOf(this.networkInterfaceConfig.getTimeoutMillis()));
            buffer.setText(String.valueOf(this.networkInterfaceConfig.getBufferSize()));
            promiscuous.setSelected(this.networkInterfaceConfig.getPromiscuousMode() == PcapNetworkInterface.PromiscuousMode.PROMISCUOUS);
            rfmon.setSelected(this.networkInterfaceConfig.getRfmonMode() == NetworkInterfaceMode.RfmonMode.RfmonMode);
            precision.setSelected(this.networkInterfaceConfig.getTimestampPrecision() == PcapHandle.TimestampPrecision.NANO);
            immediate.setSelected(this.networkInterfaceConfig.getImmediateMode() == NetworkInterfaceMode.ImmediateMode.ImmediateMode);
        }
        if (this.filterConfig!=null) {
            filter.setText(this.filterConfig.getFilter());
            String dir = "allLabel";
            switch (this.filterConfig.getDirection()) {
                case INOUT:
                    dir = "allLabel";
                    break;
                case IN:
                    dir = "inLabel";
                    break;
                case OUT:
                    dir = "outLabel";
                    break;
            }

            String finalDir = dir;
            this.direction.getItems().forEach(l -> {
                System.out.println(l.getId());
                if (l.getId().equals(finalDir))
                    direction.setValue(l);
            });
        }

    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public void setNetworkInterfaceConfig(CaptureNetworkInterfaceConfig networkInterfaceConfig) {
        this.networkInterfaceConfig = networkInterfaceConfig;
        this.filterConfig = networkInterfaceConfig.getFilterConfig();
    }

    @FXML
    private void OfflineChecked() {

    }

}
