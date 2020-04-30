package model.group;

import org.pcap4j.core.*;

import java.io.EOFException;
import java.util.LinkedList;
import java.util.concurrent.TimeoutException;

public class CapturePacketGroup implements PacketGroup {


    private LinkedList<PcapPacket> onlinePacketGroup;
    private LinkedList<PcapPacket>  offlinePacketGroup;
    private LinkedList<PcapPacket> packetGroup;


    //  使用 LinkedHashMap 存储包映射

    @Override
    public void Initial() {
        this.offlinePacketGroup = new LinkedList<>();
        this.onlinePacketGroup = new LinkedList<>();
        this.packetGroup = new LinkedList<>();

    }

    @Override
    public void Add(PcapPacket packet) {
        //  更新在线组
        this.onlinePacketGroup.add(packet);

    }

    @Override
    public void Add(String path, BpfProgram bpfProgram) throws PcapNativeException, EOFException, TimeoutException, NotOpenException {
        //  更新离线组
        PcapHandle handle = Pcaps.openOffline(path);
        while (true) {
            try {
                PcapPacket packet = handle.getNextPacketEx();
                if (bpfProgram.applyFilter(packet)) {
                    this.offlinePacketGroup.add(packet);
                }
            } catch (EOFException e) {
                //  读取结束的工作
                System.out.println("End of file");
                break;
            } catch (TimeoutException e) {
                //  读取超时的工作
                System.out.println("Timed out");
                break;
            }
        }

        handle.close();

    }

    @Override
    public void Clear() {
        //  清空在线组
        //  todo 记录上次读取位置，减少磁盘 IO，如实现可不清空离线组
        this.onlinePacketGroup.clear();
        this.offlinePacketGroup.clear();  // 暂时清空
        this.packetGroup.clear();

    }

    @Override
    public CapturePacketGroup clone() throws CloneNotSupportedException {
        return (CapturePacketGroup) super.clone();
    }



    public LinkedList<PcapPacket> getPacketGroup() {
        // 在线组会直接覆盖离线组的重复元素，因此顺便保证了不丢包
        this.packetGroup.addAll(this.offlinePacketGroup);
        this.packetGroup.addAll(this.onlinePacketGroup);

        return this.packetGroup;
    }
}
