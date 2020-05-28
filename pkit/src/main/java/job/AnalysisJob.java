package job;

import com.fasterxml.jackson.databind.json.JsonMapper;
import gui.model.SettingProperty;
import gui.model.analysis.*;
import gui.model.browser.PacketInfoProperty;
import gui.model.browser.PacketProperty;
import nif.CNIF;
import org.pcap4j.core.*;
import util.ChartHandle;
import util.PacketHandle;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class AnalysisJob implements Runnable {
    SettingProperty settingProperty = new SettingProperty();

    CNIF cnif;
    PcapStat pcapStat;

    long time = 0;
    long second = 0;
    int pn = 0;
//    int bn = 0;
    int dataLength = 0;
    IOLineProperty ioLineProperty;

    ProtocolPieProperty protocolPieProperty = new ProtocolPieProperty();
    Ipv4StatBarProperty ipv4StatBarProperty = new Ipv4StatBarProperty();
    Ipv6StatBarProperty ipv6StatBarProperty = new Ipv6StatBarProperty();
    SankeyProperty s2oSankeyProperty = new SankeyProperty(); // sankey 1
    SankeyProperty s2sSankeyProperty = new SankeyProperty(); // sankey 2
    SankeyProperty o2sSankeyProperty = new SankeyProperty(); // sankey 3
    SankeyProperty networkProperty = new SankeyProperty();

//    long bandwidth = 0;
//    NifStatProperty nifStatProperty = new NifStatProperty();



    public AnalysisJob (String pcapFile, PcapHandle handle) {
        cnif = new CNIF(pcapFile);
//        if (handle != null) {
//            try {
//                pcapStat = handle.getStats();
//                nifStatProperty.setName("nifStat");
//                nifStatProperty.setSendPacketNumber(pcapStat.getNumPacketsCaptured() - pcapStat.getNumPacketsReceived());
//                nifStatProperty.setReceivePacketNumber(pcapStat.getNumPacketsReceived());
//                nifStatProperty.setCapturePacketNumber(pcapStat.getNumPacketsCaptured());
//                nifStatProperty.setLossPacketNumber(pcapStat.getNumPacketsDropped() + pcapStat.getNumPacketsDroppedByIf());
//                nifStatProperty.setLossRate((double) nifStatProperty.getLossPacketNumber() / nifStatProperty.getCapturePacketNumber());
//            } catch (PcapNativeException | NotOpenException e) {
//                nifStatProperty.Initialize();
//            }
//        } else {
//            nifStatProperty.Initialize();
//        }
    }

    private void save() {
        JsonMapper mapper = new JsonMapper();
        try {
            mapper.writeValue(new File(settingProperty.ioLineChartJson), ioLineProperty);
            mapper.writeValue(new File(settingProperty.protocolPieChartJson), protocolPieProperty);
            mapper.writeValue(new File(settingProperty.ipv4StatBarChartJson), ipv4StatBarProperty);
            mapper.writeValue(new File(settingProperty.ipv6StatBarChartJson), ipv6StatBarProperty);
            mapper.writeValue(new File(settingProperty.s2oSankeyChartJson), s2oSankeyProperty);
            mapper.writeValue(new File(settingProperty.s2sSankeyChartJson), s2sSankeyProperty);
            mapper.writeValue(new File(settingProperty.o2sSankeyChartJson), o2sSankeyProperty);
            mapper.writeValue(new File(settingProperty.networkChartJson), networkProperty);
//            mapper.writeValue(new File(settingProperty.nifStatTableChartJson), nifStatProperty);
//
//            System.out.println(nifStatProperty.getName());
//            System.out.println(nifStatProperty.getSendPacketNumber());
//            System.out.println(nifStatProperty.getReceivePacketNumber());
//            System.out.println(nifStatProperty.getCapturePacketNumber());
//            System.out.println(nifStatProperty.getLossPacketNumber());
//            System.out.println(nifStatProperty.getLossRate());
//            System.out.println(nifStatProperty.getBandwidth());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int num=0;
        while (true) {
            try {

                PcapPacket packet = cnif.handle.getNextPacketEx();

//                if (nifStatProperty.getCapturePacketNumber()>0)
//                    bandwidth+=packet.getRawData().length;

                PacketProperty packetProperty = PacketHandle.Pipeline(packet);
                PacketInfoProperty packetInfoProperty = packetProperty.getInfo();
                packetInfoProperty.setNo(num);

                num++;
                // analysis
                // 1. io
                if (time == 0) {
                    time = Date.from(packet.getTimestamp()).getTime();
                    ioLineProperty = new IOLineProperty(time);
                }
                if (time > 0 && Date.from(packet.getTimestamp()).getTime() - time > 1000) {
                    second = (Date.from(packet.getTimestamp()).getTime() - time) / 1000;
                    dataLength = ((int) (dataLength + second)) + 1;
                    ioLineProperty.setDataLength(dataLength);
                    time = packet.getTimestamp().toEpochMilli();
                    ioLineProperty.getData().get(0).add(pn);
//                    ioLineProperty.getData().get(1).add(bn);
                    pn = 0;
//                    bn = 0;
                    for (int i = 0; i < second; i++) {
                        ioLineProperty.getData().get(0).add(0);
                        ioLineProperty.getData().get(1).add(0);
                    }
                }
                pn++;
//                bn = bn + packet.getRawData().length;

                // 2. pie
                if (!protocolPieProperty.getData().containsKey(packetInfoProperty.getProtocol()))
                    protocolPieProperty.getData().put(packetInfoProperty.getProtocol(), (double) 1);
                else {
                    double n = protocolPieProperty.getData().get(packetInfoProperty.getProtocol());
                    protocolPieProperty.getData().put(packetInfoProperty.getProtocol(), n + 1);
                }

                if (packetInfoProperty.getSrc().contains(".")) {
                    // communicate
                    String[] s2oOpt = {"192.168.0.0", "255.255.0.0", "0.0.0.0", "0.0.0.0"};  // 源网段、目的网段

                    ChartHandle.SankeyHandle(packetInfoProperty, s2oSankeyProperty, s2oOpt);

                    String[] s2sOpt = {"192.168.0.0", "255.255.0.0", "192.168.0.0", "255.255.0.0"};

                    ChartHandle.SankeyHandle(packetInfoProperty, s2sSankeyProperty, s2sOpt);

                    String[] o2sOpt = {"0.0.0.0", "0.0.0.0", "192.168.0.0", "255.255.0.0"};

                    ChartHandle.SankeyHandle(packetInfoProperty, o2sSankeyProperty, o2sOpt);

                    // network
                    String srcIp = packetInfoProperty.getSrc().split(":")[0];
                    String dstIp = packetInfoProperty.getDst().split(":")[0];
                    boolean ctrl;
                    ctrl = !packetInfoProperty.getProtocol().contains("IP");
                    if (ctrl) {

                        if (networkProperty.getData().containsKey(srcIp+":"+dstIp))
                            networkProperty.getData().put(srcIp+":"+dstIp, networkProperty.getData().get(srcIp+":"+dstIp)+packetInfoProperty.getLength());
                        else networkProperty.getData().put(srcIp+":"+dstIp, packetInfoProperty.getLength());

                    }

                    //3. bar
                    if (!ipv4StatBarProperty.getData().containsKey(srcIp))
                        ipv4StatBarProperty.getData().put(srcIp, ((double) packet.getOriginalLength()) / 1024);
                    else {
                        double n = ipv4StatBarProperty.getData().get(srcIp);
                        ipv4StatBarProperty.getData().put(srcIp, n + ((double) packet.getOriginalLength()) / 1024);
                    }
                } else if (packetInfoProperty.getSrc().split(":").length>6){

                    if (!ipv6StatBarProperty.getData().containsKey(packetInfoProperty.getSrc()))
                        ipv6StatBarProperty.getData().put(packetInfoProperty.getSrc(), ((double) packet.getOriginalLength()) / 1024);
                    else {
                        double n = ipv6StatBarProperty.getData().get(packetInfoProperty.getSrc());
                        ipv6StatBarProperty.getData().put(packetInfoProperty.getSrc(), n + ((double) packet.getOriginalLength()) / 1024);
                    }
                }
            } catch (EOFException e) {
                ioLineProperty.getData().get(0).add(dataLength, pn);
//                ioLineProperty.getData().get(1).add(dataLength, bn);
                dataLength++;

                int finalNum = num;
                protocolPieProperty.getData().keySet().forEach(key -> {
                    double n = protocolPieProperty.getData().get(key);
                    protocolPieProperty.getData().put(key, n / finalNum);
                });

                ipv4StatBarProperty.setData(SortByValueDescending(ipv4StatBarProperty.getData()));
                ipv6StatBarProperty.setData(SortByValueDescending(ipv6StatBarProperty.getData()));

//                nifStatProperty.setBandwidth(bandwidth);

                this.save();
                break;
            } catch (NotOpenException | TimeoutException | PcapNativeException ignored) {
            }

        }
    }

    //降序排序
    public static <K, V extends Comparable<? super V>> LinkedHashMap<K, V> SortByValueDescending(Map<K, V> map)
    {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        list.sort(new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return -compare;
            }
        });

        LinkedHashMap<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
