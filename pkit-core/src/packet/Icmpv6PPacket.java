package pkit.core.base.packet;

import org.pcap4j.core.*;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.IcmpV6EchoRequestPacket;
import org.pcap4j.packet.IpV6Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.DataLinkType;
import pkit.util.JsonHandle;

import java.io.File;
import java.io.IOException;

public class Icmpv6PPacket implements PPacket {
    private IcmpV6EchoRequestPacket.Builder builder;
    private PcapHandle pcapHandle;
    private JsonHandle jsonHandle;
    private PcapDumper dumper;
    private String packetPath;  // todo 设置默认路径
    private String configPath;

    private String name;
    private int length;

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

    }

    @Override
    public void Parse(PcapPacket pcapPacket) {

    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public void CraftBuilder() {

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

}
