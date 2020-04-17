package core;

import org.pcap4j.core.*;
import org.pcap4j.util.NifSelector;
import config.CaptureFilterConfig;
import nif.CaptureNetworkInterface;
import group.CapturePacketGroup;
import group.PacketGroup;

import java.io.EOFException;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeoutException;

public class Dump {

    public static void main(String[] args) throws PcapNativeException, NotOpenException, CloneNotSupportedException, InterruptedException, EOFException, TimeoutException {

        PcapNetworkInterface nif = null;
        try {
            nif = new NifSelector().selectNetworkInterface();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (nif == null)
            return;

        CaptureNetworkInterface captureNetworkInterface = new CaptureNetworkInterface(nif);

        // defaultHandle 创建完成, 字段初始化完毕
        captureNetworkInterface.Initial();
        // 加载配置
        captureNetworkInterface.Activate();  // 资源分配完成
        captureNetworkInterface.Load();  // 配置加载完成
        // 修改配置
        captureNetworkInterface.getCaptureNetworkInterfaceConfig().setCount(20);

        System.out.println(captureNetworkInterface.isLoopBack());

        CaptureFilterConfig captureFilterConfig = new CaptureFilterConfig();
        captureFilterConfig.Initial();
        captureFilterConfig.setFilter("ip src 192.168.2.114");
        captureNetworkInterface.setCaptureFilterConfig(captureFilterConfig);

        captureNetworkInterface.Start();
        ArrayList<PacketGroup> packetGroupArrayList = captureNetworkInterface.getPacketGroupArrayList();
        CapturePacketGroup packetGroup = (CapturePacketGroup) packetGroupArrayList.get(0);
        LinkedHashMap<Integer, PcapPacket> group = packetGroup.getPacketGroup();
        group.forEach(((index, packet) -> System.out.println(packet)));

        String path = "tmp/group.tps";
        PcapDumper dumper = captureNetworkInterface.handle.dumpOpen(path);
        packetGroup.Dump(dumper);

    }
}
