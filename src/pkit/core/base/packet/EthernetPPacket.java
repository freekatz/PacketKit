package pkit.core.base.packet;

import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.MacAddress;

public class EthernetPPacket implements PPacket {
    private String name;
    private int length;
    private String  dstAddr;
    private String  srcAddr;
    private String type;

    @Override
    public void Initial() {
        this.name = "ethernet";
    }

    @Override
    public void Parse(PcapPacket pcapPacket) {
        EthernetPacket.EthernetHeader header = pcapPacket.get(EthernetPacket.class).getHeader();
        this.length = header.length();
        this.dstAddr = header.getDstAddr().toString();
        this.srcAddr = header.getSrcAddr().toString();
        this.type = header.getType().valueAsString();
    }

    @Override
    public PcapPacket Craft() {
        return null;
    }

    @Override
    public void Dump(String path) {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSrcAddr() {
        return srcAddr;
    }

    public void setSrcAddr(String  srcAddr) {
        this.srcAddr = srcAddr;
    }

    public String getDstAddr() {
        return dstAddr;
    }

    public void setDstAddr(String  dstAddr) {
        this.dstAddr = dstAddr;
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
