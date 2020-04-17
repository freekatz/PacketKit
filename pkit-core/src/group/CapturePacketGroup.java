package pkit.core.base.group;

import org.pcap4j.core.*;

import java.io.EOFException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeoutException;

public class CapturePacketGroup implements PacketGroup {


    private LinkedHashMap<Integer, PcapPacket> onlinePacketGroup;
    private LinkedHashMap<Integer, PcapPacket>  offlinePacketGroup;
    private LinkedHashMap<Integer, PcapPacket> packetGroup;


    private String id;
    private String name;
    private String description;
    private Date timeStamp;
    private int size;

    //  使用 LinkedHashMap 存储包映射

    @Override
    public void Initial() {
        this.offlinePacketGroup = new LinkedHashMap<>();
        this.onlinePacketGroup = new LinkedHashMap<>();
        this.packetGroup = new LinkedHashMap<>();

        this.id = null;
        this.name = null;
        this.description = null;
        this.timeStamp = new Date();
        this.size = 0;

    }

    @Override
    public void Add(PcapPacket packet) {
        //  更新在线组
        this.size++;
        int index = this.size;
        this.onlinePacketGroup.put(index, packet);

    }

    @Override
    public void Add(String path, BpfProgram bpfProgram) throws PcapNativeException, EOFException, TimeoutException, NotOpenException {
        //  更新离线组
        PcapHandle handle = Pcaps.openOffline(path);
        while (true) {
            try {
                PcapPacket packet = handle.getNextPacketEx();
                if (bpfProgram.applyFilter(packet)) {
                    this.size++;
                    int index = this.size;
                    this.offlinePacketGroup.put(index, packet);
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
        this.size = 0;

    }

    @Override
    public CapturePacketGroup clone() throws CloneNotSupportedException {
        return (CapturePacketGroup) super.clone();
    }


    public void Dump(PcapDumper dumper) throws PcapNativeException, NotOpenException {

        this.packetGroup.forEach(((index, packet) -> {
            try {
                dumper.dump(packet);
            } catch (NotOpenException e) {
                e.printStackTrace();
            }
        }));

        dumper.close();

    }


    public LinkedHashMap<Integer, PcapPacket> getPacketGroup() {
        // 在线组会直接覆盖离线组的重复元素，因此顺便保证了不丢包
        this.packetGroup.putAll(this.offlinePacketGroup);
        this.packetGroup.putAll(this.onlinePacketGroup);

        return this.packetGroup;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public int getSize() {
        return size;
    }
}
