package pkit.core;

import org.pcap4j.core.*;
import pkit.core.base.packet.PacketHandle;
import pkit.util.JsonHandle;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PacketTest {

    public static void main(String[] args) throws PcapNativeException, IOException, TimeoutException, NotOpenException {

        String path = "tmp/tmp.tps";
        PcapHandle pcapHandle = Pcaps.openOffline(path);
        pcapHandle.setFilter("tcp", BpfProgram.BpfCompileMode.OPTIMIZE);
        PcapPacket pcapPacket = pcapHandle.getNextPacketEx();
        PacketHandle packetHandle = new PacketHandle(pcapPacket);
        packetHandle.Pipeline();
        JsonHandle jsonHandle = new JsonHandle();
        jsonHandle.Object2Json(new File("tmp/ph.json"), packetHandle);
    }
}
