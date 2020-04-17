package packet;

import org.pcap4j.core.*;
import org.pcap4j.packet.*;
import org.pcap4j.packet.factory.PacketFactories;
import org.pcap4j.packet.namednumber.DataLinkType;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;
import org.pcap4j.packet.namednumber.NotApplicable;
import util.JsonHandle;

import java.io.File;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.UnknownHostException;

public class Ipv6PPacket implements PPacket{
    private IpV6Packet.Builder builder;
    private PcapHandle pcapHandle;
    private JsonHandle jsonHandle;
    private PcapDumper dumper;
    private String packetPath;  // todo 设置默认路径
    private String configPath;

    private String name;
    private int length;
    private byte version;
    private byte trafficClass;
    private int flowLabel;
    private short payloadLength;
    private byte nextHeader;
    private byte hopLimit;
    private String srcAddr;
    private String dstAddr;

    @Override
    public Packet.Builder builder() {
        return this.builder;
    }


    @Override
    public String name() {
        return this.name;
    }

    @Override
    public void Initial() {
        this.builder = new IpV6Packet.Builder();
        this.packetPath = "tmp/";
        this.configPath = "tmp/";

        this.name = "ipv6";
        this.length = 40;
        this.version = IpVersion.IPV6.value();
        this.trafficClass = 0;
        this.flowLabel = 0;
        this.payloadLength = 0;
        this.nextHeader = IpNumber.TCP.value();
        this.hopLimit = (byte) 255;
        this.srcAddr = "::1";
        this.dstAddr = "ff::11";
    }

    @Override
    public void Parse(PcapPacket pcapPacket) {
        IpV6Packet.IpV6Header header = pcapPacket.get(IpV6Packet.class).getHeader();
        header.getTrafficClass().value();
        this.length = header.length();
        this.version = header.getVersion().value();
        this.trafficClass = header.getTrafficClass().value();
        this.flowLabel = header.getFlowLabel().value();
        this.payloadLength = header.getPayloadLength();
        this.nextHeader = header.getNextHeader().value();
        this.hopLimit = header.getHopLimit();
        this.srcAddr = header.getSrcAddr().getHostAddress();
        this.dstAddr = header.getDstAddr().getHostAddress();

    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public void CraftBuilder() {
        this.builder.version(IpVersion.getInstance(this.version))
                .trafficClass(IpV6SimpleTrafficClass.newInstance(this.trafficClass))
                .flowLabel(IpV6SimpleFlowLabel.newInstance(this.flowLabel))
                .payloadLength(this.payloadLength)
                .nextHeader(IpNumber.getInstance(this.nextHeader))
                .hopLimit(this.hopLimit);

        try {
            this.builder.srcAddr((Inet6Address) Inet6Address.getByName(this.srcAddr))
                    .dstAddr((Inet6Address) Inet6Address.getByName(this.dstAddr));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void CraftBuilder(Packet.Builder builder) {
        this.CraftBuilder();
        this.builder.payloadBuilder(builder);
    }


    @Override
    public Packet CraftPacket() {
        return this.builder.build();
    }

    @Override
    public void Dump(String filename) throws PcapNativeException, NotOpenException, IOException {
        this.pcapHandle = Pcaps.openDead(DataLinkType.EN10MB, 0);  // todo 链路类型自动适应
        this.jsonHandle = new JsonHandle();
        this.dumper = this.pcapHandle.dumpOpen(this.packetPath+filename+".pcap");

        this.dumper.dump(this.builder.build());
        this.jsonHandle.Object2Json(new File(this.configPath+filename+".json"), this);
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

    public Byte getNextHeader() {
        return nextHeader;
    }

    public void setNextHeader(Byte nextHeader) {
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

    public Byte getVersion() {
        return version;
    }

    public void setVersion(Byte version) {
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
