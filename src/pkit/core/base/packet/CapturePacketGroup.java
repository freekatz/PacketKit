package pkit.core.base.packet;

public class CapturePacketGroup implements PacketGroup {

    private String Id;
    private String InterfaceId;
    private String Name;
    private String Description;
    private String TimeStamp;
    private int PacketNumber;

    //  使用 LinkedHashMap 存储包映射

    public CapturePacketGroup() {

    }

    public void Dump() {
        //  转储当前组

    }

    void Reproduce(String filterExpression) {
        //  根据过滤器繁殖生成子数据包组

    }

    @Override
    public CapturePacketGroup clone() throws CloneNotSupportedException {
        return (CapturePacketGroup) super.clone();
    }

    @Override
    public void Add() {

    }

    @Override
    public void ForEach() {

    }
}
