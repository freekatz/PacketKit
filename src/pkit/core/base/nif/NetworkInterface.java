package pkit.core.base.nif;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;

import java.util.List;
// 此接口描述了一个网卡的生命周期, 以下操作均视为原子操作
public interface NetworkInterface {

    /*
    进行各种初始化工作，将网卡设为初始状态，
    原则上只涉及字段的初始化等，不涉及资源占用，
    包括但不限于：
        1. 将各种统计信息不加判断的清零
        2. 将网卡的各种操作属性设为默认值
        3. 将 builder 和 handle 设为 null
        4.
     */
    void Initial(); // 初始化字段和属性值

    /*
    激活网卡，进行各种发送或捕获之前的准备工作，注意与初始化工作相区分，
    准备工作会涉及到资源的占用，为加载网卡配置，进行捕获等做准备，
    包括但不限于：
        1. 将 activate 标记设为 true
        2. 根据网卡 name 新建 PcapHandle.Builder 对象
        3. 准备磁盘缓冲区，创建临时文件，分配缓冲和内存资源，创建需要的对象
        4.
     */
    void Activate(); // 分配资源

    /*
    重新激活此网卡，适用于重启网卡作业（Restart）或停止再重新运行或运行新的（Stop->Start）网卡作业的情况，
    与 Activate 功能差别不大，唯一不同的是，
    Reactivate 在重置网卡之前可进行更多的判断和处理，
    最后再调用 Activate 完成激活，判断处理包括但不限于：
        1. 判断及处理之前的缓冲区和临时文件
        2. 网卡是否需要重新初始化
        3. builder 对象是否正常
        4.
     */
    void Reactivate(); // 重新分配资源

    /*
    从磁盘配置文件中加载用户网卡配置, 根据配置设置网卡字段，
    新建 PcapHandle 对象, 此时网卡已经具备进行捕获发送等操作的能力
     */
    void Load() throws PcapNativeException; // 设置字段，从文件中加载

    /*
    重新加载配置, 适用于修改且保存配置，并需要重新加载到网卡的情况，会使用到配置相关的操作
    如暂停及恢复网卡作业（Pause->Resume）期间或停止再重新运行或运行新的（Stop->Start）网卡作业
     */
    void Reload() throws PcapNativeException; // 重新设置字段，并保存到文件

    /*
    重新加载配置, 适用于临时修改不保存配置，并需要重新加载到网卡的情况，不会使用到配置相关的操作，直接传入配置在内存中的映像，如 Json 对象
    比如暂停及恢复网卡作业（Pause->Resume）期间或停止再重新运行或运行新的（Stop->Start）网卡作业
     */
    void Modify() throws PcapNativeException; // 临时地重新设置字段，不保存

    /*
    开启并运行一个网卡作业, 调用相关操作，如 Capture、Send 等
    一次 Start 就是一次操作过程, 可以执行并控制这个过程
    更高级的控制流程会在 service 包中定义实现
     */
    void Start() throws PcapNativeException, NotOpenException; // 开启并控制一个网卡作业流程

    /*
    对当前的作业进行重启，同时会重置一切
     */
    void Restart(); // 重新开启并控制一个网卡作业流程

    /*
    暂停当前作业，实质上就是跳出作业执行的轮回，或是暂停作业的线程，
    暂停期间可修改网卡的配置，即进行 Reload 或 Edit 操作
     */
    void Pause(); // 暂停当前作业

    void Resume(); // 恢复当前作业

    /*
    停止一个网卡作业, 即结束一个操作过程
     */
    void Stop() throws NotOpenException; // 结束并控制一个网卡作业流程

}
