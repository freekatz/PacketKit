package packet;

import org.pcap4j.core.*;
import org.pcap4j.packet.*;

import java.util.ArrayList;
import java.util.Date;

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
            this.ArrayHandle(ethernetPPacket);
            this.sourceMac = ethernetPPacket.getSrcAddr();
            this.destinationMac = ethernetPPacket.getDstAddr();
            if (this.packet.contains(ArpPacket.class)) {
                ArpPPacket arpPPacket = new ArpPPacket();
                this.ArrayHandle(arpPPacket);
                this.sourceIp = arpPPacket.getSrcProtocolAddr();
                this.destinationIp = arpPPacket.getDstProtocolAddr();
            } else if (this.packet.contains(IpV4Packet.class)) {
                Ipv4PPacket ipv4PPacket = new Ipv4PPacket();
                this.ArrayHandle(ipv4PPacket);
                this.sourceIp = ipv4PPacket.getSrcAddr();
                this.destinationIp = ipv4PPacket.getDstAddr();
                if (this.packet.contains(UdpPacket.class)) {
                    UdpPPacket udpPPacket = new UdpPPacket();
                    this.ArrayHandle(udpPPacket);
//                    if (this.packet.contains(DnsPacket.class)) {
//                        DnsPPacket dnsPPacket = new DnsPPacket();
//                        this.ArrayHandle(dnsPPacket);
//                    }  // todo dns 实现
                } else if (this.packet.contains(TcpPacket.class)) {
                    TcpPPacket tcpPPacket = new TcpPPacket();
                    this.ArrayHandle(tcpPPacket);
                }  // todo 添加更多协议支持，icmpv4
            } else if (this.packet.contains(IpV6Packet.class)) {
                Ipv6PPacket ipv6PPacket = new Ipv6PPacket();
                this.ArrayHandle(ipv6PPacket);
                this.sourceIp = ipv6PPacket.getSrcAddr();
                this.destinationIp = ipv6PPacket.getDstAddr();
                // todo 添加更多协议支持，icmpv6
            }
        }
    }

    private void ArrayHandle(PPacket pPacket) {
        pPacket.Parse(this.packet);
        this.description = pPacket.description();  // todo 实现描述
        this.typeArrayList.add(pPacket.name());
        this.packetArrayList.add(pPacket);
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
