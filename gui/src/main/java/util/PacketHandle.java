package util;

import model.PacketProperty;
import model.packet.*;
import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.*;

public class PacketHandle {


    public static PacketProperty Pipeline(PcapPacket packet) {
        // todo 描述生成
        PacketProperty packetProperty = new PacketProperty();
        packetProperty.setLength(packet.getOriginalLength());
        packetProperty.setTime(packet.getTimestamp().toString());
        if (packet.contains(EthernetPacket.class)) {
            EthernetPPacket ethernetPPacket = new EthernetPPacket();
            ethernetPPacket.Parse(packet);
            packetProperty.setSrc(ethernetPPacket.getSrcAddr());
            packetProperty.setDst(ethernetPPacket.getDstAddr());
            packetProperty.setProtocol("Ethernet");
            packetProperty.setInfo(ethernetPPacket.description());
            if (packet.contains(ArpPacket.class)) {
                ArpPPacket arpPPacket = new ArpPPacket();
                arpPPacket.Parse(packet);
                packetProperty.setProtocol("ARP");
                packetProperty.setInfo(arpPPacket.description());
            } else if (packet.contains(IpV4Packet.class)) {
                Ipv4PPacket ipv4PPacket = new Ipv4PPacket();
                ipv4PPacket.Parse(packet);
                packetProperty.setSrc(ipv4PPacket.getSrcAddr());
                packetProperty.setDst(ipv4PPacket.getDstAddr());
                packetProperty.setProtocol("IPv4");
                packetProperty.setInfo(ipv4PPacket.description());
                if (packet.contains(UdpPacket.class)) {
                    UdpPPacket udpPPacket = new UdpPPacket();
                    udpPPacket.Parse(packet);
                    packetProperty.setSrc(packetProperty.getSrc() + ":" + udpPPacket.getSrcPort());
                    packetProperty.setDst(packetProperty.getDst() + ":" + udpPPacket.getDstPort());
                    packetProperty.setProtocol("UDP");
                    packetProperty.setInfo(udpPPacket.description());
//                    if (this.packet.contains(DnsPacket.class)) {
//                        DnsPPacket dnsPPacket = new DnsPPacket();
//                        this.ArrayHandle(dnsPPacket);
//                    }  // todo dns 实现
                } else if (packet.contains(TcpPacket.class)) {
                    TcpPPacket tcpPPacket = new TcpPPacket();
                    tcpPPacket.Parse(packet);
                    packetProperty.setSrc(packetProperty.getSrc() + ":" + tcpPPacket.getSrcPort());
                    packetProperty.setDst(packetProperty.getDst() + ":" + tcpPPacket.getDstPort());
                    packetProperty.setProtocol("TCP");
                    packetProperty.setInfo(tcpPPacket.description());
                }  // todo 添加更多协议支持，icmpv4
            } else if (packet.contains(IpV6Packet.class)) {
                Ipv6PPacket ipv6PPacket = new Ipv6PPacket();
                ipv6PPacket.Parse(packet);
                packetProperty.setSrc(ipv6PPacket.getSrcAddr());
                packetProperty.setDst(ipv6PPacket.getDstAddr());
                packetProperty.setProtocol("IPv6");
                packetProperty.setInfo(ipv6PPacket.description());
                // todo 添加更多协议支持，icmpv6
            }
        }

        return packetProperty;
    }


}
