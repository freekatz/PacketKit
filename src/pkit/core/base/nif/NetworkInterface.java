package pkit.core.base.nif;

import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;

public class NetworkInterface implements Interface {
    @Override
    public void setSnapshotLength(int snapshotLength) {

    }

    @Override
    public void setCount(int count) {

    }

    @Override
    public void setPromiscuousMode(PcapNetworkInterface.PromiscuousMode promiscuousMode) {

    }

    @Override
    public void setRfmonMode(boolean rfmonMode) {

    }

    @Override
    public void setOfflineMode(boolean offlineMode) {

    }

    @Override
    public void setTimeoutMillis(int timeoutMillis) {

    }

    @Override
    public void setBufferSize(int bufferSize) {

    }

    @Override
    public void setTimestampPrecision(PcapHandle.TimestampPrecision timestampPrecision) {

    }

    @Override
    public void setDirection(PcapHandle.PcapDirection direction) {

    }

    @Override
    public void setImmediateMode(boolean immediateMode) {

    }

    @Override
    public void setFilter(String filter) {

    }

    @Override
    public String getMacAddress() {
        return null;
    }

    @Override
    public String getIPv4Address() {
        return null;
    }

    @Override
    public String getIPv6Address() {
        return null;
    }

    @Override
    public String getSubnetMask() {
        return null;
    }

    @Override
    public String getGateway() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getEasyName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public int getSendPacketNumber() {
        return 0;
    }

    @Override
    public int getReceivePacketNumber() {
        return 0;
    }

    @Override
    public int getCapturePacketNumber() {
        return 0;
    }

    @Override
    public int getLossPacketNumber() {
        return 0;
    }

    @Override
    public double getPacketLossRate() {
        return 0;
    }

    @Override
    public int getSendByteNumber() {
        return 0;
    }

    @Override
    public int getReceiveByteNumber() {
        return 0;
    }

    @Override
    public double getBandwidth() {
        return 0;
    }

    @Override
    public int getWorkTime() {
        return 0;
    }

    @Override
    public int getLiveTime() {
        return 0;
    }

    @Override
    public double getUseingRate() {
        return 0;
    }
}
