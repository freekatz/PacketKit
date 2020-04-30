package util;

import com.fasterxml.jackson.databind.json.JsonMapper;
import gui.model.SettingProperty;
import gui.model.analysis.IOLineProperty;
import gui.model.analysis.Ipv4StatBarProperty;
import gui.model.analysis.Ipv6StatBarProperty;
import gui.model.analysis.ProtocolPieProperty;
import util.nif.CNIF;
import gui.ctrl.CaptureView;
import gui.model.CaptureProperty;
import gui.model.browser.PacketInfoProperty;
import gui.model.browser.PacketProperty;
import gui.model.Property;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import org.pcap4j.core.*;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class Job {

    public static class OnlineJob extends Task<PacketProperty> {
        CaptureProperty captureProperty;
        String filterExpression;
        CNIF cnif;
        TableView<Property> packetTable;
        TreeView<Property> headerTree;
        ListView<String> indexList;
        TextArea hexArea;
        TextArea txtArea;


        long time = 0;
        long second = 0;
        int pn = 0;
        int bn = 0;
        int dataLength = 0;
        IOLineProperty ioLineProperty;

        ProtocolPieProperty protocolPieProperty = new ProtocolPieProperty();
        Ipv4StatBarProperty ipv4StatBarProperty = new Ipv4StatBarProperty();
        Ipv6StatBarProperty ipv6StatBarProperty = new Ipv6StatBarProperty();

        public OnlineJob(CaptureView view) {
            captureProperty = view.getCaptureProperty();
            filterExpression = view.getFilterExpression();
            cnif = view.getCnif();
            packetTable = view.getPacketListCtrl().getPacketTable();
            headerTree = view.getPacketHeaderCtrl().getHeaderTree();
            indexList = view.getPacketDataCtrl().getIndexList();
            hexArea = view.getPacketDataCtrl().getHexArea();
            txtArea = view.getPacketDataCtrl().getTxtArea();
        }

        public OnlineJob(CaptureView view, CNIF cnif) {
            captureProperty = view.getCaptureProperty();
            filterExpression = view.getFilterExpression();
            this.cnif = cnif;
            packetTable = view.getPacketListCtrl().getPacketTable();
            headerTree = view.getPacketHeaderCtrl().getHeaderTree();
            indexList = view.getPacketDataCtrl().getIndexList();
            hexArea = view.getPacketDataCtrl().getHexArea();
            txtArea = view.getPacketDataCtrl().getTxtArea();
        }

        @Override
        protected void updateValue(PacketProperty packetProperty) {
            super.updateValue(packetProperty);
            if (packetProperty!=null) {
                packetTable.getItems().add(packetProperty.getInfo());
                // header and data
            }

        }


        private void save() {
            JsonMapper mapper = new JsonMapper();
            try {
                mapper.writeValue(new File(SettingProperty.ioLineChartJson), ioLineProperty);
                mapper.writeValue(new File(SettingProperty.protocolPieChartJson), protocolPieProperty);
                mapper.writeValue(new File(SettingProperty.ipv4StatBarChartJson), ipv4StatBarProperty);
                mapper.writeValue(new File(SettingProperty.ipv6StatBarChartJson), ipv6StatBarProperty);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected PacketProperty call() {
            boolean lop = false;
            int num = 0;
            if (captureProperty.getCount() < 0)
                lop = true;
            while (lop || num < captureProperty.getCount()) {
                try {
                    BpfProgram bpfProgram = cnif.handle.compileFilter(filterExpression, BpfProgram.BpfCompileMode.OPTIMIZE, (Inet4Address) InetAddress.getByName("255.255.255.255"));
                    PcapPacket packet = cnif.handle.getNextPacketEx();
                    cnif.dumper.dump(packet);

                    // analysis
                    // 1. io
                    if (time==0) {
                        time = Date.from(packet.getTimestamp()).getTime();
                        ioLineProperty = new IOLineProperty(time);
                    }
                    if (time>0 && Date.from(packet.getTimestamp()).getTime() - time>1000) {
                        second = (Date.from(packet.getTimestamp()).getTime() - time)/1000;
                        dataLength = ((int) (dataLength + second)) + 1;
                        ioLineProperty.setDataLength(dataLength);
                        time = packet.getTimestamp().toEpochMilli();
                        ioLineProperty.getData().get(0).add(pn);
                        ioLineProperty.getData().get(1).add(bn);
                        pn = 0;
                        bn = 0;
                        for (int i = 0; i < second; i++) {
                            ioLineProperty.getData().get(0).add(0);
                            ioLineProperty.getData().get(1).add(0);
                        }
                    }
                    pn++;
                    bn = bn + packet.getRawData().length;

                    PacketInfoProperty packetInfoProperty = PacketHandle.InfoPipeline(packet);
                    packetInfoProperty.setNo(num + 1);
                    PacketProperty packetProperty = new PacketProperty();
                    packetProperty.setInfo(packetInfoProperty);

                    // 2. pie
                    if (!protocolPieProperty.getData().containsKey(packetInfoProperty.getProtocol()))
                        protocolPieProperty.getData().put(packetInfoProperty.getProtocol(), (double) 1);
                    else {
                        double n = protocolPieProperty.getData().get(packetInfoProperty.getProtocol());
                        protocolPieProperty.getData().put(packetInfoProperty.getProtocol(), n + 1);
                    }
                    //3. bar
                    if (packetInfoProperty.getSrc().contains(".")) {
                        String ip = packetInfoProperty.getSrc().split(":")[0];
                        String port = packetInfoProperty.getSrc().split(":")[1];
                        if (!ipv4StatBarProperty.getData().containsKey(ip))
                            ipv4StatBarProperty.getData().put(ip, ((double) packet.getOriginalLength())/1024);
                        else {
                            double n = ipv4StatBarProperty.getData().get(ip);
                            ipv4StatBarProperty.getData().put(ip, n + ((double) packet.getOriginalLength())/1024);
                        }
                    } else {
                        if (!ipv6StatBarProperty.getData().containsKey(packetInfoProperty.getSrc()))
                            ipv6StatBarProperty.getData().put(packetInfoProperty.getSrc(), ((double) packet.getOriginalLength())/1024);
                        else {
                            double n = ipv6StatBarProperty.getData().get(packetInfoProperty.getSrc());
                            ipv6StatBarProperty.getData().put(packetInfoProperty.getSrc(), n + ((double) packet.getOriginalLength())/1024);
                        }
                    }

                    if (bpfProgram.applyFilter(packet)) {
                        // 对符合过滤器的包进行处理
                        System.out.println(packet);
                        this.updateValue(packetProperty);
                    }
                    num++;
                    this.save();
                } catch (EOFException | TimeoutException | UnknownHostException | PcapNativeException ignored) {
                } catch (NotOpenException e) {
                    ioLineProperty.getData().get(0).add(dataLength, pn);
                    ioLineProperty.getData().get(1).add(dataLength, bn);
                    dataLength++;

                    int finalNum = num;
                    protocolPieProperty.getData().keySet().forEach(key -> {
                        double n = protocolPieProperty.getData().get(key);
                        protocolPieProperty.getData().put(key, n/ finalNum);
                    });

                    ipv4StatBarProperty.setData(SortByValueDescending(ipv4StatBarProperty.getData()));
                    ipv6StatBarProperty.setData(SortByValueDescending(ipv6StatBarProperty.getData()));
                    this.save();
                    break;
                }
            }
            return null;
        }
    }

    public static class OfflineJob implements Runnable {

        int flag = 1;

        String filterExpression;
        CNIF cnif;
        TableView<Property> packetTable;
        TreeView<Property> headerTree;
        ListView<String> indexList;
        TextArea hexArea;
        TextArea txtArea;

        long time = 0;
        long second = 0;
        int pn = 0;
        int bn = 0;
        int dataLength = 0;
        IOLineProperty ioLineProperty;

        ProtocolPieProperty protocolPieProperty = new ProtocolPieProperty();
        Ipv4StatBarProperty ipv4StatBarProperty = new Ipv4StatBarProperty();
        Ipv6StatBarProperty ipv6StatBarProperty = new Ipv6StatBarProperty();


        public OfflineJob (CaptureView view) {
            filterExpression = view.getFilterExpression();
            this.cnif = view.getCnif();
            packetTable = view.getPacketListCtrl().getPacketTable();
            headerTree = view.getPacketHeaderCtrl().getHeaderTree();
            indexList = view.getPacketDataCtrl().getIndexList();
            hexArea = view.getPacketDataCtrl().getHexArea();
            txtArea = view.getPacketDataCtrl().getTxtArea();
        }

        public OfflineJob (CaptureView view, CNIF cnif) {
            filterExpression = view.getFilterExpression();
            this.cnif = cnif;
            flag = 0;
            packetTable = view.getPacketListCtrl().getPacketTable();
            headerTree = view.getPacketHeaderCtrl().getHeaderTree();
            indexList = view.getPacketDataCtrl().getIndexList();
            hexArea = view.getPacketDataCtrl().getHexArea();
            txtArea = view.getPacketDataCtrl().getTxtArea();
        }

        private void updateValue(PacketProperty packetProperty) {
            if (packetProperty!=null) {
                packetTable.getItems().add(packetProperty.getInfo());
                // header and data
            }
        }

        private void save() {
            JsonMapper mapper = new JsonMapper();
            try {
                String js1 = mapper.writeValueAsString(ipv4StatBarProperty);
                System.out.println(js1);
                String js2 = mapper.writeValueAsString(ipv6StatBarProperty);
                System.out.println(js2);
                mapper.writeValue(new File(SettingProperty.ioLineChartJson), ioLineProperty);
                mapper.writeValue(new File(SettingProperty.protocolPieChartJson), protocolPieProperty);
                mapper.writeValue(new File(SettingProperty.ipv4StatBarChartJson), ipv4StatBarProperty);
                mapper.writeValue(new File(SettingProperty.ipv6StatBarChartJson), ipv6StatBarProperty);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            int num=0;
            while (true) {
                try {
                    BpfProgram bpfProgram = cnif.handle.compileFilter(filterExpression, BpfProgram.BpfCompileMode.OPTIMIZE, (Inet4Address) InetAddress.getByName("255.255.255.255"));
                    PcapPacket packet = cnif.handle.getNextPacketEx();
                    num++;
                    if (flag==1) {
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
                            ioLineProperty.getData().get(1).add(bn);
                            pn = 0;
                            bn = 0;
                            for (int i = 0; i < second; i++) {
                                ioLineProperty.getData().get(0).add(0);
                                ioLineProperty.getData().get(1).add(0);
                            }
                        }
                        pn++;
                        bn = bn + packet.getRawData().length;
                    }

                    PacketInfoProperty packetInfoProperty = PacketHandle.InfoPipeline(packet);
                    packetInfoProperty.setNo(num + 1);
                    PacketProperty packetProperty = new PacketProperty();
                    packetProperty.setInfo(packetInfoProperty);

                    if (flag==1) {
                        // 2. pie
                        if (!protocolPieProperty.getData().containsKey(packetInfoProperty.getProtocol()))
                            protocolPieProperty.getData().put(packetInfoProperty.getProtocol(), (double) 1);
                        else {
                            double n = protocolPieProperty.getData().get(packetInfoProperty.getProtocol());
                            protocolPieProperty.getData().put(packetInfoProperty.getProtocol(), n + 1);
                        }

                        //3. bar
                        if (packetInfoProperty.getSrc().contains(".")) {
                            String ip = packetInfoProperty.getSrc().split(":")[0];
                            String port = packetInfoProperty.getSrc().split(":")[1];
                            if (!ipv4StatBarProperty.getData().containsKey(ip))
                                ipv4StatBarProperty.getData().put(ip, ((double) packet.getOriginalLength()) / 1024);
                            else {
                                double n = ipv4StatBarProperty.getData().get(ip);
                                ipv4StatBarProperty.getData().put(ip, n + ((double) packet.getOriginalLength()) / 1024);
                            }
                        } else {
                            if (!ipv6StatBarProperty.getData().containsKey(packetInfoProperty.getSrc()))
                                ipv6StatBarProperty.getData().put(packetInfoProperty.getSrc(), ((double) packet.getOriginalLength()) / 1024);
                            else {
                                double n = ipv6StatBarProperty.getData().get(packetInfoProperty.getSrc());
                                ipv6StatBarProperty.getData().put(packetInfoProperty.getSrc(), n + ((double) packet.getOriginalLength()) / 1024);
                            }
                        }
                    }
                    if (bpfProgram.applyFilter(packet)) {
                        // 对符合过滤器的包进行处理
                        this.updateValue(packetProperty);
                    }
                } catch (EOFException e) {
                    System.out.println("EOF");
                    if (flag==1) {
                        ioLineProperty.getData().get(0).add(dataLength, pn);
                        ioLineProperty.getData().get(1).add(dataLength, bn);
                        dataLength++;

                        int finalNum = num;
                        protocolPieProperty.getData().keySet().forEach(key -> {
                            double n = protocolPieProperty.getData().get(key);
                            protocolPieProperty.getData().put(key, n / finalNum);
                        });

                        ipv4StatBarProperty.setData(SortByValueDescending(ipv4StatBarProperty.getData()));
                        ipv6StatBarProperty.setData(SortByValueDescending(ipv6StatBarProperty.getData()));

                        this.save();
                    }

                    break;
                } catch (TimeoutException | UnknownHostException | NotOpenException | PcapNativeException ignored) {
                }
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
