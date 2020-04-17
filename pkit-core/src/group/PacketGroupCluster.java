package pkit.core.base.group;

import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapPacket;
import pkit.core.base.packet.PacketHandle;

import java.util.LinkedHashMap;

public class PacketGroupCluster {

    //  对一个组实现各种聚集操作，操作都是静态的

    public static LinkedHashMap<PcapPacket, PacketHandle> Reproduce(BpfProgram bpfProgram, LinkedHashMap<PcapPacket, PacketHandle> packetGroup) throws PcapNativeException {
        //  todo 根据过滤器繁殖生成子数据包组
        LinkedHashMap<PcapPacket, PacketHandle> linkedHashMap = new LinkedHashMap<>();
        packetGroup.forEach(((packet, packetHandle) -> {
            if (bpfProgram.applyFilter(packet))
                linkedHashMap.put(packet, packetHandle);
        }));

        return linkedHashMap;
    }

}
