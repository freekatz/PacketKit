package nif;

import org.pcap4j.core.*;
import org.pcap4j.util.LinkLayerAddress;
import config.SendNetworkInterfaceConfig;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class SendNetworkInterface implements NetworkInterface {
    private PcapHandle.Builder builder;
    private PcapHandle handle;
    private PcapPacket packet;


    private final int id;
    private final String name;
    private final String easyName = "easyName";
    private final String description;
    private final ArrayList<LinkLayerAddress> MacAddresses;
    private final PcapAddress IPv4Address;  // 网卡 IPv4 地址
    private final PcapAddress IPv6Address;  // 网卡 IPv6 地址
    private final boolean local; // 是否是本地接口
    private final boolean loopback; // 是否是回环网卡
    private final boolean running; // 是否运行
    private final boolean up; // 是否打开

    // 操作配置
    private SendNetworkInterfaceConfig sendNetworkInterfaceConfig;


    // 生命周期
    private boolean activate;  // 是否激活
    private boolean load;  // 是否加载了配置
    private boolean start;  // 是否正在运行作业
    private boolean stop;  // 是否运行完毕

    // 资源路径
    private String tpsPath;
    private String tpPath;
    private String logPath;

    public int sendPacketNumber;  // 发送数据包总数, 单独统计, 与捕获网卡的字段相独立
    public int failPacketNumber;  // 发送失败数据包总数


    public SendNetworkInterface(PcapNetworkInterface nif) {
        this.id = nif.hashCode();
        this.name = nif.getName();
//        this.easyName = this.getEasyName();
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
        this.handle = null;
        this.packet = null;

        this.activate = false;
        this.load = false;
        this.start = false;
        this.stop = true;

        this.sendNetworkInterfaceConfig = new SendNetworkInterfaceConfig();
        this.sendNetworkInterfaceConfig.Initial();

        this.sendPacketNumber = 0;
        this.failPacketNumber = 0;
    }

    @Override
    public void Activate() {
        this.activate = true;
        this.builder = new PcapHandle.Builder(this.name);
        /*
        缓冲区准备+文件名格式: tmp/id_date_size.tps
         */
        /*
        临时文件准备+文件名格式: tmp/id_date.tp，暂时用不上
         */
        this.tpPath = "tmp/tmp.tp";
        /*
        todo 日志文件准备+文件名格式: log/id_date.log，暂时用不上
         */
    }

    @Override
    public void Load() throws PcapNativeException {
        this.load = true;
        this.handle = this.builder.build();

        try {
            this.packet = Pcaps.openOffline(this.tpPath).getNextPacketEx();
        } catch (NotOpenException | TimeoutException | EOFException | PcapNativeException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void Start() {
        this.start = true;
        this.stop = false;

        this.Send();
    }


    @Override
    public void Stop() {
        this.stop = true;
        this.start = false;

        this.handle.close();

    }

    void Send(){
        try {
            for (int i=0; i<this.sendNetworkInterfaceConfig.getCount(); ++i) {
                Thread.sleep(this.sendNetworkInterfaceConfig.getTimeoutMillis());
                this.handle.sendPacket(this.packet);
            } // todo 发送失败重试次数实现
        } catch (NotOpenException | PcapNativeException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    void Forward(){
        // todo 将数据包直接转发到本地其它网卡
    }

    public void setSendNetworkInterfaceConfig(SendNetworkInterfaceConfig sendNetworkInterfaceConfig) {
        this.sendNetworkInterfaceConfig = sendNetworkInterfaceConfig;
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

    public SendNetworkInterfaceConfig getSendNetworkInterfaceConfig() {
        return this.sendNetworkInterfaceConfig;
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

    public int getSendPacketNumber() {
        return sendPacketNumber;
    }

    public int getFailPacketNumber() {
        return failPacketNumber;
    }
}
