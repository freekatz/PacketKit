package pkit.core.base.packet;

public class PacketExtraInformation {
    // 包额外信息，需要添加到解析信息之中，组成一个数据包
    private String Id;
    private int CaptureLength ;  // 完整长度
    private int RealLength;  // 载荷长度
    private int LoadLength;  // 完整长度
    private String SourceLocation;
    private String DestinationLocation;
    private String Abstract;

//    void setId(String id);
//    void setInterfaceId(String interfaceId);
//    void setName(String name);
//    void setDescription(String description);
//    void setTimeStamp(String timeStamp);
//    void setPacketNumber(int packetNumber);
//
//    String getId();
//    String getInterfaceId();
//    String getName();
//    String getDescription();
//    String getTimeStamp();
//    int getPacketNumber();
}
