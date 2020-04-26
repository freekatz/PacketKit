package model.group;

import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapPacket;

import java.io.EOFException;
import java.util.concurrent.TimeoutException;

public interface PacketGroup {

    void Initial();

    //  想组内添加一个数据包
    void Add(PcapPacket packet);

    void Add(String path, BpfProgram bpfProgram) throws PcapNativeException, EOFException, TimeoutException, NotOpenException;

    //  清空
    void Clear();

}
