package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import gui.ctrl.View;
import gui.model.browser.*;
import gui.model.packet.*;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;

public class PacketHandle {

    public static PacketProperty Pipeline(PcapPacket packet) {

        PacketProperty packetProperty = new PacketProperty();
        PacketInfoProperty packetInfoProperty = new PacketInfoProperty();
        PacketHeaderProperty packetHeaderProperty = new PacketHeaderProperty();
        PacketDataProperty packetDataProperty = new PacketDataProperty();


        packetInfoProperty.setLength(packet.getOriginalLength());
        packetInfoProperty.setTime(packet.getTimestamp().toString());

        packetDataProperty.setTxt(TypeHandle.BytesToTxt(packet.getRawData()));
        packetDataProperty.setHex(TypeHandle.BytesToHex(packet.getRawData()));

        if (packet.contains(EthernetPacket.class)) {
            EthernetPPacket ethernetPPacket = new EthernetPPacket();
            ethernetPPacket.Parse(packet);
            packetInfoProperty.setSrc(ethernetPPacket.getSrcAddr());
            packetInfoProperty.setDst(ethernetPPacket.getDstAddr());
            packetInfoProperty.setProtocol("Ethernet");
            packetInfoProperty.setInfo(ethernetPPacket.description());
            
            packetHeaderProperty.getHeader().add(ethernetPPacket);

            if (packet.contains(ArpPacket.class)) {
                ArpPPacket arpPPacket = new ArpPPacket();
                arpPPacket.Parse(packet);
                packetInfoProperty.setProtocol("ARP");
                packetInfoProperty.setInfo(arpPPacket.description());

                packetHeaderProperty.getHeader().add(arpPPacket);
                
            } else if (packet.contains(IpV4Packet.class)) {
                Ipv4PPacket ipv4PPacket = new Ipv4PPacket();
                ipv4PPacket.Parse(packet);
                packetInfoProperty.setSrc(ipv4PPacket.getSrcAddr());
                packetInfoProperty.setDst(ipv4PPacket.getDstAddr());
                packetInfoProperty.setProtocol("IPv4");
                packetInfoProperty.setInfo(ipv4PPacket.description());

                packetHeaderProperty.getHeader().add(ipv4PPacket);


                if (packet.contains(UdpPacket.class)) {
                    UdpPPacket udpPPacket = new UdpPPacket();
                    udpPPacket.Parse(packet);
                    packetInfoProperty.setSrc(packetInfoProperty.getSrc() + ":" + udpPPacket.getSrcPort());
                    packetInfoProperty.setDst(packetInfoProperty.getDst() + ":" + udpPPacket.getDstPort());
                    packetInfoProperty.setProtocol("UDP");
                    packetInfoProperty.setInfo(udpPPacket.description());

                    packetHeaderProperty.getHeader().add(udpPPacket);
                    
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

                    packetHeaderProperty.getHeader().add(tcpPPacket);
                    
                }  // todo 添加更多协议支持，icmpv4
            } else if (packet.contains(IpV6Packet.class)) {
                Ipv6PPacket ipv6PPacket = new Ipv6PPacket();
                ipv6PPacket.Parse(packet);
                packetInfoProperty.setSrc(ipv6PPacket.getSrcAddr());
                packetInfoProperty.setDst(ipv6PPacket.getDstAddr());
                packetInfoProperty.setProtocol("IPv6");
                packetInfoProperty.setInfo(ipv6PPacket.description());

                packetHeaderProperty.getHeader().add(ipv6PPacket);
                
                // todo 添加更多协议支持，icmpv6
            }
        }

        packetProperty.setInfo(packetInfoProperty);
        packetProperty.setHeader(packetHeaderProperty);
        packetProperty.setData(packetDataProperty);

        return packetProperty;
    }

