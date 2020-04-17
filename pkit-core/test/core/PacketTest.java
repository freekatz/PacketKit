package pkit.core;

import org.pcap4j.core.*;
import org.pcap4j.packet.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeoutException;

public class PacketTest {

    public static void main(String[] args) throws PcapNativeException, IOException, TimeoutException, NotOpenException, IllegalRawDataException {

        // Pipeline Test
//        String path = "tmp/dns.pcap";
//        PcapHandle pcapHandle = Pcaps.openOffline(path);
////        pcapHandle.setFilter("dns", BpfProgram.BpfCompileMode.OPTIMIZE);
//        PcapPacket pcapPacket = pcapHandle.getNextPacketEx();
//        System.out.println(pcapPacket);
//        PacketHandle packetHandle = new PacketHandle(pcapPacket);
//        packetHandle.Pipeline();
//        JsonHandle jsonHandle = new JsonHandle();
//        jsonHandle.Object2Json(new File("tmp/ph.json"), packetHandle);

        // todo 以下生成过程写入 PacketHandle
        // ARP Test
//        ArpPPacket arpPPacket = new ArpPPacket();
//        arpPPacket.Initial();
//        arpPPacket.CraftBuilder();
//
//        EthernetPPacket ethernetPPacket = new EthernetPPacket();
//        ethernetPPacket.Initial();
//        ethernetPPacket.setType(EtherType.ARP.value());
//        EthernetPacket ethernetPacket = (EthernetPacket) ethernetPPacket.CraftPacket(arpPPacket.builder());
//        System.out.println(ethernetPacket);

//        // IPv4 Test
//        // 定义及初始化
//        Ipv4PPacket ipv4PPacket = new Ipv4PPacket();
//        ipv4PPacket.Initial();
//        EthernetPPacket ethernetPPacket = new EthernetPPacket();
//        ethernetPPacket.Initial();
//
//        // 字段修改
//
//        // 上传载荷
//        ipv4PPacket.CraftBuilder();
//        ethernetPPacket.CraftBuilder(ipv4PPacket.builder());
//
//        // 数据包生成
//        IpV4Packet ipV4Packet = (IpV4Packet) ipv4PPacket.CraftPacket();
//        System.out.println(ipV4Packet);
//
//        EthernetPacket ethernetPacket = (EthernetPacket) ethernetPPacket.CraftPacket();
//        System.out.println(ethernetPacket);

//        // IPv6 Test
//        // 定义及初始化
//        Ipv6PPacket ipv6PPacket = new Ipv6PPacket();
//        ipv6PPacket.Initial();
//        EthernetPPacket ethernetPPacket = new EthernetPPacket();
//        ethernetPPacket.Initial();
//
//        // 字段修改
//        ethernetPPacket.setType(EtherType.IPV6.value());
//
//        // 上传载荷
//        ipv6PPacket.CraftBuilder();
//        ethernetPPacket.CraftBuilder(ipv6PPacket.builder());
//
//        // 数据包生成
//        IpV6Packet ipV6Packet = (IpV6Packet) ipv6PPacket.CraftPacket();
//        System.out.println(ipV6Packet);
//
//        EthernetPacket ethernetPacket = (EthernetPacket) ethernetPPacket.CraftPacket();
//        System.out.println(ethernetPacket);

        // TCP Test
        // 定义及初始化
        TcpPPacket tcpPPacket = new TcpPPacket();
        tcpPPacket.Initial();
        Ipv4PPacket ipv4PPacket = new Ipv4PPacket();
        ipv4PPacket.Initial();
        EthernetPPacket ethernetPPacket = new EthernetPPacket();
        ethernetPPacket.Initial();

        // 字段修改
        TcpPacket.Builder tcpBuilder = (TcpPacket.Builder) tcpPPacket.builder();  // 只有这种特殊情况才提取出 builder
        tcpBuilder.srcAddr(InetAddress.getByName(ipv4PPacket.getSrcAddr()))
                .dstAddr(InetAddress.getByName(ipv4PPacket.getDstAddr()));  // 这里其实没有影响

        // 上传载荷
        tcpPPacket.CraftBuilder();
        ipv4PPacket.CraftBuilder(tcpPPacket.builder());
        ethernetPPacket.CraftBuilder(ipv4PPacket.builder());

        // 数据包生成
        TcpPacket tcpPacket = (TcpPacket) tcpPPacket.CraftPacket();
        System.out.println(tcpPacket);

        IpV4Packet ipV4Packet = (IpV4Packet) ipv4PPacket.CraftPacket();
        System.out.println(ipV4Packet);

        EthernetPacket ethernetPacket = (EthernetPacket) ethernetPPacket.CraftPacket();
        System.out.println(ethernetPacket);

        // UDP Test
//        // 定义及初始化
//        UdpPPacket udpPPacket = new UdpPPacket();
//        udpPPacket.Initial();
//        Ipv4PPacket ipv4PPacket = new Ipv4PPacket();
//        ipv4PPacket.Initial();
//        EthernetPPacket ethernetPPacket = new EthernetPPacket();
//        ethernetPPacket.Initial();
//
//        // 字段修改
//        UdpPacket.Builder udpBuilder = (UdpPacket.Builder) udpPPacket.builder();  // 只有这种特殊情况才提取出 builder
//        udpBuilder.srcAddr(InetAddress.getByName(ipv4PPacket.getSrcAddr()))
//                .dstAddr(InetAddress.getByName(ipv4PPacket.getDstAddr()));  // 这里其实没有影响
//
//        // 上传载荷
//        udpPPacket.CraftBuilder();
//        ipv4PPacket.CraftBuilder(udpPPacket.builder());
//        ethernetPPacket.CraftBuilder(ipv4PPacket.builder());
//
//        // 数据包生成
//        UdpPacket udpPacket = (UdpPacket) udpPPacket.CraftPacket();
//        System.out.println(udpPacket);
//
//        IpV4Packet ipV4Packet = (IpV4Packet) ipv4PPacket.CraftPacket();
//        System.out.println(ipV4Packet);
//
//        EthernetPacket ethernetPacket = (EthernetPacket) ethernetPPacket.CraftPacket();
//        System.out.println(ethernetPacket);

        // todo dns,icmpv4,icmpv6-后期再弄


    }
}
