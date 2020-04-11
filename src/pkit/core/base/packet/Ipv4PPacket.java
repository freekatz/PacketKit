package pkit.core.base.packet;

import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;

public class Ipv4PPacket implements PPacket {
    private String name;
    private int length;
    private String version;
    private byte ihl;
    private byte tos;
    private short totalLength;
    private short identification;
    private boolean reservedFlag;
    private boolean dontFragmentFlag;
    private boolean moreFragmentFlag;
    private short fragmentOffset;
    private byte ttl;
    private String protocol;
    private short headerChecksum;
    private String srcAddr;
    private String dstAddr;
    private ArrayList<String> options;
    private byte[] padding;

    @Override
    public void Initial() {
        this.name = "ipv4";
    }

    @Override
    public void Parse(PcapPacket pcapPacket) {
        IpV4Packet.IpV4Header header = pcapPacket.get(IpV4Packet.class).getHeader();
        this.length = header.length();
        this.version = header.getVersion().valueAsString();
        this.ihl = header.getIhl();
        this.tos = header.getTos().value();
        this.totalLength = header.getTotalLength();
        this.identification = header.getIdentification();
        this.reservedFlag = header.getReservedFlag();
        this.dontFragmentFlag = header.getDontFragmentFlag();
        this.moreFragmentFlag = header.getMoreFragmentFlag();
        this.fragmentOffset = header.getFragmentOffset();
        this.ttl = header.getTtl();
        this.protocol = header.getProtocol().valueAsString();
        this.headerChecksum = header.getHeaderChecksum();
        this.srcAddr = header.getSrcAddr().getHostAddress();
        this.dstAddr = header.getDstAddr().getHostAddress();
        ArrayList<String> options = new ArrayList<>();
        header.getOptions().forEach(opt->{
            options.add(opt.getType().valueAsString());
        });
        this.options = options;
        this.padding = header.getPadding();
    }

    @Override
    public PcapPacket Craft() {
        return null;
    }

    @Override
    public void Dump(String path) {

    }

    public byte[] getPadding() {
        return padding;
    }

    public void setPadding(byte[] padding) {
        this.padding = padding;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
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

    public short getHeaderChecksum() {
        return headerChecksum;
    }

    public void setHeaderChecksum(short headerChecksum) {
        this.headerChecksum = headerChecksum;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public byte getTtl() {
        return ttl;
    }

    public void setTtl(byte ttl) {
        this.ttl = ttl;
    }

    public short getFragmentOffset() {
        return fragmentOffset;
    }

    public void setFragmentOffset(short fragmentOffset) {
        this.fragmentOffset = fragmentOffset;
    }

    public boolean isMoreFragmentFlag() {
        return moreFragmentFlag;
    }

    public void setMoreFragmentFlag(boolean moreFragmentFlag) {
        this.moreFragmentFlag = moreFragmentFlag;
    }

    public boolean isDontFragmentFlag() {
        return dontFragmentFlag;
    }

    public void setDontFragmentFlag(boolean dontFragmentFlag) {
        this.dontFragmentFlag = dontFragmentFlag;
    }

    public boolean isReservedFlag() {
        return reservedFlag;
    }

    public void setReservedFlag(boolean reservedFlag) {
        this.reservedFlag = reservedFlag;
    }

    public short getIdentification() {
        return identification;
    }

    public void setIdentification(short identification) {
        this.identification = identification;
    }

    public short getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(short totalLength) {
        this.totalLength = totalLength;
    }

    public byte getTos() {
        return tos;
    }

    public void setTos(byte tos) {
        this.tos = tos;
    }

    public byte getIhl() {
        return ihl;
    }

    public void setIhl(byte ihl) {
        this.ihl = ihl;
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
