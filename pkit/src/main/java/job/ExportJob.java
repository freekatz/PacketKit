package job;

import gui.ctrl.IndexView;
import gui.ctrl.SendView;
import gui.ctrl.View;
import gui.model.ViewType;
import gui.model.browser.PacketProperty;
import nif.CNIF;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.packet.Packet;
import util.PacketHandle;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class ExportJob implements Runnable {

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
        if (view.getType().equals(ViewType.CaptureView))
            propertyArrayList = ((IndexView)view).packetPropertyArrayList;
        else if (view.getType().equals(ViewType.SendView))
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
