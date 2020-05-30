package gui.model.packet;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.pcap4j.core.*;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.ArpHardwareType;
import org.pcap4j.packet.namednumber.ArpOperation;
import org.pcap4j.packet.namednumber.DataLinkType;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.MacAddress;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ArpPPacket implements PPacket{
    private final ArpPacket.Builder builder = new ArpPacket.Builder();
    private PcapHandle pcapHandle;
    private JsonMapper jsonMapper;
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
        return "ARP";
    }

    @Override
    public void Initial() {
        this.packetPath = "tmp/";
        this.configPath = "tmp/";

        this.name = "ARP";
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
        ParseHandle(header);
    }

    @Override
    public void Parse(Packet packet) {
        ArpPacket.ArpHeader header = packet.get(ArpPacket.class).getHeader();
        ParseHandle(header);
    }

    @Override
    public void ParseHandle(Packet.Header packetHeader) {
        ArpPacket.ArpHeader header = (ArpPacket.ArpHeader) packetHeader;
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
        if (this.operation.equals(ArpOperation.REQUEST.value()))
            return  "who is " + this.dstProtocolAddr + "? tell " + this.srcProtocolAddr + ", I'm " + this.srcHardwareAddr;
        else if (this.operation.equals(ArpOperation.REPLY.value()))
            return this.srcProtocolAddr + " is at " + this.srcHardwareAddr;
        else if (this.operation.equals(ArpOperation.REQUEST_REVERSE.value()))
            return "who is " + this.dstHardwareAddr + "? tell " + this.srcHardwareAddr + ", I'm " + this.srcProtocolAddr;
        else if (this.operation.equals(ArpOperation.REPLY_REVERSE.value()))
            return this.srcHardwareAddr + " is at " + this.srcProtocolAddr;
        else
            return this.srcProtocolAddr + " to " + this.dstProtocolAddr;
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
        this.jsonMapper = new JsonMapper();
        this.dumper = this.pcapHandle.dumpOpen(this.packetPath+filename+".pcap");

        this.dumper.dump(this.builder.build());
        this.jsonMapper.writeValue(new File(this.configPath+filename+".json"), this);
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
        return "ARP";
    }

    public void setName(String name) {
        this.name = "ARP";
    }
}
