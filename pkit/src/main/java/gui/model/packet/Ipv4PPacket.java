package gui.model.packet;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.pcap4j.core.*;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.IpV4Rfc1349Tos;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.*;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Ipv4PPacket implements PPacket{
    private IpV4Packet.Builder builder;
    private PcapHandle pcapHandle;
    private JsonMapper jsonMapper;
    private PcapDumper dumper;
    private String packetPath;  // todo 设置默认路径
    private String configPath;

    private String name;
    private int length;
    private byte version;
    private byte ihl;
    private byte tos;
    private short totalLength;
    private short identification;
    private boolean reservedFlag;
    private boolean dontFragmentFlag;
    private boolean moreFragmentFlag;
    private short fragmentOffset;
    private byte ttl;
    private byte protocol;
    private short headerChecksum;
    private String srcAddr;
    private String dstAddr;
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
        this.builder = new IpV4Packet.Builder();
        this.packetPath = "tmp/";
        this.configPath = "tmp/";

        this.name = "ipv4";
        this.length = 20;
        this.version = IpVersion.IPV4.value();
        this.ihl = 0;
        this.tos = 0;
        this.totalLength = 0;
        this.identification = 1;
        this.reservedFlag = false;
        this.dontFragmentFlag = false;
        this.moreFragmentFlag = false;
        this.fragmentOffset = 0;
        this.ttl = 64;
        this.protocol = IpNumber.TCP.value();
        this.headerChecksum = 0;
        this.srcAddr = "192.168.1.1";
        this.dstAddr = "127.0.0.1";
//        this.options = new ArrayList<>();
//        options.add(IpV4OptionType.TRACEROUTE.value());

        this.padding = new byte[0];
    }

    @Override
    public void Parse(PcapPacket pcapPacket) {
        IpV4Packet.IpV4Header header = pcapPacket.get(IpV4Packet.class).getHeader();
        this.length = header.length();
        this.version = header.getVersion().value();
        this.ihl = header.getIhl();
        this.tos = header.getTos().value();
        this.totalLength = header.getTotalLength();
        this.identification = header.getIdentification();
        this.reservedFlag = header.getReservedFlag();
        this.dontFragmentFlag = header.getDontFragmentFlag();
        this.moreFragmentFlag = header.getMoreFragmentFlag();
        this.fragmentOffset = header.getFragmentOffset();
        this.ttl = header.getTtl();
        this.protocol = header.getProtocol().value();
        this.headerChecksum = header.getHeaderChecksum();
        this.srcAddr = header.getSrcAddr().getHostAddress();
        this.dstAddr = header.getDstAddr().getHostAddress();
//        ArrayList<String> options = new ArrayList<>();
//        header.getOptions().forEach(opt->{
//            options.add(opt.getType().value());
//        });
//        this.options = options;
        this.padding = header.getPadding();
    }

    private String flag() {
        List<String> flags = new ArrayList<>();
        if (reservedFlag)
            flags.add("reserve");
        if (dontFragmentFlag)
            flags.add("don't");
        if (moreFragmentFlag)
            flags.add("more");
        return flags.toString();
    }

    @Override
    public String description() {
        return this.srcAddr + " to " + this.dstAddr
                + ", ttl=" + this.ttl
                + ", " + this.flag()
                + ", protocol is: " + IpNumber.getInstance(this.protocol);
    }

    @Override
    public void CraftBuilder() {
        this.builder.version(IpVersion.getInstance(this.version))
                .ihl(this.ihl)
                .totalLength(this.totalLength)
                .identification(this.identification)
                .reservedFlag(this.reservedFlag)
                .dontFragmentFlag(this.dontFragmentFlag)
                .moreFragmentFlag(this.moreFragmentFlag)
                .fragmentOffset(this.fragmentOffset)
                .ttl(this.ttl)
                .protocol(IpNumber.getInstance(this.protocol))
                .headerChecksum(this.headerChecksum);

        IpV4Rfc1349Tos.Builder tosBuilder = new IpV4Rfc1349Tos.Builder();
        IpV4TosPrecedence tosPrecedence = IpV4TosPrecedence.getInstance(this.tos);
        IpV4TosTos tosTos = IpV4TosTos.getInstance(this.tos);
        tosBuilder.precedence(tosPrecedence)
                .tos(tosTos)
                .mbz(true);
        this.builder.tos(tosBuilder.build());
        try {
            this.builder.srcAddr((Inet4Address) InetAddress.getByName(this.srcAddr));
            this.builder.dstAddr((Inet4Address) InetAddress.getByName(this.dstAddr));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        this.builder.padding(this.padding);

        // todo ipv4 option（种类太多）
//        ArrayList<IpV4Packet.IpV4Option> options = new ArrayList<>();
//        Ipv4Option
//        this.options.forEach(opt -> {
//            options.add(IpV4OptionType);
//        });
//        this.builder.options()

        this.builder.correctLengthAtBuild(true)
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

    public String getDstAddr() {
        return dstAddr;
    }

    public void setDstAddr(String dstAddr) {
        this.dstAddr = dstAddr;
    }

    public String getSrcAddr() {
        return srcAddr;
    }

    public void setSrcAddr(String srcAddr) {
        this.srcAddr = srcAddr;
    }

    public short getHeaderChecksum() {
        return headerChecksum;
    }

    public void setHeaderChecksum(short headerChecksum) {
        this.headerChecksum = headerChecksum;
    }

    public Byte getProtocol() {
        return protocol;
    }

    public void setProtocol(Byte protocol) {
        this.protocol = protocol;
    }

    public byte getTtl() {
        return ttl;
    }

    public void setTtl(byte ttl) {
        this.ttl = ttl;
    }

    public short getFragmentOffset() {
        return fragmentOffset;
    }

    public void setFragmentOffset(short fragmentOffset) {
        this.fragmentOffset = fragmentOffset;
    }

    public boolean isMoreFragmentFlag() {
        return moreFragmentFlag;
    }

    public void setMoreFragmentFlag(boolean moreFragmentFlag) {
        this.moreFragmentFlag = moreFragmentFlag;
    }

    public boolean isDontFragmentFlag() {
        return dontFragmentFlag;
    }

    public void setDontFragmentFlag(boolean dontFragmentFlag) {
        this.dontFragmentFlag = dontFragmentFlag;
    }

    public boolean isReservedFlag() {
        return reservedFlag;
    }

    public void setReservedFlag(boolean reservedFlag) {
        this.reservedFlag = reservedFlag;
    }

    public short getIdentification() {
        return identification;
    }

    public void setIdentification(short identification) {
        this.identification = identification;
    }

    public short getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(short totalLength) {
        this.totalLength = totalLength;
    }

    public byte getTos() {
        return tos;
    }

    public void setTos(byte tos) {
        this.tos = tos;
    }

    public byte getIhl() {
        return ihl;
    }

    public void setIhl(byte ihl) {
        this.ihl = ihl;
    }

    public Byte getVersion() {
        return version;
    }

    public void setVersion(Byte version) {
        this.version = version;
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
