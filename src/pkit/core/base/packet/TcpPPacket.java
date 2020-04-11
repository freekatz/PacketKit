package pkit.core.base.packet;

import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.namednumber.TcpPort;

import java.util.ArrayList;
import java.util.List;

public class TcpPPacket implements PPacket {
    private String name;
    private int length;
    private String srcPort;
    private String dstPort;
    private int sequenceNumber;
    private int acknowledgmentNumber;
    private byte dataOffset;
    private byte reserved;
    private boolean urg;
    private boolean ack;
    private boolean psh;
    private boolean rst;
    private boolean syn;
    private boolean fin;
    private short window;
    private short checksum;
    private short urgentPointer;
    private ArrayList<String> options;
    private byte[] padding;

    @Override
    public void Initial() {
        this.name = "tcp";
    }

    @Override
    public void Parse(PcapPacket pcapPacket) {
        TcpPacket.TcpHeader header = pcapPacket.get(TcpPacket.class).getHeader();
        this.length = header.length();
        this.srcPort = header.getSrcPort().valueAsString();
        this.dstPort = header.getDstPort().valueAsString();
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
        ArrayList<String> options = new ArrayList<>();
        header.getOptions().forEach(opt -> {
            options.add(opt.getKind().valueAsString());
        });
        this.padding = header.getPadding();
    }

    @Override
    public PcapPacket Craft() {
        return null;
    }

    @Override
    public void Dump(String path) {

    }

    public byte[] getPadding() {
        return padding;
    }

    public void setPadding(byte[] padding) {
        this.padding = padding;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public short getUrgentPointer() {
        return urgentPointer;
    }

    public void setUrgentPointer(short urgentPointer) {
        this.urgentPointer = urgentPointer;
    }

    public short getChecksum() {
        return checksum;
    }

    public void setChecksum(short checksum) {
        this.checksum = checksum;
    }

    public short getWindow() {
        return window;
    }

    public void setWindow(short window) {
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

    public int getAcknowledgmentNumber() {
        return acknowledgmentNumber;
    }

    public void setAcknowledgmentNumber(int acknowledgmentNumber) {
        this.acknowledgmentNumber = acknowledgmentNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getDstPort() {
        return dstPort;
    }

    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }

    public String getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(String srcPort) {
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
