package pkit.core.base.nif;

import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;

public interface Interface {

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
    boolean RfmonMode = false;
    boolean OfflineMode = false;
    int TimeoutMillis = 0;
    int BufferSize = 2 * 1024 * 1024;
    PcapHandle.TimestampPrecision TimestampPrecision = PcapHandle.TimestampPrecision.NANO;
    PcapHandle.PcapDirection Direction = PcapHandle.PcapDirection.INOUT;
    boolean ImmediateMode = false;
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
    double UseingRate = 0;


    void setSnapshotLength(int snapshotLength);
    void setCount(int count);
    void setPromiscuousMode(PcapNetworkInterface.PromiscuousMode promiscuousMode);
    void setRfmonMode(boolean rfmonMode);
    void setOfflineMode(boolean offlineMode);
    void setTimeoutMillis(int timeoutMillis);
    void setBufferSize(int bufferSize);
    void setTimestampPrecision(PcapHandle.TimestampPrecision timestampPrecision);
    void setDirection(PcapHandle.PcapDirection direction);
    void setImmediateMode(boolean immediateMode);
    void setFilter(String filter);

    String getMacAddress();
    String getIPv4Address();
    String getIPv6Address();
    String getSubnetMask();
    String getGateway();
    String getId();
    String getName();
    String getEasyName();
    String getDescription();

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
    double getUseingRate();

}
