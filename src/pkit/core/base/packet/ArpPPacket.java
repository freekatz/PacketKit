package pkit.core.base.packet;

import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.namednumber.ArpHardwareType;
import org.pcap4j.packet.namednumber.ArpOperation;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.MacAddress;

import java.net.InetAddress;

public class ArpPPacket implements PPacket {
    private String name;
    private int length;
    private String hardwareType;
    private String protocolType;
    private byte hardwareAddrLength;
    private byte protocolAddrLength;
    private String operation;
    private String  srcHardwareAddr;
    private String srcProtocolAddr;
    private String dstHardwareAddr;
    private String dstProtocolAddr;

    @Override
    public void Initial() {
        this.name = "arp";
    }

    @Override
    public void Parse(PcapPacket pcapPacket) {
        ArpPacket.ArpHeader header = pcapPacket.get(ArpPacket.class).getHeader();
        this.length = header.length();
        this.hardwareType = header.getHardwareType().valueAsString();
        this.protocolType = header.getProtocolType().valueAsString();
        this.hardwareAddrLength = header.getHardwareAddrLength();
        this.protocolAddrLength = header.getProtocolAddrLength();
        this.operation = header.getOperation().valueAsString();
        this.srcHardwareAddr = header.getSrcHardwareAddr().toString();
        this.srcProtocolAddr = header.getSrcProtocolAddr().getHostAddress();
        this.dstHardwareAddr = header.getDstHardwareAddr().toString();
        this.dstProtocolAddr = header.getDstProtocolAddr().getHostAddress();
    }

    @Override
    public PcapPacket Craft() {
        return null;
    }

    @Override
    public void Dump(String path) {

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

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
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

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public String getHardwareType() {
        return hardwareType;
    }

    public void setHardwareType(String hardwareType) {
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
