package pkit.core.service.config;

import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import pkit.core.base.nif.NetworkInterfaceMode;

import java.util.Date;

public class NetworkInterfaceConfig implements Config{


    private int id;
    private String name;
    private String comment;
    private Date date;
    private int snapshotLength;
    private int count;
    private int timeoutMillis;
    private int bufferSize; // 2MB 缓冲大小
    private PcapNetworkInterface.PromiscuousMode promiscuousMode;
    private NetworkInterfaceMode.RfmonMode rfmonMode;
    private NetworkInterfaceMode.OfflineMode offlineMode;
    private PcapHandle.TimestampPrecision timestampPrecision;
    private PcapHandle.PcapDirection direction;
    private NetworkInterfaceMode.ImmediateMode immediateMode ;

    @Override
    public void Initial() {
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

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSnapshotLength() {
        return snapshotLength;
    }

    public void setSnapshotLength(int snapshotLength) {
        this.snapshotLength = snapshotLength;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setTimeoutMillis(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public PcapNetworkInterface.PromiscuousMode getPromiscuousMode() {
        return promiscuousMode;
    }

    public void setPromiscuousMode(PcapNetworkInterface.PromiscuousMode promiscuousMode) {
        this.promiscuousMode = promiscuousMode;
    }

    public NetworkInterfaceMode.RfmonMode getRfmonMode() {
        return rfmonMode;
    }

    public void setRfmonMode(NetworkInterfaceMode.RfmonMode rfmonMode) {
        this.rfmonMode = rfmonMode;
    }

    public NetworkInterfaceMode.OfflineMode getOfflineMode() {
        return offlineMode;
    }

    public void setOfflineMode(NetworkInterfaceMode.OfflineMode offlineMode) {
        this.offlineMode = offlineMode;
    }

    public PcapHandle.TimestampPrecision getTimestampPrecision() {
        return timestampPrecision;
    }

    public void setTimestampPrecision(PcapHandle.TimestampPrecision timestampPrecision) {
        this.timestampPrecision = timestampPrecision;
    }

    public PcapHandle.PcapDirection getDirection() {
        return direction;
    }

    public void setDirection(PcapHandle.PcapDirection direction) {
        this.direction = direction;
    }

    public NetworkInterfaceMode.ImmediateMode getImmediateMode() {
        return immediateMode;
    }

    public void setImmediateMode(NetworkInterfaceMode.ImmediateMode immediateMode) {
        this.immediateMode = immediateMode;
    }
}
