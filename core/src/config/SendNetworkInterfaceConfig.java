package config;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.pcap4j.core.PcapNetworkInterface;

import java.util.Date;

public class SendNetworkInterfaceConfig implements Config{


    private int id; // 程序中设置的，保存到文件，永久不变
    private String name;
    private String comment;
    private Date timestamp;


    private int count; // 发送数目
    private int retryCount; // 重试次数，发送失败时使用
    private int timeoutMillis; // 发送延迟
    private String dstNif; // 应用于 Forward，转发的目的网卡，默认选择目的网卡属于同一子网的接口
    // 目标主机信息等等，在构造包时设置，不属于网卡可控的内容


    @Override
    public void Initial() {
        this.timestamp = new Date();
        this.count = 1;
        this.timeoutMillis = 0;
        this.dstNif = null;
        this.retryCount = 0;
    }

    @Override
    public SendNetworkInterfaceConfig clone() throws CloneNotSupportedException {
        return (SendNetworkInterfaceConfig) super.clone();
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
    public int getTimeoutMillis() {
        return timeoutMillis;
    }
    public String getDstNif() {
        return dstNif;
    }
    public int getRetryCount() {
        return retryCount;
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
    public void setTimeoutMillis(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }
    public void setDstNif(String  dstNif) {
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
