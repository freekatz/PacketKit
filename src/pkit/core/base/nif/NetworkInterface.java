package pkit.core.base.nif;

import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;

public interface NetworkInterface {

    // information reference
    // update when construction
    String Id = null;
    String Name = null;
    String EasyName = null;
    String Description = null;
    String MacAddress = null;
    String IPv4Address = null;
    String IPv6Address = null;
    String SubnetMask = null;
    String Gateway = null;

    // operator reference
    int SnapshotLength = 65536;
    int Count = -1;
    PcapNetworkInterface.PromiscuousMode PromiscuousMode = PcapNetworkInterface.PromiscuousMode.NONPROMISCUOUS;
    NetworkInterfaceMode.RfmonMode RfmonMode = NetworkInterfaceMode.RfmonMode.NoRfmonMode;
    NetworkInterfaceMode.OfflineMode OfflineMode = NetworkInterfaceMode.OfflineMode.OnlineMode;
    int TimeoutMillis = 0;
    int BufferSize = 2 * 1024 * 1024;
    PcapHandle.TimestampPrecision TimestampPrecision = PcapHandle.TimestampPrecision.NANO;
    PcapHandle.PcapDirection Direction = PcapHandle.PcapDirection.INOUT;
    NetworkInterfaceMode.ImmediateMode ImmediateMode = NetworkInterfaceMode.ImmediateMode.DelayMode;
    String Filter = null;

    // statistic reference
    // use trigger auto update
    int SendPacketNumber = 0;
    int ReceivePacketNumber = 0;
    int CapturePacketNumber = 0;
    int LossPacketNumber = 0;
    double PacketLossRate = 0;
    int SendByteNumber = 0;
    int ReceiveByteNumber = 0;
    double Bandwidth = 0;
    int WorkTime = 0;
    int LiveTime = 0;
    double UsingRate = 0;


    void setId(String id);
    void setName(String name);
    void setEasyName(String easyName);
    void setDescription(String description);
    void setMacAddress(String MACAddress);
    void setIPv4Address(String IPv4Address);
    void setIPv6Address(String IPv6Address);
    void setSubnetMask(String subnetMask);
    void setGateway(String gateway);

    void setSnapshotLength(int snapshotLength);
    void setCount(int count);
    void setPromiscuousMode(PcapNetworkInterface.PromiscuousMode mode);
    void setRfmonMode(NetworkInterfaceMode.RfmonMode mode);
    void setOfflineMode(NetworkInterfaceMode.OfflineMode mode);
    void setTimeoutMillis(int timeoutMillis);
    void setBufferSize(int bufferSize);
    void setTimestampPrecision(PcapHandle.TimestampPrecision timestampPrecision);
    void setDirection(PcapHandle.PcapDirection direction);
    void setImmediateMode(NetworkInterfaceMode.ImmediateMode mode);
    void setFilter(String filter);

    void setSendPacketNumber(int sendPacketNumber);
    void setReceivePacketNumber(int receivePacketNumber);
    void setCapturePacketNumber(int capturePacketNumber);
    void setLossPacketNumber(int lossPacketNumber);
    void setPacketLossRate(double packetLossRate);
    void setSendByteNumber(int sendByteNumber);
    void setReceiveByteNumber(int receiveByteNumber);
    void setBandwidth(int bandwidth);
    void setWorkTime(int workTime);
    void setLiveTime(int liveTime);
    void setUsingRate(double usingRate);

    String getId();
    String getName();
    String getEasyName();
    String getDescription();
    String getMacAddress();
    String getIPv4Address();
    String getIPv6Address();
    String getSubnetMask();
    String getGateway();

    int getSendPacketNumber();
    int getReceivePacketNumber();
    int getCapturePacketNumber();
    int getLossPacketNumber();
    double getPacketLossRate();
    int getSendByteNumber();
    int getReceiveByteNumber();
    double getBandwidth();
    int getWorkTime();
    int getLiveTime();
    double getUsingRate();

    // 返回值未确定
    void start();
    void restart();
    void stop();
    void load();
    void reload();
    void edit();
    void capture();
    void send();
    void resend();
    void forward();

}
