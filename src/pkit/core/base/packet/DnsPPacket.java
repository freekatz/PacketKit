package pkit.core.base.packet;

import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.DnsPacket;
import org.pcap4j.packet.DnsQuestion;
import org.pcap4j.packet.DnsResourceRecord;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.namednumber.DnsOpCode;
import org.pcap4j.packet.namednumber.DnsRCode;

import java.util.ArrayList;
import java.util.List;

public class DnsPPacket implements PPacket {
    private String name;
    private int length;
    private short id;
    private boolean response;
    private String opCode;
    private boolean authoritativeAnswer;
    private boolean truncated;
    private boolean recursionDesired;
    private boolean recursionAvailable;
    private boolean reserved;
    private boolean authenticData;
    private boolean checkingDisabled;
    private String rCode;
    private short qdCount;
    private short anCount;
    private short nsCount;
    private short arCount;
    private ArrayList<String > questions;
    private ArrayList<String> answers;
    private ArrayList<String > authorities;
    private ArrayList<String > additionalInfo;

    @Override
    public void Initial() {
        this.name = "dns";
    }

    @Override
    public void Parse(PcapPacket pcapPacket) {
        DnsPacket.DnsHeader header = pcapPacket.get(DnsPacket.class).getHeader();
        this.length = header.length();
        this.id = header.getId();
        this.response = header.isResponse();
        this.opCode = header.getOpCode().valueAsString();
        this.authoritativeAnswer = header.isAuthoritativeAnswer();
        this.truncated = header.isTruncated();
        this.recursionDesired = header.isRecursionDesired();
        this.recursionAvailable = header.isRecursionAvailable();
        this.reserved = header.getReservedBit();
        this.authenticData = header.isAuthenticData();
        this.checkingDisabled = header.isCheckingDisabled();
        this.rCode = header.getrCode().valueAsString();
        this.qdCount = header.getQdCount();
        this.anCount = header.getAnCount();
        this.nsCount = header.getNsCount();
        this.arCount = header.getArCount();
        ArrayList<String> questions = new ArrayList<>();
        header.getQuestions().forEach(qst -> {
            questions.add(qst.getQClass().valueAsString());
        });
        this.questions = questions;
        ArrayList<String> answers = new ArrayList<>();
        header.getAnswers().forEach(asw -> {
            answers.add(asw.getDataClass().valueAsString());
        });
        this.answers = answers;
        ArrayList<String> authorities = new ArrayList<>();
        header.getAuthorities().forEach(auth -> {
            authorities.add(auth.getDataClass().valueAsString());
        });
        this.authorities = authorities;
        ArrayList<String> additionalInfo = new ArrayList<>();
        header.getAdditionalInfo().forEach(adi -> {
            additionalInfo.add(adi.getDataClass().valueAsString());
        });
        this.additionalInfo = additionalInfo;

    }

    @Override
    public PcapPacket Craft() {
        return null;
    }

    @Override
    public void Dump(String path) {

    }

    public ArrayList<String> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(ArrayList<String> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public ArrayList<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(ArrayList<String> authorities) {
        this.authorities = authorities;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<String> questions) {
        this.questions = questions;
    }

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

    public String getrCode() {
        return rCode;
    }

    public void setrCode(String rCode) {
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

    public String getOpCode() {
        return opCode;
    }

    public void setOpCode(String opCode) {
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
