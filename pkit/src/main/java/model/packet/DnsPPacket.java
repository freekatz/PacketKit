package model.packet;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.pcap4j.core.*;
import org.pcap4j.packet.DnsPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.DataLinkType;
import org.pcap4j.packet.namednumber.DnsOpCode;
import org.pcap4j.packet.namednumber.DnsRCode;

import java.io.File;
import java.io.IOException;

// todo 改正 DNS
public class DnsPPacket implements PPacket{
    private DnsPacket.Builder builder;
    private PcapHandle pcapHandle;
    private JsonMapper jsonMapper;
    private PcapDumper dumper;
    private String packetPath;  // todo 设置默认路径
    private String configPath;

    private String name;
    private int length;
    private short id;
    private boolean response;
    private byte opCode;
    private boolean authoritativeAnswer;
    private boolean truncated;
    private boolean recursionDesired;
    private boolean recursionAvailable;
    private boolean reserved;
    private boolean authenticData;
    private boolean checkingDisabled;
    private byte rCode;
    private short qdCount;
    private short anCount;
    private short nsCount;
    private short arCount;
//    private ArrayList<ArrayList<String>> questions;
//    private ArrayList<ArrayList<String>> answers;
//    private ArrayList<ArrayList<String>> authorities;
//    private ArrayList<ArrayList<String>> additionalInfo;

    @Override
    public Packet.Builder builder() {
        return this.builder;
    }

    @Override
    public void Initial() {
        this.builder = new DnsPacket.Builder();
        this.packetPath = "tmp/";
        this.configPath = "tmp/";

        this.name = "dns";
        this.length = 12;
        this.id = 0x0001;
        this.response = false;
        this.opCode = 0;
        this.authoritativeAnswer = false;
        this.truncated = false;
        this.recursionDesired = true;
        this.recursionAvailable = false;
        this.reserved = false;
        this.authenticData = false;
        this.checkingDisabled = false;
        this.rCode = 0;
        this.qdCount = 0;
        this.anCount = 0;
        this.nsCount = 0;
        this.arCount = 0;
//        this.questions = new ArrayList<>();
//        ArrayList<String> domainNameArrayList = new ArrayList<>();
//        domainNameArrayList.add("www.google.com");
//        DnsDomainName.Builder dnsDomainNameBuilder = new DnsDomainName.Builder();
//        dnsDomainNameBuilder.labels(domainNameArrayList);
//        DnsDomainName qName = dnsDomainNameBuilder.build();
//        DnsResourceRecordType qType = DnsResourceRecordType.A;
//        DnsClass qClass = DnsClass.IN;
//        DnsQuestion.Builder questionBuilder = new DnsQuestion.Builder();
//        questionBuilder.qName(qName)
//                .qType(qType)
//                .qClass(qClass);
//        this.questions.add(questionBuilder.build());
        //  pass the last field

    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public void Parse(PcapPacket pcapPacket) {
        DnsPacket.DnsHeader header = pcapPacket.get(DnsPacket.class).getHeader();
        this.length = header.length();
        this.id = header.getId();
        this.response = header.isResponse();
        this.opCode = header.getOpCode().value();
        this.authoritativeAnswer = header.isAuthoritativeAnswer();
        this.truncated = header.isTruncated();
        this.recursionDesired = header.isRecursionDesired();
        this.recursionAvailable = header.isRecursionAvailable();
        this.reserved = header.getReservedBit();
        this.authenticData = header.isAuthenticData();
        this.checkingDisabled = header.isCheckingDisabled();
        this.rCode = header.getrCode().value();
        this.qdCount = header.getQdCount();
        this.anCount = header.getAnCount();
        this.nsCount = header.getNsCount();
        this.arCount = header.getArCount();
//        this.questions = new ArrayList<>(header.getQuestions());
//        this.answers = new ArrayList<>(header.getAnswers());
//        this.authorities = new ArrayList<>(header.getAuthorities());
//        this.additionalInfo = new ArrayList<>(header.getAdditionalInfo());
    }

    @Override
    public String description() {
        return "";
    }

    @Override
    public void CraftBuilder() {
        this.builder.id(this.id)
                .response(this.response)
                .opCode(DnsOpCode.getInstance(this.opCode))
                .authoritativeAnswer(this.authoritativeAnswer)
                .truncated(this.truncated)
                .recursionDesired(this.recursionDesired)
                .recursionAvailable(this.recursionAvailable)
                .reserved(this.reserved)
                .authenticData(this.authenticData)
                .checkingDisabled(this.checkingDisabled)
                .rCode(DnsRCode.getInstance(this.rCode))
                .qdCount(this.qdCount)
                .anCount(this.anCount)
                .nsCount(this.nsCount)
                .arCount(this.arCount);
//                .questions(this.questions)
//                .answers(this.answers)
//                .authorities(this.authorities)
//                .additionalInfo(this.additionalInfo);

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
//
//    public ArrayList<DnsResourceRecord> getAdditionalInfo() {
//        return additionalInfo;
//    }
//
//    public void setAdditionalInfo(ArrayList<DnsResourceRecord> additionalInfo) {
//        this.additionalInfo = additionalInfo;
//    }
//
//    public ArrayList<DnsResourceRecord> getAuthorities() {
//        return authorities;
//    }
//
//    public void setAuthorities(ArrayList<DnsResourceRecord> authorities) {
//        this.authorities = authorities;
//    }
//
//    public ArrayList<DnsResourceRecord> getAnswers() {
//        return answers;
//    }
//
//    public void setAnswers(ArrayList<DnsResourceRecord> answers) {
//        this.answers = answers;
//    }
//
//    public ArrayList<DnsQuestion> getQuestions() {
//        return questions;
//    }
//
//    public void setQuestions(ArrayList<DnsQuestion> questions) {
//        this.questions = questions;
//    }

    public short getArCount() {
        return arCount;
    }

    public void setArCount(short arCount) {
        this.arCount = arCount;
    }

    public short getNsCount() {
        return nsCount;
    }

    public void setNsCount(short nsCount) {
        this.nsCount = nsCount;
    }

    public short getAnCount() {
        return anCount;
    }

    public void setAnCount(short anCount) {
        this.anCount = anCount;
    }

    public short getQdCount() {
        return qdCount;
    }

    public void setQdCount(short qdCount) {
        this.qdCount = qdCount;
    }

    public Byte getrCode() {
        return rCode;
    }

    public void setrCode(Byte rCode) {
        this.rCode = rCode;
    }

    public boolean isCheckingDisabled() {
        return checkingDisabled;
    }

    public void setCheckingDisabled(boolean checkingDisabled) {
        this.checkingDisabled = checkingDisabled;
    }

    public boolean isAuthenticData() {
        return authenticData;
    }

    public void setAuthenticData(boolean authenticData) {
        this.authenticData = authenticData;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public boolean isRecursionAvailable() {
        return recursionAvailable;
    }

    public void setRecursionAvailable(boolean recursionAvailable) {
        this.recursionAvailable = recursionAvailable;
    }

    public boolean isRecursionDesired() {
        return recursionDesired;
    }

    public void setRecursionDesired(boolean recursionDesired) {
        this.recursionDesired = recursionDesired;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public boolean isAuthoritativeAnswer() {
        return authoritativeAnswer;
    }

    public void setAuthoritativeAnswer(boolean authoritativeAnswer) {
        this.authoritativeAnswer = authoritativeAnswer;
    }

    public Byte getOpCode() {
        return opCode;
    }

    public void setOpCode(Byte opCode) {
        this.opCode = opCode;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
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
