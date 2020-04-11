package pkit.core.base.packet;

import org.pcap4j.core.PcapPacket;

public interface Packet {

    Packet Parse(PcapPacket pcapPacket);
    PcapPacket Factory();
}
