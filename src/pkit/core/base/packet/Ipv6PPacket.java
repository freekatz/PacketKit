package pkit.core.base.packet;

import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.IpV6Packet;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;

import java.net.Inet6Address;

public class Ipv6PPacket implements PPacket {
    private String name;
    private int length;
    private String version;
    private byte trafficClass;
    private int flowLabel;
    private short payloadLength;
    private String nextHeader;
    private byte hopLimit;
    private String srcAddr;
    private String dstAddr;

    @Override
    public void Initial() {
        this.name = "ipv6";
    }

    @Override
    public void Parse(PcapPacket pcapPacket) {
        IpV6Packet.IpV6Header header = pcapPacket.get(IpV6Packet.class).getHeader();
        header.getTrafficClass().value();
        this.length = header.length();
        this.version = header.getVersion().valueAsString();
        this.trafficClass = header.getTrafficClass().value();
        this.flowLabel = header.getFlowLabel().value();
        this.payloadLength = header.getPayloadLength();
        this.nextHeader = header.getNextHeader().valueAsString();
        this.hopLimit = header.getHopLimit();
        this.srcAddr = header.getSrcAddr().getHostAddress();
        this.dstAddr = header.getDstAddr().getHostAddress();

    }

    @Override
    public PcapPacket Craft() {
        return null;
    }

    @Override
    public void Dump(String path) {

    }

    public String getDstAddr() {
        return dstAddr;
    }

    public void setDstAddr(String dstAddr) {
        this.dstAddr = dstAddr;
    }

    public String getSrcAddr() {
        return srcAddr;
    }

    public void setSrcAddr(String srcAddr) {
        this.srcAddr = srcAddr;
    }

    public byte getHopLimit() {
        return hopLimit;
    }

    public void setHopLimit(byte hopLimit) {
        this.hopLimit = hopLimit;
    }

    public String getNextHeader() {
        return nextHeader;
    }

    public void setNextHeader(String nextHeader) {
        this.nextHeader = nextHeader;
    }

    public short getPayloadLength() {
        return payloadLength;
    }

    public void setPayloadLength(short payloadLength) {
        this.payloadLength = payloadLength;
    }

    public int getFlowLabel() {
        return flowLabel;
    }

    public void setFlowLabel(int flowLabel) {
        this.flowLabel = flowLabel;
    }

    public byte getTrafficClass() {
        return trafficClass;
    }

    public void setTrafficClass(byte trafficClass) {
        this.trafficClass = trafficClass;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
