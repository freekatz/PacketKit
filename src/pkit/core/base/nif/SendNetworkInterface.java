package pkit.core.base.nif;

import org.pcap4j.core.*;
import org.pcap4j.util.LinkLayerAddress;
import pkit.core.base.config.Config;

import java.util.ArrayList;
import java.util.List;

public class SendNetworkInterface implements NetworkInterface {

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
    int count = 1; // 发送数目
    int timeoutMillis = 0; // 发送延迟
    PcapNetworkInterface destNif = null; // 应用于 Forward，转发的目的网卡，默认选择目的网卡属于同一子网的接口
    int retryCount = 1; // 重试次数，发送失败时使用
    // etc...

    public int sendPacketNumber = 0;  // 发送数据包总数, 单独统计, 与捕获网卡的字段相独立
    public int failPacketNumber = 0;  // 发送失败数据包总数


    SendNetworkInterface(PcapNetworkInterface nif) {
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
    public void Initial() throws PcapNativeException, NotOpenException {

    }

    @Override
    public void Activate() throws PcapNativeException {

    }

    @Override
    public void Load() throws PcapNativeException, NotOpenException {

    }

    @Override
    public void Modify(Config c) throws PcapNativeException, NotOpenException {

    }

    @Override
    public void Start() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Stop() throws NotOpenException {

    }

    void Send(){

    }

    void Resend(){

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

}
