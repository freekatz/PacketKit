package util;

import gui.ctrl.IndexView;
import gui.model.Property;
import gui.model.browser.PacketInfoProperty;
import gui.model.browser.PacketProperty;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapPacket;
import util.nif.CNIF;

import java.io.EOFException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

public class OfflineJob extends Task<PacketProperty> {

    CNIF cnif;

    IndexView indexView;
    String filterExpression;
    TableView<Property> packetTable;
    TreeView<Property> headerTree;
    ListView<String> indexList;
    TextArea hexArea;
    TextArea txtArea;


    public OfflineJob(IndexView indexView) {
        // capture job
        this.indexView = indexView;

        cnif = new CNIF(indexView.getPcapFile());

        packetTable = indexView.getPacketListCtrl().getPacketTable();
        headerTree = indexView.getPacketHeaderCtrl().getHeaderTree();
        indexList = indexView.getPacketDataCtrl().getIndexList();
        hexArea = indexView.getPacketDataCtrl().getHexArea();
        txtArea = indexView.getPacketDataCtrl().getTxtArea();

    }

    public OfflineJob(IndexView indexView, String pcapFile) {
        // apply job
        this.indexView = indexView;

        cnif = new CNIF(pcapFile);

        packetTable = indexView.getPacketListCtrl().getPacketTable();
        headerTree = indexView.getPacketHeaderCtrl().getHeaderTree();
        indexList = indexView.getPacketDataCtrl().getIndexList();
        hexArea = indexView.getPacketDataCtrl().getHexArea();
        txtArea = indexView.getPacketDataCtrl().getTxtArea();
    }


    @Override
    protected void updateValue(PacketProperty packetProperty) {
        super.updateValue(packetProperty);
        if (packetProperty!=null) {
            packetTable.getItems().add(packetProperty.getInfo());
            // header and data
        }

    }

    @Override
    public PacketProperty call() {

        int num = 0;
        while (true) {
            try {
                BpfProgram bpfProgram = cnif.handle.compileFilter(indexView.getFilterProperty().getExpression(), BpfProgram.BpfCompileMode.OPTIMIZE, (Inet4Address) InetAddress.getByName("255.255.255.255"));
                PcapPacket packet = cnif.handle.getNextPacketEx();
                num++;
                if (bpfProgram.applyFilter(packet)) {
                    // 对符合过滤器的包进行处理
                    PacketInfoProperty packetInfoProperty = PacketHandle.InfoPipeline(packet);
                    packetInfoProperty.setNo(num);
                    PacketProperty packetProperty = new PacketProperty();
                    packetProperty.setInfo(packetInfoProperty);
                    this.updateValue(packetProperty);
                }
            } catch (EOFException e) {
                break;
            } catch (TimeoutException | PcapNativeException | NotOpenException | UnknownHostException ignored) {
            }
        }

        return null;
    }
}
