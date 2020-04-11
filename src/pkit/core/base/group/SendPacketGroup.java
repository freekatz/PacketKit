package pkit.core.base.packet;


import org.pcap4j.core.*;

import java.io.EOFException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeoutException;

public class SendPacketGroup implements PacketGroup {

    private LinkedHashMap<Integer, PcapPacket> packetGroup;
    private PcapPacket packet;  // 活动数据包，即将要发送的数据包

    // 用于控制活动数据包的参数，
    // modify 为 true 代表数据包是之前没有的或进行了修改
    // 为 false 代表数据包是从文件读取的
    private boolean modify;


    private String id;
    private String name;
    private String description;
    private Date timeStamp;
    private int size;


    // 包组存储与包组结构分离，建立结构文件，通过结构文件访问包组

    @Override
    public SendPacketGroup clone() throws CloneNotSupportedException {
        return (SendPacketGroup) super.clone();
    }

    @Override
    public void Initial() {
        this.packetGroup = new LinkedHashMap<>();
        this.packet = null;

        this.modify = false;

        this.id = null;
        this.name = null;
        this.description = null;
        this.timeStamp = new Date();
        this.size = 0;

    }

    @Override
    public void Add(PcapPacket packet) {
        this.size++;
        int index = this.size;
        this.packetGroup.put(index, packet);
    }

    @Override
    public void Add(String path, BpfProgram bpfProgram) throws PcapNativeException, EOFException, TimeoutException, NotOpenException {
        // 根据过滤器批量导入
        PcapHandle handle = Pcaps.openOffline(path);
        while (true) {
            try {
                PcapPacket packet = handle.getNextPacketEx();
                if (bpfProgram.applyFilter(packet)) {
                    this.size++;
                    int index = this.size;
                    this.packetGroup.put(index, packet);
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
        this.packetGroup.clear();
        //  todo 文件删除操作及目录结构重写操作

    }


    public void Remove(int index) {
        this.packetGroup.remove(index);
        //  todo 文件删除操作及目录结构重写操作
    }

    public void Get(int index) {
        this.packetGroup.get(index);
        //  todo 文件新建操作及目录结构重写操作
    }

    public void setPacket(PcapPacket packet) {
        this.packet = packet;
        // 判断是否已包括，因此要注意在组中选择活动数据包时，如对其进行了修改，需要手动将 modify 置为 true
        // 此处只适合新建和重放的清况
        this.modify = !this.packetGroup.containsValue(packet);
    }

    public LinkedHashMap<Integer, PcapPacket> getPacketGroup() {
        return this.packetGroup;
    }

    public PcapPacket getPacket() {
        return packet;
    }

    public boolean isModify() {
        return modify;
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
