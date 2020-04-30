package core;

import org.pcap4j.core.*;
import org.pcap4j.util.NifSelector;
import model.group.CapturePacketGroup;

import java.io.EOFException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeoutException;

public class Dump {

    public static void main(String[] args) throws PcapNativeException, NotOpenException, CloneNotSupportedException, InterruptedException, EOFException, TimeoutException, UnknownHostException {

        PcapNetworkInterface nif = null;
        try {
            nif = new NifSelector().selectNetworkInterface();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (nif == null)
            return;

//        CaptureNetworkInterface captureNetworkInterface = new CaptureNetworkInterface(nif);

//        // defaultHandle 创建完成, 字段初始化完毕
//        captureNetworkInterface.Initial();
//        // 加载配置
//        captureNetworkInterface.Activate();  // 资源分配完成
//        captureNetworkInterface.Load();  // 配置加载完成
//        // 修改配置
//        captureNetworkInterface.getCaptureNetworkInterfaceConfig().setCount(20);
//
//        System.out.println(captureNetworkInterface.isLoopBack());
//
//        CaptureFilterConfig captureFilterConfig = new CaptureFilterConfig();
//        captureFilterConfig.Initial();
//        captureFilterConfig.setFilter("icmp");
//        captureNetworkInterface.setCaptureFilterConfig(captureFilterConfig);
//
//        captureNetworkInterface.Start();
//        CapturePacketGroup packetGroup = captureNetworkInterface.packetGroup();
//        LinkedHashMap<Integer, PcapPacket> group = packetGroup.getPacketGroup();
//        group.forEach(((index, packet) -> System.out.println(packet)));
//
//        String path = "tmp/group.tps";
//        PcapDumper dumper = captureNetworkInterface.handle.dumpOpen(path);
//        packetGroup.Dump(dumper);

    }
}
