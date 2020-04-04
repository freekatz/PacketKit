package pkit.core;

import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.NifSelector;
import pkit.core.base.config.FilterConfig;
import pkit.core.base.config.NetworkInterfaceConfig;
import pkit.core.base.nif.CaptureNetworkInterface;

import java.io.EOFException;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class DumpGet {

    public static void main(String[] args) throws PcapNativeException, NotOpenException, CloneNotSupportedException, InterruptedException {

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


        // 修改配置并保存
        captureNetworkInterface.getFilterConfig().setFilter("tcp");
        captureNetworkInterface.getNetworkInterfaceConfig().setCount(20);

        // 修改配置不保存
        FilterConfig filterConfig = (FilterConfig) captureNetworkInterface.getFilterConfig().clone();
        filterConfig.setFilter("tcp and ip src 192.168.2.114");

        BpfProgram bpfProgram = captureNetworkInterface.handle.compileFilter(filterConfig.getFilter(), BpfProgram.BpfCompileMode.OPTIMIZE, (Inet4Address) captureNetworkInterface.getIPv4Address().getNetmask());
        System.out.println(captureNetworkInterface.handle.getFilteringExpression()+"\n");
        System.out.println(filterConfig.getFilter());
        int num=0;
        while (num < captureNetworkInterface.getNetworkInterfaceConfig().getCount()) {
            PcapPacket packet = captureNetworkInterface.handle.getNextPacket(); // // 调用 getNextPacket 函数进行抓包, 一次得到一个包
            if (packet != null) {
                captureNetworkInterface.dumper.dump(packet); // 依次将数据包 Dump 到文件中
                if (bpfProgram.applyFilter(packet))
                    System.out.println(packet);
                num++;
            }
        }
        captureNetworkInterface.handle.close();
        captureNetworkInterface.dumper.close();

    }
}
