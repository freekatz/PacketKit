package job;

import gui.ctrl.IndexView;
import gui.model.Property;
import gui.model.browser.PacketInfoProperty;
import gui.model.browser.PacketProperty;
import gui.model.config.CaptureProperty;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;
import nif.CNIF;
import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapPacket;
import util.PacketHandle;

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

    public OnlineJob(IndexView indexView) {
        this.indexView = indexView;

        cnif = indexView.getCnif();
        captureProperty = indexView.getCaptureProperty();

        packetTable = indexView.getPacketListCtrl().getPacketTable();
    }

    @Override
    protected void updateValue(PacketProperty packetProperty) {
        super.updateValue(packetProperty);
        if (packetProperty!=null) {
            indexView.packetPropertyArrayList.add(packetProperty);
            if (indexView.packetPropertyArrayList.size()==1) {
                BrowserJob job = new BrowserJob(packetProperty, indexView);
                Thread thread = new Thread(job);
                thread.start();
            }
            packetTable.getItems().add(packetProperty.getInfo());
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
                BpfProgram bpfProgram = cnif.handle.compileFilter(indexView.getFilterProperty().getExpression(), BpfProgram.BpfCompileMode.OPTIMIZE, (Inet4Address) InetAddress.getByName("255.255.255.255"));
                PcapPacket packet = cnif.handle.getNextPacketEx();
                cnif.dumper.dump(packet);
                num++;
                if (bpfProgram.applyFilter(packet)) {
                    // 对符合过滤器的包进行处理
                    PacketProperty packetProperty = PacketHandle.Pipeline(packet);
                    PacketInfoProperty packetInfoProperty = packetProperty.getInfo();
                    packetInfoProperty.setNo(num);
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
        indexView.getCaptureToolBarCtrl().getToolBar().getItems().get(4).setDisable(false);
        indexView.getCaptureToolBarCtrl().getToolBar().getItems().get(5).setDisable(false);
        indexView.getCaptureStatusBarCtrl().configButton.setDisable(false);
        return null;
    }


}
