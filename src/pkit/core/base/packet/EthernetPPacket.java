package pkit.core.base.packet;

import org.pcap4j.core.*;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.DataLinkType;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.MacAddress;
import pkit.util.JsonHandle;

import java.io.File;
import java.io.IOException;

public class EthernetPPacket implements PPacket{
    private EthernetPacket.Builder builder;
    private PcapHandle pcapHandle;
    private JsonHandle jsonHandle;
    private PcapDumper dumper;
    private String packetPath;  // todo 设置默认路径
    private String configPath;

    private String name;
    private int length;
    private String  dstAddr;
    private String  srcAddr;
    private Short type;

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
        this.builder = new EthernetPacket.Builder();
        this.packetPath = "tmp/";
        this.configPath = "tmp/";

        this.name = "ethernet";
        this.length = 14;
        this.dstAddr = "ff:ff:ff:ff:ff:ff";
        this.srcAddr = "00:00:00:00:00:01";
        this.type = 2048;  // IPv4
    }

    @Override
    public void Parse(PcapPacket pcapPacket) {
        EthernetPacket.EthernetHeader header = pcapPacket.get(EthernetPacket.class).getHeader();
        this.length = header.length();
        this.dstAddr = header.getDstAddr().toString();
        this.srcAddr = header.getSrcAddr().toString();
        this.type = header.getType().value();
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public void CraftBuilder() {
        // TODO: 2020/4/12 builder 如果没有用到一些字段解决办法
        // builder 是载荷的构建器
        this.builder
                .dstAddr(MacAddress.getByName(this.dstAddr))
                .srcAddr(MacAddress.getByName(this.srcAddr))
                .type(EtherType.getInstance(this.type))
                .payloadBuilder(builder)
                .paddingAtBuild(true);
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

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
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
