package pkit.core.base.nif;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;

import java.util.List;

public interface NetworkInterface {


    // 此接口描述了一个网卡的生命周期

    /*
    激活此网卡, 激活状态下网卡才可以加载配置,
    结果是新建了一个 PcapHandle.Builder 对象
     */
    void Activate();
    /*
    重新激活此网卡
     */
    void Reactivate();
    /*
    从磁盘配置文件中加载用户网卡配置, 加载完毕之后网卡才可启动,
    结果是新建了一个 PcapHandle 对象, Load 一般只会运行一次
    此时网卡已经具备进行捕获发送等操作的能力
     */
    void Load() throws PcapNativeException;
    /*
    重新加载配置, 适用于修改且保存配置需要重新加载到网卡的情况,
    比如更新了过滤器, 如还想将本次更新的配置保存, 那么就需要使用 Reload
     */
    void Reload() throws PcapNativeException;
    /*
    修改此网卡的配置, 与上面 Load 和 Reload 不同之处在于,
    Edit 不通过磁盘文件为中介, 即需要加载的配置不在磁盘, 而在内存,
    适用于临时修改且不保存配置需要重新加载到网卡的情况,
    比如临时更新了过滤器, 如不想将本次更新的配置保存, 那么就需要使用 Edit
     */
    void Edit() throws PcapNativeException;
    /*
    启动一个网卡作业, 启动状态下用于实时确定, 更新及控制当前的操作逻辑,
    比如可能对同一批次的数据包进行多次过滤, 那么就会多次修改过滤器,
    则在调用 Reload 或 Edit 之后, Start 就会根据配置调用 Capture,
    然后根据 Capture 捕获的数据包进行后续步骤, 如控制抓包模式等等
    可以这样认为: 一次 Start 就是一次操作过程, Start 就是捕获功能的基础控制流程
    更高级的控制流程会在 service 包中定义实现
     */
    void Start() throws PcapNativeException, NotOpenException;
    /*
    停止一个网卡作业, 即结束一个操作过程, 对结果进行收尾工作, 如控制数据包的去向等等
     */
    void Stop();

}
