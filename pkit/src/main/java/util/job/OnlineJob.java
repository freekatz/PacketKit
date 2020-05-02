package util.job;

import gui.ctrl.IndexView;
import gui.model.CaptureProperty;
import gui.model.Property;
import gui.model.browser.PacketInfoProperty;
import gui.model.browser.PacketProperty;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapPacket;
import util.PacketHandle;
import util.nif.CNIF;

import java.io.EOFException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

public class OnlineJob extends Task<PacketProperty> {

    CNIF cnif;

    IndexView indexView;
    CaptureProperty captureProperty;
    TableView<Property> packetTable;
    TreeTableView<String> headerTreeTable;
    ListView<String> indexList;
    TextArea hexArea;
    TextArea txtArea;

    public OnlineJob(IndexView indexView) {
        this.indexView = indexView;

        cnif = indexView.getCnif();
        captureProperty = indexView.getCaptureProperty();

        packetTable = indexView.getPacketListCtrl().getPacketTable();
        headerTreeTable = indexView.getPacketHeaderCtrl().getHeaderTreeTable();
        indexList = indexView.getPacketDataCtrl().getIndexList();
        hexArea = indexView.getPacketDataCtrl().getHexArea();
        txtArea = indexView.getPacketDataCtrl().getTxtArea();
    }

    @Override
    protected void updateValue(PacketProperty packetProperty) {
        super.updateValue(packetProperty);
        if (packetProperty!=null) {
            // add signal in index view to control the refresh buffer
            packetTable.getItems().add(packetProperty.getInfo());
            // header and data
        }

    }

    @Override
    public PacketProperty call() {

        boolean loop = false;
        int num = 0;
        if (captureProperty.getCount() < 0)
            loop = true;
        while (loop || num < captureProperty.getCount()) {
            try {
                System.out.println(indexView.getFilterProperty().getExpression());
                BpfProgram bpfProgram = cnif.handle.compileFilter(indexView.getFilterProperty().getExpression(), BpfProgram.BpfCompileMode.OPTIMIZE, (Inet4Address) InetAddress.getByName("255.255.255.255"));
                PcapPacket packet = cnif.handle.getNextPacketEx();
                cnif.dumper.dump(packet);
                num++;
                if (bpfProgram.applyFilter(packet)) {
                    // 对符合过滤器的包进行处理
                    PacketInfoProperty packetInfoProperty = PacketHandle.InfoPipeline(packet);
                    packetInfoProperty.setNo(num);
                    PacketProperty packetProperty = new PacketProperty();
                    packetProperty.setInfo(packetInfoProperty);
                    this.updateValue(packetProperty);
                }
            } catch (EOFException | PcapNativeException | TimeoutException | UnknownHostException ignored) {
            } catch (NotOpenException e) {
                break;
            }
        }

        cnif.dumper.close();
        cnif.handle.close();
        indexView.getCaptureToolBarCtrl().getToolBar().getItems().get(0).setDisable(false);
        indexView.getCaptureToolBarCtrl().getToolBar().getItems().get(1).setDisable(true);
        indexView.getCaptureToolBarCtrl().getToolBar().getItems().get(2).setDisable(false);
        indexView.getCaptureToolBarCtrl().getToolBar().getItems().get(5).setDisable(false);
        indexView.getCaptureStatusBarCtrl().configButton.setDisable(false);
        return null;
    }


}
