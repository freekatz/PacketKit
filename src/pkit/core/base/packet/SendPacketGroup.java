package pkit.core.base.packet;


import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapPacket;

import java.io.EOFException;
import java.util.concurrent.TimeoutException;

public class SendPacketGroup implements PacketGroup {


    // 包组存储与包组结构分离，建立结构文件，通过结构文件访问包组

    public SendPacketGroup() {

    }

    public void Remove() {

    }

    public void Get() {

    }

    @Override
    public SendPacketGroup clone() throws CloneNotSupportedException {
        return (SendPacketGroup) super.clone();
    }

    @Override
    public void Initial() {

    }

    @Override
    public void Add(PcapPacket packet) {

    }

    @Override
    public void Add(String path, BpfProgram bpfProgram) throws PcapNativeException, EOFException, TimeoutException, NotOpenException {

        // 根据过滤器批量导入
    }

    @Override
    public void Clear() {

    }

}
