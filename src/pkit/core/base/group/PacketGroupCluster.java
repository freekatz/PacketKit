package pkit.core.base.packet;

import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapPacket;

import java.util.LinkedHashMap;

public class PacketGroupCluster {

    //  对一个组实现各种聚集操作，操作都是静态的

    public static LinkedHashMap<PcapPacket, PacketExtraInformation> Reproduce(BpfProgram bpfProgram, LinkedHashMap<PcapPacket, PacketExtraInformation> packetGroup) throws PcapNativeException {
        //  todo 根据过滤器繁殖生成子数据包组
        LinkedHashMap<PcapPacket, PacketExtraInformation> linkedHashMap = new LinkedHashMap<>();
        packetGroup.forEach(((packet, packetExtraInformation) -> {
            if (bpfProgram.applyFilter(packet))
                linkedHashMap.put(packet, packetExtraInformation);
        }));

        return linkedHashMap;
    }

}
