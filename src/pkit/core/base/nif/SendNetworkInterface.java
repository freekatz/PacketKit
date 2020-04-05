package pkit.core.base.nif;

import org.pcap4j.core.*;
import org.pcap4j.util.LinkLayerAddress;
import pkit.core.base.config.SendNetworkInterfaceConfig;

import java.util.ArrayList;
import java.util.List;

public class SendNetworkInterface implements NetworkInterface {

    private PcapHandle.Builder builder = null;
    private PcapHandle handle = null;

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

    public int sendPacketNumber;  // 发送数据包总数, 单独统计, 与捕获网卡的字段相独立
    public int failPacketNumber;  // 发送失败数据包总数


    SendNetworkInterface(PcapNetworkInterface nif) {
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
    public void Initial() throws PcapNativeException, NotOpenException {
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
    public void Activate() throws PcapNativeException {

    }

    @Override
    public void Load() throws PcapNativeException, NotOpenException {

    }

    @Override
    public void Start() {

    }


    @Override
    public void Stop() {

    }

    void Send(){

    }

    void Forward(){

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

}
