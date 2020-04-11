package pkit.core.base.packet;

import org.pcap4j.core.*;
import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.NamedNumber;
import org.pcap4j.util.MacAddress;
import pkit.util.JsonHandle;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class PacketHandle {
    private PcapPacket packet;
    // 包额外信息，需要添加到解析信息之中，组成一个数据包
    private String id;
    private int originLength ;  // 完整长度
    private int realLength;
    private int payloadLength;  // 载荷长度
    private String sourceMac;
    private String destinationMac;
//    private String sourceVendor;  // 厂商
//    private String destinationVendor;
    private String sourceIp;
    private String destinationIp;
//    private String sourceLocation;  // 位置
//    private String destinationLocation;
    private String description;
    private ArrayList<String> typeArrayList;
    private ArrayList<PPacket> packetArrayList;
    private Date timeStamp;  // 时间戳，可以是捕获时间也可以是修改或构造时间


    public PacketHandle(PcapPacket packet){
        this.packet = packet;

        this.typeArrayList = new ArrayList<>(5);
        this.packetArrayList = new ArrayList<>(5);
    }

    public void Pipeline() {
        // todo 描述生成
        this.originLength = this.packet.getOriginalLength();
        this.realLength = this.packet.length();
        this.payloadLength = this.packet.getPayload().length();
        if (this.packet.contains(EthernetPacket.class)) {
            EthernetPPacket ethernetPPacket = new EthernetPPacket();
            ethernetPPacket.Parse(this.packet);
            this.sourceMac = ethernetPPacket.getSrcAddr();
            this.destinationMac = ethernetPPacket.getDstAddr();
            this.typeArrayList.add(ethernetPPacket.getType());
            this.packetArrayList.add(ethernetPPacket);
            if (this.packet.contains(ArpPacket.class)) {
                ArpPPacket arpPPacket = new ArpPPacket();
                arpPPacket.Parse(this.packet);
                this.sourceIp = arpPPacket.getSrcProtocolAddr();
                this.destinationIp = arpPPacket.getDstProtocolAddr();
                this.typeArrayList.add(arpPPacket.getProtocolType());
                this.packetArrayList.add(arpPPacket);
            } else if (this.packet.contains(IpV4Packet.class)) {
                Ipv4PPacket ipv4PPacket = new Ipv4PPacket();
                ipv4PPacket.Parse(this.packet);
                this.sourceIp = ipv4PPacket.getSrcAddr();
                this.destinationIp = ipv4PPacket.getDstAddr();
                this.typeArrayList.add(ipv4PPacket.getProtocol());
                this.packetArrayList.add(ipv4PPacket);
                if (this.packet.contains(UdpPacket.class)) {
                    UdpPPacket udpPPacket = new UdpPPacket();
                    udpPPacket.Parse(this.packet);
                    this.typeArrayList.add(udpPPacket.getSrcPort());
                    this.typeArrayList.add(udpPPacket.getDstPort());
                    this.packetArrayList.add(udpPPacket);
                } else if (this.packet.contains(TcpPacket.class)) {
                    TcpPPacket tcpPPacket = new TcpPPacket();
                    tcpPPacket.Parse(this.packet);
                    this.typeArrayList.add(tcpPPacket.getSrcPort());
                    this.typeArrayList.add(tcpPPacket.getDstPort());
                    this.packetArrayList.add(tcpPPacket);
                }  // todo 添加更多协议支持，icmpv4
            } else if (this.packet.contains(IpV6Packet.class)) {
                Ipv6PPacket ipv6PPacket = new Ipv6PPacket();
                ipv6PPacket.Parse(this.packet);
                this.sourceIp = ipv6PPacket.getSrcAddr();
                this.destinationIp = ipv6PPacket.getDstAddr();
                this.typeArrayList.add(ipv6PPacket.getNextHeader());
                this.packetArrayList.add(ipv6PPacket);

                // todo 添加更多协议支持，icmpv6
            }
        }
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ArrayList<PPacket> getPacketArrayList() {
        return packetArrayList;
    }

    public void setPacketArrayList(ArrayList<PPacket> packetArrayList) {
        this.packetArrayList = packetArrayList;
    }

    public ArrayList<String> getTypeArrayList() {
        return typeArrayList;
    }

    public void setTypeArrayList(ArrayList<String> typeArrayList) {
        this.typeArrayList = typeArrayList;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestinationIp() {
        return destinationIp;
    }

    public void setDestinationIp(String destinationIp) {
        this.destinationIp = destinationIp;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getDestinationMac() {
        return destinationMac;
    }

    public void setDestinationMac(String destinationMac) {
        this.destinationMac = destinationMac;
    }

    public String getSourceMac() {
        return sourceMac;
    }

    public void setSourceMac(String sourceMac) {
        this.sourceMac = sourceMac;
    }

    public int getPayloadLength() {
        return payloadLength;
    }

    public void setPayloadLength(int payloadLength) {
        this.payloadLength = payloadLength;
    }

    public int getRealLength() {
        return realLength;
    }

    public void setRealLength(int realLength) {
        this.realLength = realLength;
    }

    public int getOriginLength() {
        return originLength;
    }

    public void setOriginLength(int originLength) {
        this.originLength = originLength;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