    public static PacketProperty Parse(Packet packet, PacketProperty packetProperty) {

        PacketInfoProperty packetInfoProperty = (PacketInfoProperty) packetProperty.getInfo().clone();
        PacketHeaderProperty packetHeaderProperty = (PacketHeaderProperty) packetProperty.getHeader().clone();
        PacketDataProperty packetDataProperty = (PacketDataProperty) packetProperty.getData().clone();

        packetHeaderProperty.getHeader().clear();

        packetInfoProperty.setLength(packet.length());

        packetDataProperty.setTxt(TypeHandle.BytesToTxt(packet.getRawData()));
        packetDataProperty.setHex(TypeHandle.BytesToHex(packet.getRawData()));

        if (packet.contains(EthernetPacket.class)) {
            EthernetPPacket ethernetPPacket = new EthernetPPacket();
            ethernetPPacket.Parse(packet);
            packetInfoProperty.setSrc(ethernetPPacket.getSrcAddr());
            packetInfoProperty.setDst(ethernetPPacket.getDstAddr());
            packetInfoProperty.setProtocol("Ethernet");
            packetInfoProperty.setInfo(ethernetPPacket.description());

            packetHeaderProperty.getHeader().add(ethernetPPacket);

            if (packet.contains(ArpPacket.class)) {
                ArpPPacket arpPPacket = new ArpPPacket();
                arpPPacket.Parse(packet);
                packetInfoProperty.setProtocol("ARP");
                packetInfoProperty.setInfo(arpPPacket.description());

                packetHeaderProperty.getHeader().add(arpPPacket);

            } else if (packet.contains(IpV4Packet.class)) {
                Ipv4PPacket ipv4PPacket = new Ipv4PPacket();
                ipv4PPacket.Parse(packet);
                packetInfoProperty.setSrc(ipv4PPacket.getSrcAddr());
                packetInfoProperty.setDst(ipv4PPacket.getDstAddr());
                packetInfoProperty.setProtocol("IPv4");
                packetInfoProperty.setInfo(ipv4PPacket.description());

                packetHeaderProperty.getHeader().add(ipv4PPacket);


                if (packet.contains(UdpPacket.class)) {
                    UdpPPacket udpPPacket = new UdpPPacket();
                    udpPPacket.Parse(packet);
                    packetInfoProperty.setSrc(packetInfoProperty.getSrc() + ":" + udpPPacket.getSrcPort());
                    packetInfoProperty.setDst(packetInfoProperty.getDst() + ":" + udpPPacket.getDstPort());
                    packetInfoProperty.setProtocol("UDP");
                    packetInfoProperty.setInfo(udpPPacket.description());

                    packetHeaderProperty.getHeader().add(udpPPacket);

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

                    packetHeaderProperty.getHeader().add(tcpPPacket);

                }  // todo 添加更多协议支持，icmpv4
            } else if (packet.contains(IpV6Packet.class)) {
                Ipv6PPacket ipv6PPacket = new Ipv6PPacket();
                ipv6PPacket.Parse(packet);
                packetInfoProperty.setSrc(ipv6PPacket.getSrcAddr());
                packetInfoProperty.setDst(ipv6PPacket.getDstAddr());
                packetInfoProperty.setProtocol("IPv6");
                packetInfoProperty.setInfo(ipv6PPacket.description());

                packetHeaderProperty.getHeader().add(ipv6PPacket);

                // todo 添加更多协议支持，icmpv6
            }
        }

        packetProperty.setInfo(packetInfoProperty);
        packetProperty.setHeader(packetHeaderProperty);
        packetProperty.setData(packetDataProperty);

        return packetProperty;
    }

    public static Packet Restore(PacketProperty packetProperty) throws UnknownHostException {
        // TODO: 2020/5/3 还原属性对象

        // 1 按序遍历出协议名称和 ppacket 对象， 2 执行craft
        PacketHeaderProperty packetHeaderProperty = packetProperty.getHeader();

        LinkedList<PPacket> header = packetHeaderProperty.getHeader();

        EthernetPPacket ethernetPPacket = null;
        ArpPPacket arpPPacket = null;
        Ipv4PPacket ipv4PPacket = null;
        Ipv6PPacket ipv6PPacket = null;
        UdpPPacket udpPPacket = null;
        TcpPPacket tcpPPacket = null;

        ethernetPPacket = (EthernetPPacket) header.get(0);

        String type = "";
        switch (header.get(1).name()) {
            case "ARP":
                arpPPacket = (ArpPPacket) header.get(1);
                type = "ARP";
                break;
            case "IPv4": ipv4PPacket = (Ipv4PPacket) header.get(1);
                type = "IPv4";
                break;
            case "IPv6": ipv6PPacket = (Ipv6PPacket) header.get(1);
                type = "IPv6";
                break;
        }

        if (header.size()==3) {
            switch (header.get(2).name()) {
                case "UDP":
                    udpPPacket = (UdpPPacket) header.get(2);
                    type = "UDP";
                    break;
                case "TCP":
                    tcpPPacket = (TcpPPacket) header.get(2);
                    type = "TCP";
                    break;
            }
        }


        switch (type) {
            case "":
                break;
            case "ARP":
                arpPPacket.CraftBuilder();
                ethernetPPacket.CraftBuilder(arpPPacket.builder());
                break;
            case "IPv4":
                ipv4PPacket.CraftBuilder();
                ethernetPPacket.CraftBuilder(ipv4PPacket.builder());
                break;
            case "IPv6":
                ipv6PPacket.CraftBuilder();
                ethernetPPacket.CraftBuilder(ipv6PPacket.builder());
                break;
            case "UDP":
                assert ipv4PPacket != null;
                // 这里易错
                UdpPacket.Builder udpBuilder = (UdpPacket.Builder) udpPPacket.builder();  // 只有这种特殊情况才提取出 builder
                udpBuilder.srcAddr(InetAddress.getByName(ipv4PPacket.getSrcAddr()))
                        .dstAddr(InetAddress.getByName(ipv4PPacket.getDstAddr()));  // 这里其实没有影响
                udpPPacket.CraftBuilder();
                ipv4PPacket.CraftBuilder(udpPPacket.builder());
                ethernetPPacket.CraftBuilder(ipv4PPacket.builder());
                break;
            case "TCP":
                assert ipv4PPacket != null;
                TcpPacket.Builder tcpBuilder = (TcpPacket.Builder) tcpPPacket.builder();  // 只有这种特殊情况才提取出 builder
                tcpBuilder.srcAddr(InetAddress.getByName(ipv4PPacket.getSrcAddr()))
                    .dstAddr(InetAddress.getByName(ipv4PPacket.getDstAddr()));  // 这里其实没有影响
                tcpPPacket.CraftBuilder();
                ipv4PPacket.CraftBuilder(tcpPPacket.builder());
                ethernetPPacket.CraftBuilder(ipv4PPacket.builder());
                break;

        }

        return ethernetPPacket.CraftPacket();
    }

    public static String ShortIpv6Addr(String ipv6) {
        // 计算短ipv6地址

        return null;
    }


}
