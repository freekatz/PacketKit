package pkit.core.base.packet;

public interface PacketGroup {
    String Id = null;
    String InterfaceId = null;
    String Name = null;
    String Description = null;
    String TimeStamp = null;
    int PacketNumber = 0;


    enum PacketGroupType {
        Send(0),
        Capture(1),
        // etc...
        ;

        private final int value;

        PacketGroupType(int value) { this.value = value; }

        public int getValue() { return this.value; }
    }

    void setId(String id);
    void setInterfaceId(String interfaceId);
    void setName(String name);
    void setDescription(String description);
    void setTimeStamp(String timeStamp);
    void setPacketNumber(int packetNumber);

    String getId();
    String getInterfaceId();
    String getName();
    String getDescription();
    String getTimeStamp();
    int getPacketNumber();

    void dump();
    void remove();

}
