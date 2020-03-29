package pkit.core.base.packet;

public class PacketOperator {
    // 包额外信息，需要添加到解析信息之中，组成一个数据包
    String Id = null;
    int CaptureLength = 0;
    int RealLength = 0;
    int LoadLength = 0;
    String SourceLocation = null;
    String DestinationLocation = null;
    String Abstract = null;
}
