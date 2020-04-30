package util;

import gui.model.packet.*;
import gui.model.browser.PacketDataProperty;
import gui.model.browser.PacketHeaderProperty;
import gui.model.browser.PacketInfoProperty;
import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.*;

public class PacketHandle {


    public static PacketInfoProperty InfoPipeline(PcapPacket packet) {
        // todo 描述生成
        PacketInfoProperty packetInfoProperty = new PacketInfoProperty();
        packetInfoProperty.setLength(packet.getOriginalLength());
        packetInfoProperty.setTime(packet.getTimestamp().toString());
        if (packet.contains(EthernetPacket.class)) {
            EthernetPPacket ethernetPPacket = new EthernetPPacket();
            ethernetPPacket.Parse(packet);
            packetInfoProperty.setSrc(ethernetPPacket.getSrcAddr());
            packetInfoProperty.setDst(ethernetPPacket.getDstAddr());
            packetInfoProperty.setProtocol("Ethernet");
            packetInfoProperty.setInfo(ethernetPPacket.description());
            if (packet.contains(ArpPacket.class)) {
                ArpPPacket arpPPacket = new ArpPPacket();
                arpPPacket.Parse(packet);
                packetInfoProperty.setProtocol("ARP");
                packetInfoProperty.setInfo(arpPPacket.description());
            } else if (packet.contains(IpV4Packet.class)) {
                Ipv4PPacket ipv4PPacket = new Ipv4PPacket();
                ipv4PPacket.Parse(packet);
                packetInfoProperty.setSrc(ipv4PPacket.getSrcAddr());
                packetInfoProperty.setDst(ipv4PPacket.getDstAddr());
                packetInfoProperty.setProtocol("IPv4");
                packetInfoProperty.setInfo(ipv4PPacket.description());
                if (packet.contains(UdpPacket.class)) {
                    UdpPPacket udpPPacket = new UdpPPacket();
                    udpPPacket.Parse(packet);
                    packetInfoProperty.setSrc(packetInfoProperty.getSrc() + ":" + udpPPacket.getSrcPort());
                    packetInfoProperty.setDst(packetInfoProperty.getDst() + ":" + udpPPacket.getDstPort());
                    packetInfoProperty.setProtocol("UDP");
                    packetInfoProperty.setInfo(udpPPacket.description());
//                    if (this.packet.contains(DnsPacket.class)) {
//                        DnsPPacket dnsPPacket = new DnsPPacket();
//                        this.ArrayHandle(dnsPPacket);
//                    }  // todo dns 实现
                } else if (packet.contains(TcpPacket.class)) {
                    TcpPPacket tcpPPacket = new TcpPPacket();
                    tcpPPacket.Parse(packet);
                    packetInfoProperty.setSrc(packetInfoProperty.getSrc() + ":" + tcpPPacket.getSrcPort());
                    packetInfoProperty.setDst(packetInfoProperty.getDst() + ":" + tcpPPacket.getDstPort());
                    packetInfoProperty.setProtocol("TCP");
                    packetInfoProperty.setInfo(tcpPPacket.description());
                }  // todo 添加更多协议支持，icmpv4
            } else if (packet.contains(IpV6Packet.class)) {
                Ipv6PPacket ipv6PPacket = new Ipv6PPacket();
                ipv6PPacket.Parse(packet);
                packetInfoProperty.setSrc(ipv6PPacket.getSrcAddr());
                packetInfoProperty.setDst(ipv6PPacket.getDstAddr());
                packetInfoProperty.setProtocol("IPv6");
                packetInfoProperty.setInfo(ipv6PPacket.description());
                // todo 添加更多协议支持，icmpv6
            }
        }

        return packetInfoProperty;
    }

    public static PacketHeaderProperty HeaderPipeline() {
        // 将 PPacket 改造为 Property，然后折腾 treeview，延后
        return null;
    }

    public static PacketDataProperty DataPipeline() {
        // 延后
        return null;
    }

}
