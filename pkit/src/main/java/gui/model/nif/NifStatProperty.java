package gui.model.nif;

import gui.model.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class NifStatProperty implements Property {
    public SimpleStringProperty name = new SimpleStringProperty(this, "name");
    public SimpleLongProperty sendPacketNumber = new SimpleLongProperty(this, "spn");  // 发送数据包总数, 指的是源 MAC 为本网卡的数据包
    public SimpleLongProperty receivePacketNumber = new SimpleLongProperty(this, "rpn");  // 收到数据包总数, 指的是目的 MAC 为本网卡的数据包
    public SimpleLongProperty capturePacketNumber = new SimpleLongProperty(this, "cpn");  // 捕获数据包总数, 在非嗅探模式下等于上面两字段的和
    public SimpleLongProperty lossPacketNumber = new SimpleLongProperty(this, "lpn");  // 丢失数据包总数, 指的是由于缓冲区大小不足, 数据包有错等原因丢弃的数据包
    public SimpleDoubleProperty lossRate = new SimpleDoubleProperty(this, "lr");  // 丢包率, 上面两字段相除的百分比
    public SimpleLongProperty bandwidth = new SimpleLongProperty(this, "bw");  // 带宽之和


    public void Initialize() {
        setSendPacketNumber(0);
        setReceivePacketNumber(0);
        setCapturePacketNumber(0);
        setLossPacketNumber(0);
        setLossRate(0);
        setBandwidth(0);
    }

    public long getBandwidth() {
        return bandwidth.get();
    }

    public SimpleLongProperty bandwidthProperty() {
        return bandwidth;
    }

    public void setBandwidth(long bandwidth) {
        this.bandwidth.set(bandwidth);
    }

    public double getLossRate() {
        return lossRate.get();
    }

    public SimpleDoubleProperty lossRateProperty() {
        return lossRate;
    }

    public void setLossRate(double lossRate) {
        this.lossRate.set(lossRate);
    }

    public long getLossPacketNumber() {
        return lossPacketNumber.get();
    }

    public SimpleLongProperty lossPacketNumberProperty() {
        return lossPacketNumber;
    }

    public void setLossPacketNumber(long lossPacketNumber) {
        this.lossPacketNumber.set(lossPacketNumber);
    }

    public long getCapturePacketNumber() {

        return capturePacketNumber.get();
    }

    public SimpleLongProperty capturePacketNumberProperty() {
        return capturePacketNumber;
    }

    public void setCapturePacketNumber(long capturePacketNumber) {
        this.capturePacketNumber.set(capturePacketNumber);
    }

    public long getSendPacketNumber() {
        return sendPacketNumber.get();
    }

    public SimpleLongProperty sendPacketNumberProperty() {
        return sendPacketNumber;
    }

    public void setSendPacketNumber(long sendPacketNumber) {
        this.sendPacketNumber.set(sendPacketNumber);
    }

    public long getReceivePacketNumber() {
        return receivePacketNumber.get();
    }

    public SimpleLongProperty receivePacketNumberProperty() {
        return receivePacketNumber;
    }

    public void setReceivePacketNumber(long receivePacketNumber) {
        this.receivePacketNumber.set(receivePacketNumber);
    }

    @Override
    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
