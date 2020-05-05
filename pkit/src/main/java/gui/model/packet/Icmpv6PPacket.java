package gui.model.packet;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.pcap4j.core.*;
import org.pcap4j.packet.IcmpV6EchoRequestPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.DataLinkType;

import java.io.File;
import java.io.IOException;

public class Icmpv6PPacket implements PPacket {
    private IcmpV6EchoRequestPacket.Builder builder = new IcmpV6EchoRequestPacket.Builder();
    private PcapHandle pcapHandle;
    private JsonMapper jsonMapper;
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
        return "ICMPv6";
    }

    @Override
    public void Initial() {

    }

    @Override
    public void Parse(PcapPacket pcapPacket) {

    }

    @Override
    public void Parse(Packet packet) {

    }

    @Override
    public void ParseHandle(Packet.Header packetHeader) {

    }

    @Override
    public String description() {
        return "";
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
        this.jsonMapper = new JsonMapper();
        this.dumper = this.pcapHandle.dumpOpen(this.packetPath+filename+".pcap");

        this.dumper.dump(this.builder.build());
        this.jsonMapper.writeValue(new File(this.configPath+filename+".json"), this);
    }

}
