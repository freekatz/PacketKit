package pkit.core.base.packet;

import org.pcap4j.core.*;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.UdpPacket;
import org.pcap4j.packet.namednumber.DataLinkType;
import org.pcap4j.packet.namednumber.UdpPort;
import pkit.util.JsonHandle;

import java.io.File;
import java.io.IOException;

public class UdpPPacket implements PPacket {
    private UdpPacket.Builder builder;
    private PcapHandle pcapHandle;
    private JsonHandle jsonHandle;
    private PcapDumper dumper;
    private String packetPath;  // todo 设置默认路径
    private String configPath;

    private String name;
    private int length;
    private short srcPort;
    private short dstPort;
    private short totalLength;
    private short checksum;

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
        this.builder = new UdpPacket.Builder();
        this.packetPath = "tmp/";
        this.configPath = "tmp/";

        this.name = "udp";
        this.length = 8;
        this.srcPort = 53;
        this.dstPort = 53;
        this.totalLength = 8; // 无 payload
        this.checksum = 0;
    }

    @Override
    public void Parse(PcapPacket pcapPacket) {
        UdpPacket.UdpHeader header = pcapPacket.get(UdpPacket.class).getHeader();
        this.length = header.length();
        this.srcPort = header.getSrcPort().value();
        this.dstPort = header.getDstPort().value();
        this.totalLength = header.getLength();
        this.checksum = header.getChecksum();
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public void CraftBuilder() {
        this.builder.srcPort(UdpPort.getInstance(this.srcPort))
                .dstPort(UdpPort.getInstance(this.dstPort))
                .length(this.totalLength)
                .checksum(this.checksum)
                .correctLengthAtBuild(true)
                .correctChecksumAtBuild(true);
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

    public Short getDstPort() {
        return dstPort;
    }

    public void setDstPort(Short dstPort) {
        this.dstPort = dstPort;
    }

    public Short getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(Short srcPort) {
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
