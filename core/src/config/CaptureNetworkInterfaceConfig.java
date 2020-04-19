package config;

import nif.NetworkInterfaceMode;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;

import java.util.Date;

// 静态配置
public class CaptureNetworkInterfaceConfig implements Config, Cloneable{


    private int id; // 程序中设置的，保存到文件，永久不变
    private String name;
    private String comment;
    private Date timestamp;

    private int count;  // 并不是网卡属性和字段, 用于控制抓包数量, -1 则表示忽略 count
    private int snapshotLength;
    private int timeoutMillis;
    private int bufferSize; // 2MB 缓冲大小
    private PcapNetworkInterface.PromiscuousMode promiscuousMode;
    private NetworkInterfaceMode.RfmonMode rfmonMode;
    private PcapHandle.TimestampPrecision timestampPrecision;
    private NetworkInterfaceMode.ImmediateMode immediateMode;

    private CaptureFilterConfig filterConfig;



    @Override
    public void Initial() {
        this.timestamp = new Date(); // todo 这个需要修改
        this.count = 0;
        this.snapshotLength = 65536;
        this.timeoutMillis = 0;
        this.bufferSize = 20 * 1024 * 1024; // 20MB 缓冲大小
        this.promiscuousMode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;
        this.rfmonMode = NetworkInterfaceMode.RfmonMode.NoRfmonMode;  // todo 需检测是否是无线网卡及是否支持
        this.timestampPrecision = PcapHandle.TimestampPrecision.MICRO;  // todo 需检测平台是否支持
        this.immediateMode = NetworkInterfaceMode.ImmediateMode.ImmediateMode;
        this.filterConfig = new CaptureFilterConfig();
        this.filterConfig.Initial();
    }

    @Override
    public CaptureNetworkInterfaceConfig clone() throws CloneNotSupportedException {
        return (CaptureNetworkInterfaceConfig) super.clone();
    }


    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public void setSnapshotLength(int snapshotLength) {
        this.snapshotLength = snapshotLength;
    }
    public void setTimeoutMillis(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
    public void setPromiscuousMode(PcapNetworkInterface.PromiscuousMode promiscuousMode) {
        this.promiscuousMode = promiscuousMode;
    }
    public void setRfmonMode(NetworkInterfaceMode.RfmonMode rfmonMode) {
        this.rfmonMode = rfmonMode;
    }
    public void setTimestampPrecision(PcapHandle.TimestampPrecision timestampPrecision) {
        this.timestampPrecision = timestampPrecision;
    }
    public void setImmediateMode(NetworkInterfaceMode.ImmediateMode immediateMode) {
        this.immediateMode = immediateMode;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getComment() {
        return comment;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getTimestamp() {
        return timestamp;
    }

    public int getCount() {
        return count;
    }
    public int getSnapshotLength() {
        return snapshotLength;
    }
    public int getTimeoutMillis() {
        return timeoutMillis;
    }
    public int getBufferSize() {
        return bufferSize;
    }
    public PcapNetworkInterface.PromiscuousMode getPromiscuousMode() {
        return promiscuousMode;
    }
    public NetworkInterfaceMode.RfmonMode getRfmonMode() {
        return rfmonMode;
    }
    public PcapHandle.TimestampPrecision getTimestampPrecision() {
        return timestampPrecision;
    }
    public NetworkInterfaceMode.ImmediateMode getImmediateMode() {
        return immediateMode;
    }

    public CaptureFilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void setFilterConfig(CaptureFilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public String toString() {
        return "CaptureNetworkInterfaceConfig{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", timestamp=" + timestamp +
                ", count=" + count +
                ", snapshotLength=" + snapshotLength +
                ", timeoutMillis=" + timeoutMillis +
                ", bufferSize=" + bufferSize +
                ", promiscuousMode=" + promiscuousMode +
                ", rfmonMode=" + rfmonMode +
                ", timestampPrecision=" + timestampPrecision +
                ", immediateMode=" + immediateMode +
                ", filterConfig=" + filterConfig +
                '}';
    }
}
