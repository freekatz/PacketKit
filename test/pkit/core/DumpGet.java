package pkit.core;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.NifSelector;
import pkit.core.base.config.FilterConfig;
import pkit.core.base.config.NetworkInterfaceConfig;
import pkit.core.base.nif.CaptureNetworkInterface;

import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DumpGet {

    public static void main(String[] args) throws PcapNativeException, NotOpenException, CloneNotSupportedException {
        String tmpPath = "tmp/tmp.tps";

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
        captureNetworkInterface.Load();


        // 修改配置并保存
        captureNetworkInterface.getFilterConfig().setFilter("icmp");
        captureNetworkInterface.Load();

        // 修改配置不保存
        FilterConfig filterConfig = (FilterConfig) captureNetworkInterface.getFilterConfig().clone();
        filterConfig.setFilter("tcp or icmp");
        captureNetworkInterface.Modify(filterConfig);

        // todo 多线程实现: 在线模式缓冲区+离线模式
        

    }
}
