package pkit.core.base.nif;

import org.pcap4j.core.*;
import org.pcap4j.util.LinkLayerAddress;
import pkit.core.base.config.CaptureFilterConfig;
import pkit.core.base.config.CaptureNetworkInterfaceConfig;
import pkit.core.base.group.CapturePacketGroup;
import pkit.core.base.group.PacketGroup;

import java.io.EOFException;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public final class CaptureNetworkInterface implements NetworkInterface {
    private PcapHandle.Builder builder;  // 网卡构建对象, 通过 builder 设置网卡操作相关的字段
    public PcapHandle handle;  // handle, 默认捕获全部数据包
    public PcapDumper dumper;  // 用于存储到默认缓冲区
    private ArrayList<PacketGroup> packetGroupArrayList;
//    public PacketListener listener;  // 方案 1

    // 网卡的静态信息
    // 初始化时只更新一次
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

    // 操作配置
    private CaptureNetworkInterfaceConfig captureNetworkInterfaceConfig;
    private CaptureFilterConfig captureFilterConfig;


    // 生命周期
    private boolean activate;  // 是否激活
    private boolean load;  // 是否加载了配置
    private boolean start;  // 是否正在运行作业
    private boolean stop;  // 是否运行完毕

    // 资源路径
    private String tpsPath;
    private String tpPath;
    private String logPath;

    // 统计信息
    // todo: 使用触发器自动更新，考虑将下面的内容放置到其它的类中进行统一管理
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
    public void Initial(){
        this.builder = null;
        this.handle = null;
        this.packetGroupArrayList = new ArrayList<>();

        this.activate = false;
        this.load = false;
        this.start = false;
        this.stop = true;

        this.captureNetworkInterfaceConfig = new CaptureNetworkInterfaceConfig();
        this.captureFilterConfig = new CaptureFilterConfig();
        this.captureNetworkInterfaceConfig.Initial();
        this.captureFilterConfig.Initial();

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
    public void Activate(){
        this.activate = true;
        this.builder = new PcapHandle.Builder(this.name);

        CapturePacketGroup packetGroup = new CapturePacketGroup();
        packetGroup.Initial();
        this.packetGroupArrayList.add(packetGroup);
        /*
        缓冲区准备+文件名格式: tmp/id_date_size.tps
         */
        this.tpsPath = "tmp/tmp.tps";
        /*
        临时文件准备+文件名格式: tmp/id_date.tp，暂时用不上
         */
        /*
        todo 日志文件准备+文件名格式: log/id_date.log，暂时用不上
         */
    }

    @Override
    public void Load() throws PcapNativeException, NotOpenException {
        this.load = true;

        this.builder.snaplen(this.captureNetworkInterfaceConfig.getSnapshotLength())
                .timeoutMillis(this.captureNetworkInterfaceConfig.getTimeoutMillis())
                .bufferSize(this.captureNetworkInterfaceConfig.getBufferSize())
                .promiscuousMode(this.captureNetworkInterfaceConfig.getPromiscuousMode())
                .timestampPrecision(this.captureNetworkInterfaceConfig.getTimestampPrecision());

        if (this.captureNetworkInterfaceConfig.getRfmonMode() == NetworkInterfaceMode.RfmonMode.RfmonMode)
                this.builder.rfmon(true);
        else this.builder.rfmon(false);

        if (this.captureNetworkInterfaceConfig.getImmediateMode() == NetworkInterfaceMode.ImmediateMode.ImmediateMode)
                this.builder.immediateMode(true);
        else this.builder.immediateMode(false);


        this.handle = this.builder.build();
        this.dumper = this.handle.dumpOpen(this.tpsPath);
    }


    @Override
    public void Start() throws PcapNativeException, NotOpenException, EOFException, TimeoutException, InterruptedException {
        this.start = true;
        this.stop = false;

        this.Capture();
    }

    @Override
    public void Stop(){
        this.start = false;
        this.stop = true;
        if (this.handle.isOpen())
            this.handle.close();
        if (this.dumper.isOpen())
            this.dumper.close();
    }

    private void Capture() throws NotOpenException, PcapNativeException, EOFException, TimeoutException {
        // count < 0 时无限循环
        // 方案 1
//        this.listener = new PacketListener() {
//            @Override
//            public void gotPacket(PcapPacket pcapPacket) {
//                try {
//                    dumper.dump(pcapPacket);
//                    BpfProgram bpfProgram = handle.compileFilter(filterConfig.getFilter(), BpfProgram.BpfCompileMode.OPTIMIZE, (Inet4Address) IPv4Address.getNetmask());
//                    if (bpfProgram.applyFilter(pcapPacket))
//                        System.out.println(pcapPacket);
//                } catch (PcapNativeException | NotOpenException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        handle.loop(this.networkInterfaceConfig.getCount(), this.listener);
        // 方案 2
        BpfProgram bpfProgram = this.handle.compileFilter(captureFilterConfig.getFilter(), BpfProgram.BpfCompileMode.OPTIMIZE, (Inet4Address) this.getIPv4Address().getNetmask());
        boolean lop = false;
        int num = 0;
        if (this.captureNetworkInterfaceConfig.getCount()<0)
            lop = true;
        while (lop || num < this.captureNetworkInterfaceConfig.getCount()){
            PcapPacket packet = this.handle.getNextPacketEx();
            this.dumper.dump(packet); // 依次将数据包 Dump 到文件中
            if (bpfProgram.applyFilter(packet)) {  // todo 将此判断过程得到的 packet 送入在线数据包组
                // 将 packet 送入其它模块: a 过程完成
                // 同时也可直接对临时文件进行读写，必须使用 getNextPacket，因为写入过程得缓冲区无 EOF
                CapturePacketGroup packetGroup = (CapturePacketGroup) this.packetGroupArrayList.get(0);
                packetGroup.Add(packet);
                //  todo 界面实时更新在这里
            }
            num++;
        }
    }

    public void setCaptureNetworkInterfaceConfig(CaptureNetworkInterfaceConfig captureNetworkInterfaceConfig) {
        this.captureNetworkInterfaceConfig = captureNetworkInterfaceConfig;
    }
    public void setCaptureFilterConfig(CaptureFilterConfig captureFilterConfig) {
        this.captureFilterConfig = captureFilterConfig;
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

    public ArrayList<PacketGroup> getPacketGroupArrayList() {
        return packetGroupArrayList;
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

    public CaptureNetworkInterfaceConfig getCaptureNetworkInterfaceConfig() {
        return this.captureNetworkInterfaceConfig;
    }
    public CaptureFilterConfig getCaptureFilterConfig() {
        return this.captureFilterConfig;
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
                "activate=" + activate +
                ", load=" + load +
                ", start=" + start +
                ", stop=" + stop +
                '}';
    }
}
