package util.job;

import gui.ctrl.IndexView;
import gui.ctrl.SendView;
import gui.model.Property;
import gui.model.browser.FieldProperty;
import gui.model.browser.PacketInfoProperty;
import gui.model.browser.PacketProperty;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapPacket;
import util.PacketHandle;
import util.ViewHandle;
import util.nif.CNIF;

import java.io.EOFException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

public class OfflineJob extends Task<PacketProperty> {

    CNIF cnif;

    IndexView indexView;
    SendView sendView;
    TableView<Property> packetTable;


    public OfflineJob(IndexView indexView) {
        // capture job
        this.indexView = indexView;

        cnif = new CNIF(indexView.getPcapFile());

        packetTable = indexView.getPacketListCtrl().getPacketTable();

    }

    public OfflineJob(SendView sendView) {
        // capture job
        this.sendView = sendView;

        cnif = new CNIF(sendView.getPcapFile());

        packetTable = sendView.getPacketListCtrl().getPacketTable();

    }

    public OfflineJob(IndexView indexView, String pcapFile) {
        // apply job
        this.indexView = indexView;

        cnif = new CNIF(pcapFile);

        packetTable = indexView.getPacketListCtrl().getPacketTable();
    }

    public OfflineJob(SendView sendView, String pcapFile) {
        // apply job
        this.sendView = sendView;

        cnif = new CNIF(pcapFile);

        packetTable = sendView.getPacketListCtrl().getPacketTable();
    }


    @Override
    protected void updateValue(PacketProperty packetProperty) {
        super.updateValue(packetProperty);
        if (packetProperty!=null) {
            if (indexView!=null) {
                indexView.packetPropertyArrayList.add(packetProperty);
                if (indexView.packetPropertyArrayList.size()==1) {
                    // 第一个包初始化
                    BrowserJob job = new BrowserJob(packetProperty, indexView);
                    Thread thread = new Thread(job);
                    thread.start();
                }

            } else if (sendView!=null){
                sendView.packetPropertyArrayList.add(packetProperty);
                if (sendView.packetPropertyArrayList.size()==1) {
                    // 第一个包初始化
                    BrowserJob job = new BrowserJob(packetProperty, sendView);
                    Thread thread = new Thread(job);
                    thread.start();
                }
            }
            packetTable.getItems().add(packetProperty.getInfo());

        }

    }

    @Override
    public PacketProperty call() {

        int num = 0;
        while (true) {
            try {
                BpfProgram bpfProgram;
                if (indexView==null)  bpfProgram = cnif.handle.compileFilter("", BpfProgram.BpfCompileMode.OPTIMIZE, (Inet4Address) InetAddress.getByName("255.255.255.255"));
                else bpfProgram = cnif.handle.compileFilter(indexView.getFilterProperty().getExpression(), BpfProgram.BpfCompileMode.OPTIMIZE, (Inet4Address) InetAddress.getByName("255.255.255.255"));
                PcapPacket packet = cnif.handle.getNextPacketEx();
                num++;
                if (bpfProgram.applyFilter(packet)) {
                    // 对符合过滤器的包进行处理
                    PacketProperty packetProperty = PacketHandle.Pipeline(packet);
                    PacketInfoProperty packetInfoProperty = packetProperty.getInfo();
                    packetInfoProperty.setNo(num);
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
