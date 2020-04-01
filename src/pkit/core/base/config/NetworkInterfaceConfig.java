package pkit.core.base.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import pkit.core.base.nif.NetworkInterfaceMode;

import java.util.Date;

public class NetworkInterfaceConfig implements Config{


    private int id; // 程序中设置的，保存到文件，永久不变
    private String name;
    private String comment;
    private Date timestamp;
    private int snapshotLength;
    private int count;
    private int timeoutMillis;
    private int bufferSize; // 2MB 缓冲大小
    private PcapNetworkInterface.PromiscuousMode promiscuousMode;
    private NetworkInterfaceMode.RfmonMode rfmonMode;
    private NetworkInterfaceMode.OfflineMode offlineMode;
    private PcapHandle.TimestampPrecision timestampPrecision;
    private PcapHandle.PcapDirection direction;
    private NetworkInterfaceMode.ImmediateMode immediateMode;

    @Override
    public void Initial() {
        this.timestamp = new Date();
        this.snapshotLength = 65536;
        this.count = -1;
        this.timeoutMillis = 0;
        this.bufferSize = 2 * 1024 * 1024; // 2MB 缓冲大小
        this.promiscuousMode = PcapNetworkInterface.PromiscuousMode.NONPROMISCUOUS;
        this.rfmonMode = NetworkInterfaceMode.RfmonMode.NoRfmonMode;
        this.offlineMode = NetworkInterfaceMode.OfflineMode.OnlineMode;
        this.timestampPrecision = PcapHandle.TimestampPrecision.NANO;
        this.direction = PcapHandle.PcapDirection.INOUT;
        this.immediateMode = NetworkInterfaceMode.ImmediateMode.DelayMode;
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
    public int getSnapshotLength() {
        return snapshotLength;
    }
    public int getCount() {
        return count;
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
    public NetworkInterfaceMode.OfflineMode getOfflineMode() {
        return offlineMode;
    }
    public PcapHandle.TimestampPrecision getTimestampPrecision() {
        return timestampPrecision;
    }
    public PcapHandle.PcapDirection getDirection() {
        return direction;
    }
    public NetworkInterfaceMode.ImmediateMode getImmediateMode() {
        return immediateMode;
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
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public void setSnapshotLength(int snapshotLength) {
        this.snapshotLength = snapshotLength;
    }
    public void setCount(int count) {
        this.count = count;
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
    public void setOfflineMode(NetworkInterfaceMode.OfflineMode offlineMode) {
        this.offlineMode = offlineMode;
    }
    public void setTimestampPrecision(PcapHandle.TimestampPrecision timestampPrecision) {
        this.timestampPrecision = timestampPrecision;
    }
    public void setDirection(PcapHandle.PcapDirection direction) {
        this.direction = direction;
    }
    public void setImmediateMode(NetworkInterfaceMode.ImmediateMode immediateMode) {
        this.immediateMode = immediateMode;
    }
}
