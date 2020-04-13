package pkit.core.base.packet;

import org.pcap4j.core.*;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.ArpHardwareType;
import org.pcap4j.packet.namednumber.ArpOperation;
import org.pcap4j.packet.namednumber.DataLinkType;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.MacAddress;
import pkit.util.JsonHandle;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ArpPPacket implements PPacket{
    private ArpPacket.Builder builder;
    private PcapHandle pcapHandle;
    private JsonHandle jsonHandle;
    private PcapDumper dumper;
    private String packetPath;  // todo 设置默认路径
    private String configPath;

    private String name;
    private int length;
    private Short hardwareType;
    private Short protocolType;
    private byte hardwareAddrLength;
    private byte protocolAddrLength;
    private Short operation;
    private String  srcHardwareAddr;
    private String srcProtocolAddr;
    private String dstHardwareAddr;
    private String dstProtocolAddr;

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
        this.builder = new ArpPacket.Builder();
        this.packetPath = "tmp/";
        this.configPath = "tmp/";

        this.name = "arp";
        this.length = 28;
        this.hardwareType = ArpHardwareType.ETHERNET.value();
        this.protocolType = EtherType.IPV4.value();
        this.hardwareAddrLength = 6;
        this.protocolAddrLength  = 4;
        this.operation = ArpOperation.REQUEST.value();
        this.srcHardwareAddr = "00:00:00:00:00:01";
        this.srcProtocolAddr = "192.168.1.1";
        this.dstHardwareAddr = MacAddress.ETHER_BROADCAST_ADDRESS.toString();
        this.dstProtocolAddr = "192.168.1.0";
    }

    @Override
    public void Parse(PcapPacket pcapPacket) {
        ArpPacket.ArpHeader header = pcapPacket.get(ArpPacket.class).getHeader();
        this.length = header.length();
        this.hardwareType = header.getHardwareType().value();
        this.protocolType = header.getProtocolType().value();
        this.hardwareAddrLength = header.getHardwareAddrLength();
        this.protocolAddrLength = header.getProtocolAddrLength();
        this.operation = header.getOperation().value();
        this.srcHardwareAddr = header.getSrcHardwareAddr().toString();
        this.srcProtocolAddr = header.getSrcProtocolAddr().getHostAddress();
        this.dstHardwareAddr = header.getDstHardwareAddr().toString();
        this.dstProtocolAddr = header.getDstProtocolAddr().getHostAddress();
    }

    @Override
    public String description() {
        return null;
    }


    @Override
    public void CraftBuilder() {
        this.builder.hardwareType(ArpHardwareType.getInstance(this.hardwareType))
                .protocolType(EtherType.getInstance(this.protocolType))
                .hardwareAddrLength(this.hardwareAddrLength)
                .protocolAddrLength(this.protocolAddrLength)
                .operation(ArpOperation.getInstance(this.operation))
                .srcHardwareAddr(MacAddress.getByName(this.srcHardwareAddr))
                .dstHardwareAddr(MacAddress.getByName(this.dstHardwareAddr));
        try {
            this.builder.srcProtocolAddr(InetAddress.getByName(this.srcProtocolAddr))
                    .dstProtocolAddr(InetAddress.getByName(this.dstProtocolAddr));
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



    public String getDstProtocolAddr() {
        return dstProtocolAddr;
    }

    public void setDstProtocolAddr(String dstProtocolAddr) {
        this.dstProtocolAddr = dstProtocolAddr;
    }

    public String getDstHardwareAddr() {
        return dstHardwareAddr;
    }

    public void setDstHardwareAddr(String dstHardwareAddr) {
        this.dstHardwareAddr = dstHardwareAddr;
    }

    public String getSrcProtocolAddr() {
        return srcProtocolAddr;
    }

    public void setSrcProtocolAddr(String srcProtocolAddr) {
        this.srcProtocolAddr = srcProtocolAddr;
    }

    public String getSrcHardwareAddr() {
        return srcHardwareAddr;
    }

    public void setSrcHardwareAddr(String srcHardwareAddr) {
        this.srcHardwareAddr = srcHardwareAddr;
    }

    public Short getOperation() {
        return operation;
    }

    public void setOperation(Short operation) {
        this.operation = operation;
    }

    public byte getProtocolAddrLength() {
        return protocolAddrLength;
    }

    public void setProtocolAddrLength(byte protocolAddrLength) {
        this.protocolAddrLength = protocolAddrLength;
    }

    public byte getHardwareAddrLength() {
        return hardwareAddrLength;
    }

    public void setHardwareAddrLength(byte hardwareAddrLength) {
        this.hardwareAddrLength = hardwareAddrLength;
    }

    public Short getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(Short protocolType) {
        this.protocolType = protocolType;
    }

    public Short getHardwareType() {
        return hardwareType;
    }

    public void setHardwareType(Short hardwareType) {
        this.hardwareType = hardwareType;
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
