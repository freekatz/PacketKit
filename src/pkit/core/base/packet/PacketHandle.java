package pkit.core.base.packet;

import org.pcap4j.core.*;
import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.ArpOperation;
import org.pcap4j.packet.namednumber.NamedNumber;
import org.pcap4j.util.MacAddress;
import pkit.util.JsonHandle;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class PacketExtraInformation {
    private PcapPacket packet;
    // 包额外信息，需要添加到解析信息之中，组成一个数据包
    private String id;
    private int originLength ;  // 完整长度
    private int realLength;  // 完整长度
    private int payloadLength;  // 载荷长度
    private MacAddress sourceMac;
    private MacAddress destinationMac;
//    private String sourceVendor;  // 厂商
//    private String destinationVendor;
    private InetAddress sourceIp;
    private InetAddress destinationIp;
//    private String sourceLocation;  // 位置
//    private String destinationLocation;
    private String description;
    private NamedNumber type;  // 包类型：icmp、arp 等等
    private ArrayList<NamedNumber> typeList;
    private ArrayList<HashMap<String, String>> protocolList;
    private Instant timeStamp;  // 时间戳，可以是捕获时间也可以是修改或构造时间

    private PcapPacket.Builder builder;

    public PacketExtraInformation(PcapPacket packet) throws IOException {
        this.packet = packet;
        // 一个数据包对应一个信息
        this.originLength = packet.getOriginalLength();
        this.realLength = packet.length();
        this.payloadLength = packet.getPayload().length();
        this.sourceMac = packet.get(EthernetPacket.class).getHeader().getSrcAddr();
        this.destinationMac = packet.get(EthernetPacket.class).getHeader().getDstAddr();

        this.typeList = new ArrayList<>(4);
        this.protocolList = new ArrayList<>(4);
//        this.getList();
//        this.getType();
//        this.generateDescription();
//        this.Pipeline();

        this.timeStamp = packet.getTimestamp();
    }

    private void getType() {
        this.type = this.typeList.get(this.typeList.size()-1);
    }

    private void Pipeline() throws IOException {
        JsonHandle handle = new JsonHandle();
        if (this.packet.contains(EthernetPacket.class)) {
            EthernetPPacket pPacket = new EthernetPPacket();
            pPacket.Parse(this.packet);
            handle.Object2Json(new File("tmp/eth.json"), pPacket);
        }

        if (this.packet.contains(ArpPacket.class)) {
            ArpPPacket pPacket = new ArpPPacket();
            pPacket.Parse(this.packet);
            handle.Object2Json(new File("tmp/arp.json"), pPacket);
        }

        if (this.packet.contains(IpV4Packet.class)) {
            Ipv4PPacket pPacket = new Ipv4PPacket();
            pPacket.Parse(this.packet);
            handle.Object2Json(new File("tmp/ipv4.json"), pPacket);
        }


    }

    // todo tcp, udp, icmp 系列

    // todo dns


    public static void main(String[] args) throws PcapNativeException, NotOpenException {
        String filePath = "tmp/tmp.tps";
        PcapHandle handle = Pcaps.openOffline(filePath);

        handle.setFilter("ip src 125.39.132.162", BpfProgram.BpfCompileMode.OPTIMIZE);

        PcapPacket packet;
        while (true) {
            try {
                packet = handle.getNextPacketEx();
                System.out.println(packet.get(EthernetPacket.class).getHeader());
                System.out.println("Get: \n" + packet);
                PacketExtraInformation information = new PacketExtraInformation(packet);
                information.Pipeline();
                System.out.println(information.description);

            } catch (EOFException e) {
                System.out.println("End of file");
                break;
            } catch (TimeoutException | NotOpenException | IOException e) {
                System.out.println("Timed out");
                break;
            }
        }
    }
}
