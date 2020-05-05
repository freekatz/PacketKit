package gui.model.packet;

import gui.model.browser.PacketProperty;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.Packet;

import java.io.IOException;

public interface PPacket {

    Packet.Builder builder();
    String name();
    void Initial();
    void Parse(PcapPacket pcapPacket);  // 将一个 pcap 包解析成 ppacket 对象
    void Parse(Packet packet);
    void ParseHandle(Packet.Header packetHeader);
//    void Parse(PacketProperty packetProperty); // 将一个 property 对象解析成 ppacket 对象
    String description();
    void CraftBuilder();  // 制作构建器
    void CraftBuilder(Packet.Builder builder);  // 向低层上传载荷的构建器
    Packet CraftPacket();  // 制作数据包
    void Dump(String filename) throws PcapNativeException, NotOpenException, IOException;  // 输入文件名，在默认目录下转储数据包二进制及数据包配置信息
}
