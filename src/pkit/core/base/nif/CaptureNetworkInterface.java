package pkit.core.base.nif;

import org.pcap4j.core.*;
import org.pcap4j.util.LinkLayerAddress;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public final class CaptureNetworkInterface implements NetworkInterface{

    private PcapHandle.Builder builder = null;
    private PcapHandle handle = null;

    // information reference, static
    // update when construction
    private final int id; // 自编
    private final String name;
    private final String easyName = "easyName"; // 需要一个函数来获取
    private final String description;
    private final ArrayList<LinkLayerAddress> MacAddresses;
    private final List<PcapAddress> IPAddresses;
    private final boolean local; // 是否是本地接口
    private final boolean loopback; // 是否是回环网卡
    private final boolean running; // 是否运行
    private final boolean up; // 是否打开

    // operator reference
    boolean activate = false; // 是否激活(编程属性而非网卡实体属性), 当一个网卡的处理类被新建时设为 true
    int snapshotLength = 65536;
    int count = -1;
    PcapNetworkInterface.PromiscuousMode promiscuousMode = PcapNetworkInterface.PromiscuousMode.NONPROMISCUOUS;
    NetworkInterfaceMode.RfmonMode rfmonMode = NetworkInterfaceMode.RfmonMode.NoRfmonMode;
    NetworkInterfaceMode.OfflineMode offlineMode = NetworkInterfaceMode.OfflineMode.OnlineMode;
    int timeoutMillis = 0;
    int bufferSize = 2 * 1024 * 1024; // 2MB 缓冲大小
    PcapHandle.TimestampPrecision timestampPrecision = PcapHandle.TimestampPrecision.NANO;
    PcapHandle.PcapDirection direction = PcapHandle.PcapDirection.INOUT;
    NetworkInterfaceMode.ImmediateMode immediateMode = NetworkInterfaceMode.ImmediateMode.DelayMode;
    String filter = null;

    // statistic reference
    // use trigger auto update
    public int sendPacketNumber = 0; // 这个字段不需要在发送接口中定义，因为捕获接口可以捕获到本机发送的包，无需再次统计
    public int receivePacketNumber = 0;
    public int capturePacketNumber = 0;
    public int lossPacketNumber = 0;
    public double packetLossRate = 0;
    public int sendByteNumber = 0;
    public int receiveByteNumber = 0;
    public double bandwidth = 0;
    public int workTime = 0;
    public int liveTime = 0;
    public double usingRate = 0;

    private CaptureNetworkInterface(PcapNetworkInterface nif) {
        this.id = nif.hashCode();
        this.name = nif.getName();
//        this.easyName = this.getEasyName();
        this.description = nif.getDescription();
        this.MacAddresses = nif.getLinkLayerAddresses();
        this.IPAddresses = nif.getAddresses();
        this.local = nif.isLocal();
        this.loopback = nif.isLoopBack();
        this.running = nif.isRunning();
        this.up = nif.isUp();

    }

    @Override
    public void Activate() {
        this.activate = true;
        this.builder = new PcapHandle.Builder(this.name);
    }

    @Override
    public void Reactivate() {
        if (!this.activate)
            this.activate = true;
        if (this.builder != null)
            this.builder = null;
        this.builder = new PcapHandle.Builder(this.name);
    }

    @Override
    public void Load() throws PcapNativeException {
        // 此处代码较多, 待完善
        if (this.activate) {
            this.handle = this.builder.build();
        }
    }

    @Override
    public void Reload() throws PcapNativeException {
        // 此处代码较多, 待完善
        this.handle = this.builder.build();
    }

    @Override
    public void Edit() throws PcapNativeException {
        // 此处代码较多, 待完善
        this.handle = this.builder.build();
    }

    @Override
    public void Start() throws PcapNativeException, NotOpenException {
        if (this.handle != null)
            this.handle.setFilter(this.filter, BpfProgram.BpfCompileMode.OPTIMIZE);
        // 此处代码较多, 待完善
    }

    @Override
    public void Stop() {
        assert handle != null;
        handle.close();
        // 此处代码较多, 待完善
    }


    void Capture(NetworkInterfaceMode.CaptureMode mode) throws PcapNativeException, InterruptedException, NotOpenException, EOFException, TimeoutException {
        // 以下代码这是示例
        PacketListener listener;
        switch (mode){
            case LoopMode:
                 listener = System.out::println;
                this.handle.loop(5, listener);
                break;
            case HeavyLoopMode:
                listener =
                        packet -> {
                            System.out.println("start a heavy task");
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException ignored) {

                            }
                            System.out.println("done");
                        };

                try {
                    ExecutorService pool = Executors.newCachedThreadPool();
                    // 我们只需向 loop 函数传入 pool 即可, p4 作者已经将线程池的实现封装好
                    handle.loop(5, listener, pool); // This is better than handle.loop(5, listener);
                    pool.shutdown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            case GetNextPacketMode:
                this.handle.getNextPacket();
            case GetNextPacketExMode:
                this.handle.getNextPacketEx();
        }
    }

    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public String getEasyName(){
        return this.easyName;
    }
    public String getDescription(){
        return this.description;
    }
    public ArrayList<LinkLayerAddress> getMacAddresses(){
        return this.MacAddresses;
    }
    public List<PcapAddress> getIPAddresses(){
        return this.IPAddresses;
    }
    public boolean isLocal(){
        return this.local;
    }
    public boolean isLoopBack(){
        return this.loopback;
    }
    public boolean isRunning(){
        return this.running;
    }
    public boolean isUp(){
        return this.up;
    }
    public int getSendPacketNumber(){
        return this.sendPacketNumber;
    }
    public int getReceivePacketNumber(){
        return this.receivePacketNumber;
    }
    public int getCapturePacketNumber(){
        return this.capturePacketNumber;
    }
    public int getLossPacketNumber(){
        return this.lossPacketNumber;
    }
    public double getPacketLossRate(){
        return this.packetLossRate;
    }
    public int getSendByteNumber(){
        return this.sendByteNumber;
    }
    public int getReceiveByteNumber(){
        return this.receiveByteNumber;
    }
    public double getBandwidth(){
        return this.bandwidth;
    }
    public int getWorkTime(){
        return this.workTime;
    }
    public int getLiveTime(){
        return this.liveTime;
    }
    public double getUsingRate(){
        return this.usingRate;
    }
}
