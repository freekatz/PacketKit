package pkit.core.base.packet;

import org.pcap4j.core.PcapPacket;

public interface PPacket {

    void Initial();
    void Parse(PcapPacket pcapPacket);
    PcapPacket Craft();
    void Dump(String path);
}
