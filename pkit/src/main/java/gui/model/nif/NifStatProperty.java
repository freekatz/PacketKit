package gui.model.nif;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class NifStatProperty {
    public final SimpleIntegerProperty sendPacketNumber = new SimpleIntegerProperty(this, "spn");  // 发送数据包总数, 指的是源 MAC 为本网卡的数据包
    public final SimpleIntegerProperty receivePacketNumber = new SimpleIntegerProperty(this, "rpn");  // 收到数据包总数, 指的是目的 MAC 为本网卡的数据包
    public final SimpleIntegerProperty capturePacketNumber = new SimpleIntegerProperty(this, "cpn");  // 捕获数据包总数, 在非嗅探模式下等于上面两字段的和
    public final SimpleIntegerProperty lossPacketNumber = new SimpleIntegerProperty(this, "lpn");  // 丢失数据包总数, 指的是由于缓冲区大小不足, 数据包有错等原因丢弃的数据包
    public final SimpleDoubleProperty lossRate = new SimpleDoubleProperty(this, "lr");  // 丢包率, 上面两字段相除的百分比
    public final SimpleIntegerProperty sendByteNumber = new SimpleIntegerProperty(this, "sbn");  // 即上行带宽大小
    public final SimpleIntegerProperty receiveByteNumber = new SimpleIntegerProperty(this, "rbn");  // 即下行带宽大小
    public SimpleDoubleProperty bandwidth = new SimpleDoubleProperty(this, "bw");  // 上面两字段之和
    public final SimpleIntegerProperty workTime = new SimpleIntegerProperty(this, "wt");  // 网卡工作时长, 指的是在程序中处于激活状态下的时长
    public final SimpleIntegerProperty liveTime = new SimpleIntegerProperty(this, "lt");  // 网卡活跃时长, 指的是在程序中处于启动状态下的时长
    public SimpleDoubleProperty usingRate = new SimpleDoubleProperty(this, "ur");  // 使用率, 上面两字段相除的百分比
}
