package util.job;

import gui.ctrl.IndexView;
import gui.ctrl.SendView;
import gui.ctrl.View;
import gui.model.browser.PacketInfoProperty;
import gui.model.browser.PacketProperty;
import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.Packet;
import util.PacketHandle;
import util.nif.CNIF;

import java.io.EOFException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class ExportJob implements Runnable{

    CNIF cnif;

    View view;
    String pcapFile;
    String exportPath;

    public ExportJob(View view, String path, String exportPath) {
        this.view = view;
        this.pcapFile = path;
        this.exportPath = exportPath;

        cnif = new CNIF(path);

        try {
            cnif.dumper = cnif.handle.dumpOpen(exportPath);
        } catch (PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        ArrayList<PacketProperty> propertyArrayList;
        if (view.getType().equals("capture"))
            propertyArrayList = ((IndexView)view).packetPropertyArrayList;
        else if (view.getType().equals("send"))
            propertyArrayList = ((SendView) view).packetPropertyArrayList;
        else return;

        propertyArrayList.forEach(p->{
            try {
                Packet packet = PacketHandle.Restore(p);
                try {
                    cnif.dumper.dump(packet);
                } catch (NotOpenException e) {
                    e.printStackTrace();
                }
            } catch (UnknownHostException ignored) {
            }
        });
    }
}
