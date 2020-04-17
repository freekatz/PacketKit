package config;

import org.pcap4j.core.PcapHandle;

import java.util.Date;

// 动态配置
public class CaptureFilterConfig implements Config, Cloneable{
    private int id; // 程序中设置的，保存到文件，永久不变
    private String name;
    private String filter;
    private String comment;
    private Date timestamp;
    private PcapHandle.PcapDirection direction;  // todo 需检测平台是否支持


    @Override
    public void Initial() {
        this.timestamp = new Date();
        this.filter = "";
        this.direction = PcapHandle.PcapDirection.INOUT;
    }

    @Override
    public CaptureFilterConfig clone() throws CloneNotSupportedException {
        return (CaptureFilterConfig) super.clone();
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setFilter(String filter) {
        this.filter = filter;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public void setDirection(PcapHandle.PcapDirection direction) {
        this.direction = direction;
    }


    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public String getFilter() {
        return this.filter;
    }
    public String getComment() {
        return this.comment;
    }
    public Date getTimestamp() {
        return this.timestamp;
    }
    public PcapHandle.PcapDirection getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "FilterConfig{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", filter='" + filter + '\'' +
                ", comment='" + comment + '\'' +
                ", timestamp=" + timestamp +
                ", direction=" + direction +
                '}';
    }
}
