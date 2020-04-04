package pkit.core.base.nif;

import org.pcap4j.core.*;
import org.pcap4j.util.LinkLayerAddress;
import pkit.core.base.config.Config;
import pkit.core.base.config.FilterConfig;
import pkit.core.base.config.NetworkInterfaceConfig;

import java.io.EOFException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public final class CaptureNetworkInterface implements NetworkInterface{
    private PcapHandle.Builder builder;  // 网卡构建对象, 通过 builder 设置网卡操作相关的字段
    public PcapHandle handle; // 默认 handle, 默认捕获全部数据包, 只加载部分配置
    public PcapDumper dumper;

    // information reference, static
    // update when construction
    private final int id;  // id 作为网卡的唯一不变的标识
    private final String name;  // 网卡名字, 形如 Device/...
    private final String easyName = "easyName";  // 网卡的别名, 如 WLAN 等
    private final String description;  // 网卡描述信息
    private final ArrayList<LinkLayerAddress> MacAddresses;  // 网卡物理地址数组
    private final PcapAddress IPv4Address;  // 网卡 IPv4 地址
    private final PcapAddress IPv6Address;  // 网卡 IPv6 地址
    private final boolean local; // 是否是本地接口
    private final boolean loopback; // 是否是回环网卡
    private final boolean running; // 是否运行, 这里的运行指的是在操作系统中的运行状态, 而不是程序中
    private final boolean up; // 是否打开, 同上

    // operator reference
    private NetworkInterfaceConfig networkInterfaceConfig;
    private FilterConfig filterConfig;


    // live circle
    private boolean activate;  // 是否激活
    private boolean load;  // 是否加载了配置
    private boolean start;  // 是否正在运行作业
    private boolean stop;  // 是否运行完毕

    // tmp path

    private String tpsPath;
    private String tpPath;
    private String logPath;

    // statistic reference
    // use trigger auto update
    // todo: 考虑将下面的内容放置到其它的类中进行统一管理
    public int sendPacketNumber = 0;  // 发送数据包总数, 指的是源 MAC 为本网卡的数据包
    public int receivePacketNumber = 0;  // 收到数据包总数, 指的是目的 MAC 为本网卡的数据包
    public int capturePacketNumber = 0;  // 捕获数据包总数, 在非嗅探模式下等于上面两字段的和
    public int lossPacketNumber = 0;  // 丢失数据包总数, 指的是由于缓冲区大小不足, 数据包有错等原因丢弃的数据包
    public double packetLossRate = 0;  // 丢包率, 上面两字段相除的百分比
    public int sendByteNumber = 0;  // 即上行带宽大小
    public int receiveByteNumber = 0;  // 即下行带宽大小
    public double bandwidth = 0;  // 上面两字段之和
    public int workTime = 0;  // 网卡工作时长, 指的是在程序中处于激活状态下的时长
    public int liveTime = 0;  // 网卡活跃时长, 指的是在程序中处于启动状态下的时长
    public double usingRate = 0;  // 使用率, 上面两字段相除的百分比

    public CaptureNetworkInterface(PcapNetworkInterface nif) {
        this.id = nif.hashCode(); // todo: 将这里修改为 hashCode()/n, n 为网卡总数, 将所有网卡依次存到一个 Hash 表中
        this.name = nif.getName();
//        this.easyName = this.getEasyName();  // todo: 获取 easyName
        this.description = nif.getDescription();
        this.MacAddresses = nif.getLinkLayerAddresses();
        this.IPv4Address = nif.getAddresses().get(1);  // todo: fit more cases
        this.IPv6Address = nif.getAddresses().get(0);
        this.local = nif.isLocal();
        this.loopback = nif.isLoopBack();
        this.running = nif.isRunning();
        this.up = nif.isUp();


    }

    @Override
    public void Initial() throws PcapNativeException, NotOpenException {
        this.activate = false;
        this.load = false;
        this.start = false;
        this.stop = false;
        this.networkInterfaceConfig = new NetworkInterfaceConfig();
        this.filterConfig = new FilterConfig();
        this.networkInterfaceConfig.Initial();
        this.filterConfig.Initial();

        this.sendPacketNumber = 0;
        this.receivePacketNumber = 0;
        this.capturePacketNumber = 0;
        this.lossPacketNumber = 0;
        this.packetLossRate = 0;
        this.sendByteNumber = 0;
        this.receiveByteNumber = 0;
        this.bandwidth = 0;
        this.workTime = 0;
        this.liveTime = 0;
        this.usingRate = 0;

    }

    @Override
    public void Activate() throws PcapNativeException {
        this.activate = true;
        this.builder = new PcapHandle.Builder(this.name);
        /*
        todo 缓冲区准备: tmp/id_date_size.tps
         */
        this.tpsPath = "tmp/tmp.tps";
        /*
        todo 临时文件准备: tmp/id_date.tp
         */
        /*
        todo 日志文件准备: log/id_date.log
         */
    }

    @Override
    public void Load() throws PcapNativeException, NotOpenException {
        this.load = true;

        this.builder.snaplen(this.networkInterfaceConfig.getSnapshotLength())
                .timeoutMillis(this.networkInterfaceConfig.getTimeoutMillis())
                .bufferSize(this.networkInterfaceConfig.getBufferSize())
                .promiscuousMode(this.networkInterfaceConfig.getPromiscuousMode())
                .timestampPrecision(this.networkInterfaceConfig.getTimestampPrecision());

        if (this.networkInterfaceConfig.getRfmonMode() == NetworkInterfaceMode.RfmonMode.RfmonMode)
                this.builder.rfmon(true);
        else this.builder.rfmon(false);

        if (this.networkInterfaceConfig.getImmediateMode() == NetworkInterfaceMode.ImmediateMode.ImmediateMode)
                this.builder.immediateMode(true);
        else this.builder.immediateMode(false);


        this.handle = this.builder.build();
        this.dumper = this.handle.dumpOpen(this.tpsPath);
    }


    @Override
    public void Start(){
        // warning: Start 中的包捕获模式一定是 OfflineMode, 因为磁盘缓冲区
        this.start = true;
        this.stop = false;

    }

    @Override
    public void Pause() {
        this.start = true;
        this.stop = true; // start 和 stop 同时为 true 时代表当前网卡作业处于暂停状态

    }

    @Override
    public void Resume() {
        this.start = true;
        this.stop = false;

    }

    @Override
    public void Stop() throws NotOpenException {
        this.start = false;
        this.stop = true;
        // 此处代码较多, 待完善
    }


    public void Capture(NetworkInterfaceMode.CaptureMode mode) throws PcapNativeException, InterruptedException, NotOpenException, EOFException, TimeoutException {
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

    public void Dump() {

    }

    public void setNetworkInterfaceConfig(NetworkInterfaceConfig networkInterfaceConfig) {
        this.networkInterfaceConfig = networkInterfaceConfig;
    }
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }
    public void setTpsPath(String path) {
        this.tpsPath = path;
    }
    public void setTpPath(String path) {
        this.tpPath = path;
    }
    public void setLogPathPath(String path) {
        this.logPath = path;
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
    public PcapAddress getIPv4Address(){
        return this.IPv4Address;
    }
    public PcapAddress getIPv6Address(){
        return this.IPv6Address;
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

    public NetworkInterfaceConfig getNetworkInterfaceConfig() {
        return this.networkInterfaceConfig;
    }
    public FilterConfig getFilterConfig() {
        return this.filterConfig;
    }

    public boolean isActivate() {
        return this.activate;
    }
    public boolean isLoad() {
        return load;
    }
    public boolean isStart() {
        return start;
    }
    public boolean isStop() {
        return stop;
    }

    public String getTpsPath() {
        return this.tpsPath;
    }
    public String getTpPath() {
        return this.tpPath;
    }
    public String getLogPath() {
        return this.logPath;
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

    @Override
    public String toString() {
        return "CaptureNetworkInterface{" +
                ", activate=" + activate +
                ", load=" + load +
                ", start=" + start +
                ", stop=" + stop +
                '}';
    }
}
