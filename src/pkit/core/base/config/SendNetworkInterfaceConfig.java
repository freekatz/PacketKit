package pkit.core.base.config;

import org.pcap4j.core.PcapNetworkInterface;

public class SendNetworkInterfaceConfig implements Config{

    private int count; // 发送数目
    private int timeoutMillis; // 发送延迟
    private PcapNetworkInterface dstNif; // 应用于 Forward，转发的目的网卡，默认选择目的网卡属于同一子网的接口
    private int retryCount; // 重试次数，发送失败时使用


    @Override
    public void Initial() {
        this.count = 1;
        this.timeoutMillis = 0;
        this.dstNif = null;
        this.retryCount = 0;
    }

    @Override
    public SendNetworkInterfaceConfig clone() throws CloneNotSupportedException {
        return (SendNetworkInterfaceConfig) super.clone();
    }

    public int getCount() {
        return count;
    }
    public int getTimeoutMillis() {
        return timeoutMillis;
    }
    public PcapNetworkInterface getDstNif() {
        return dstNif;
    }
    public int getRetryCount() {
        return retryCount;
    }


    public void setCount(int count) {
        this.count = count;
    }
    public void setTimeoutMillis(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }
    public void setDstNif(PcapNetworkInterface dstNif) {
        this.dstNif = dstNif;
    }
    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public String toString() {
        return "SendNetworkInterfaceConfig{" +
                "count=" + count +
                ", timeoutMillis=" + timeoutMillis +
                ", dstNif=" + dstNif +
                ", retryCount=" + retryCount +
                '}';
    }
}
