package pkit.core.base.packet;

import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;
import org.pcap4j.packet.namednumber.UdpPort;

public class UdpPPacket implements PPacket {
    private String name;
    private int length;
    private String srcPort;
    private String dstPort;
    private short totalLength;
    private short checksum;

    @Override
    public void Initial() {
        this.name = "udp";
    }

    @Override
    public void Parse(PcapPacket pcapPacket) {
        UdpPacket.UdpHeader header = pcapPacket.get(UdpPacket.class).getHeader();
        this.length = header.length();
        this.srcPort = header.getSrcPort().valueAsString();
        this.dstPort = header.getDstPort().valueAsString();
        this.totalLength = header.getLength();
        this.checksum = header.getChecksum();
    }

    @Override
    public PcapPacket Craft() {
        return null;
    }

    @Override
    public void Dump(String path) {

    }

    public short getChecksum() {
        return checksum;
    }

    public void setChecksum(short checksum) {
        this.checksum = checksum;
    }

    public short getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(short totalLength) {
        this.totalLength = totalLength;
    }

    public String getDstPort() {
        return dstPort;
    }

    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }

    public String getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(String srcPort) {
        this.srcPort = srcPort;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
