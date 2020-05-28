package job;

import gui.ctrl.SendView;
import gui.model.config.SendProperty;
import nif.SNIF;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.MacAddress;
import util.PacketHandle;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class ForwardJob implements Runnable {

    private final SNIF snif;
    private final SendProperty sendProperty;
    private SendView sendView;
    private String opt;
    Packet packet;
    ArrayList<Packet> packetArrayList;

    int retry;

    public ForwardJob(SendView sendView, String opt, String target) {
        this.sendView = sendView;
        this.opt = opt;
        this.snif = sendView.getSnif();
        this.sendProperty = sendView.getSendProperty();

        try {
            this.snif.load();
            PcapNetworkInterface nif = Pcaps.getDevByName(target);
            if (opt.equals("one")) {
                EthernetPacket.Builder builder = ((EthernetPacket)PacketHandle.Restore(sendView.getPacketProperty())).getBuilder();
                builder.dstAddr((MacAddress) nif.getLinkLayerAddresses().get(0));
                this.packet = builder.build();
            }
            else {
                packetArrayList = new ArrayList<>(sendView.packetPropertyArrayList.size());
                sendView.packetPropertyArrayList.forEach(pp -> {
                    try {
                        EthernetPacket.Builder builder = ((EthernetPacket)PacketHandle.Restore(pp)).getBuilder();
                        builder.dstAddr((MacAddress) nif.getLinkLayerAddresses().get(0));
                        Packet packet = builder.build();
                        packetArrayList.add(packet);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (UnknownHostException | PcapNativeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        retry = 0;
        System.out.println(packet);
        if (opt.equals("one")) {
            SendOne(packet);
        } else SendMulti();

    }

    private void SendMulti() {
        packetArrayList.forEach(this::SendOne);
    }

    private void SendOne(Packet packet) {
        try {
            for (int i=0; i<sendProperty.getCount(); ++i) {
                Thread.sleep(sendProperty.getTimeout());
                snif.handle.sendPacket(packet);
            }
        } catch (NotOpenException | PcapNativeException | InterruptedException ignored) {
            while (retry<sendProperty.getRetry())
                SendOne(packet);
        }
        retry++;
    }
}
