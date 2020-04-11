package pkit.core.base.packet;

import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.MacAddress;

public class EthernetPacket implements Packet {

    private int length;
    private MacAddress destinationAddress;
    private MacAddress sourceAddress;
    private EtherType type;

    @Override
    public Packet Parse(PcapPacket pcapPacket) {
        return null;
    }

    @Override
    public PcapPacket Factory() {
        return null;
    }
}
