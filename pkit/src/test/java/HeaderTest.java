//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.json.JsonMapper;
//import gui.model.browser.FieldProperty;
//import gui.model.browser.PacketProperty;
//import gui.model.packet.EthernetPPacket;
//import gui.model.packet.Ipv4PPacket;
//import gui.model.packet.UdpPPacket;
//import javafx.scene.control.TreeTableView;
//import org.pcap4j.packet.EthernetPacket;
//import org.pcap4j.packet.IpV4Packet;
//import org.pcap4j.packet.UdpPacket;
//import util.PacketHandle;
//import util.ViewHandle;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
//public class HeaderTest {
//    public static void main(String[] args) throws UnknownHostException, JsonProcessingException {
//
//        JsonMapper mapper = new JsonMapper();
//
//        // UDP Test
//        // 定义及初始化
//        UdpPPacket udpPPacket = new UdpPPacket();
//        udpPPacket.Initial();
//        udpPPacket.setSrcPort(65502);
//        Ipv4PPacket ipv4PPacket = new Ipv4PPacket();
//        ipv4PPacket.Initial();
////        ipv4PPacket.setPadding(new byte[16]);
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
//
//        String udp = mapper.writeValueAsString(udpPPacket);
//        System.out.println(udp);
//
//        String ip = mapper.writeValueAsString(ipv4PPacket);
//        System.out.println(ip);
//
//        String eth = mapper.writeValueAsString(ethernetPPacket);
//        System.out.println(eth);
//
//    }
//
//}
