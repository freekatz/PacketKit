package gui.model.packet;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.namednumber.DataLinkType;
import org.pcap4j.packet.namednumber.TcpPort;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TcpPPacket implements PPacket{
    private TcpPacket.Builder builder;
    private PcapHandle pcapHandle;
    private JsonMapper jsonMapper;
    private PcapDumper dumper;
    private String packetPath;  // todo 设置默认路径
    private String configPath;

    private String name;
    private int length;
    private int srcPort;
    private int dstPort;
    private long sequenceNumber;
    private long acknowledgmentNumber;
    private byte dataOffset;
    private byte reserved;
    private boolean urg;
    private boolean ack;
    private boolean psh;
    private boolean rst;
    private boolean syn;
    private boolean fin;
    private long window;
    private long checksum;
    private long urgentPointer;
//    private ArrayList<String> options;
    private byte[] padding;

    @Override
    public Packet.Builder builder() {
        return this.builder;
    }


    @Override
    public String name() {
        return this.name;
    }

    @Override
    public void Initial() {
        this.builder = new TcpPacket.Builder();
        this.packetPath = "tmp/";
        this.configPath = "tmp/";

        this.name = "tcp";
        this.srcPort = 1080;
        this.dstPort = 80;
        this.sequenceNumber = 1;
        this.acknowledgmentNumber = 1;
        this.dataOffset = 20;
        this.reserved = 0;
        this.urg = false;
        this.ack = false;
        this.psh = false;
        this.rst = false;
        this.syn = true;
        this.fin = false;
        this.window = 10000;
        this.checksum = 0;
        this.urgentPointer = 0;
        this.padding = new byte[0];
    }

    @Override
    public void Parse(PcapPacket pcapPacket) {
        TcpPacket.TcpHeader header = pcapPacket.get(TcpPacket.class).getHeader();
        this.length = header.length();
        this.srcPort = header.getSrcPort().valueAsInt();
        this.dstPort = header.getDstPort().valueAsInt();
        this.sequenceNumber = header.getSequenceNumber();
        this.acknowledgmentNumber = header.getAcknowledgmentNumber();
        this.dataOffset = header.getDataOffset();
        this.reserved = header.getReserved();
        this.urg = header.getUrg();
        this.ack = header.getAck();
        this.psh = header.getPsh();
        this.rst = header.getRst();
        this.syn = header.getSyn();
        this.fin = header.getFin();
        this.window = header.getWindow();
        this.checksum = header.getChecksum();
        this.urgentPointer = header.getUrgentPointer();
//        ArrayList<String> options = new ArrayList<>();
//        header.getOptions().forEach(opt -> {
//            options.add(opt.getKind().value());
//        });
        this.padding = header.getPadding();
    }

    private String flag() {
        List<String> flags = new ArrayList<>();
        if (urg)
            flags.add("urg");
        if (ack)
            flags.add("ack");
        if (psh)
            flags.add("psh");
        if (urg)
            flags.add("rst");
        if (ack)
            flags.add("syn");
        if (psh)
            flags.add("fin");
        return flags.toString();
    }

    @Override
    public String description() {
        return this.srcPort + "->" + this.dstPort
                + ", " + this.flag()
                + ", seq=" + this.sequenceNumber
                + ", ack=" + this.acknowledgmentNumber
                + ", win=" + this.window;
    }

    @Override
    public void CraftBuilder() {
        this.builder.srcPort(TcpPort.getInstance((short)this.srcPort))
                .dstPort(TcpPort.getInstance((short)dstPort))
                .sequenceNumber((int)this.sequenceNumber)
                .acknowledgmentNumber((int)this.acknowledgmentNumber)
                .dataOffset(this.dataOffset)
                .reserved(this.reserved)
                .urg(this.urg)
                .ack(this.ack)
                .psh(this.psh)
                .rst(this.rst)
                .syn(this.syn)
                .fin(this.fin)
                .window((short)this.window)
                .checksum((short)this.checksum)
                .urgentPointer((short)this.urgentPointer)
                .padding(this.padding)
                .correctLengthAtBuild(true)
                .correctChecksumAtBuild(true);

    }

    @Override
    public void CraftBuilder(Packet.Builder builder) {
        this.CraftBuilder();
        this.builder.payloadBuilder(builder);
    }


    @Override
    public Packet CraftPacket() {
        return this.builder.build();
    }

    @Override
    public void Dump(String filename) throws PcapNativeException, NotOpenException, IOException {
        this.pcapHandle = Pcaps.openDead(DataLinkType.EN10MB, 0);  // todo 链路类型自动适应
        this.jsonMapper = new JsonMapper();
        this.dumper = this.pcapHandle.dumpOpen(this.packetPath+filename+".pcap");

        this.dumper.dump(this.builder.build());
        this.jsonMapper.writeValue(new File(this.configPath+filename+".json"), this);
    }


    public byte[] getPadding() {
        return padding;
    }

    public void setPadding(byte[] padding) {
        this.padding = padding;
    }

//    public ArrayList<String> getOptions() {
//        return options;
//    }
//
//    public void setOptions(ArrayList<String> options) {
//        this.options = options;
//    }

    public long getUrgentPointer() {
        return urgentPointer;
    }

    public void setUrgentPointer(long urgentPointer) {
        this.urgentPointer = urgentPointer;
    }

    public long getChecksum() {
        return checksum;
    }

    public void setChecksum(long checksum) {
        this.checksum = checksum;
    }

    public long getWindow() {
        return window;
    }

    public void setWindow(long window) {
        this.window = window;
    }

    public boolean isFin() {
        return fin;
    }

    public void setFin(boolean fin) {
        this.fin = fin;
    }

    public boolean isSyn() {
        return syn;
    }

    public void setSyn(boolean syn) {
        this.syn = syn;
    }

    public boolean isRst() {
        return rst;
    }

    public void setRst(boolean rst) {
        this.rst = rst;
    }

    public boolean isPsh() {
        return psh;
    }

    public void setPsh(boolean psh) {
        this.psh = psh;
    }

    public boolean isAck() {
        return ack;
    }

    public void setAck(boolean ack) {
        this.ack = ack;
    }

    public boolean isUrg() {
        return urg;
    }

    public void setUrg(boolean urg) {
        this.urg = urg;
    }

    public byte getReserved() {
        return reserved;
    }

    public void setReserved(byte reserved) {
        this.reserved = reserved;
    }

    public byte getDataOffset() {
        return dataOffset;
    }

    public void setDataOffset(byte dataOffset) {
        this.dataOffset = dataOffset;
    }

    public long getAcknowledgmentNumber() {
        return acknowledgmentNumber;
    }

    public void setAcknowledgmentNumber(long acknowledgmentNumber) {
        this.acknowledgmentNumber = acknowledgmentNumber;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getDstPort() {
        return dstPort;
    }

    public void setDstPort(int dstPort) {
        this.dstPort = dstPort;
    }

    public int getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(int srcPort) {
        this.srcPort = srcPort;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
