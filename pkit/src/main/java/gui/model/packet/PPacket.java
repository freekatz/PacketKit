package gui.model.packet;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.Packet;

import java.io.IOException;

public interface PPacket {

    Packet.Builder builder();
    String name();
    void Initial();
    void Parse(PcapPacket pcapPacket);
    String description();
    void CraftBuilder();  // 制作构建器，这个方法更加的灵活，适用于需要对各种载荷进行组合的复杂情况
    void CraftBuilder(Packet.Builder builder);
    Packet CraftPacket();  // 无载荷，制作数据包流，适用于制作无载荷数据包
    void Dump(String filename) throws PcapNativeException, NotOpenException, IOException;  // 输入文件名，在默认目录下转储数据包二进制及数据包配置信息
}
