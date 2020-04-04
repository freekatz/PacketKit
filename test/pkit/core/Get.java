package pkit.core;

import org.pcap4j.core.*;
import org.pcap4j.util.NifSelector;
import pkit.core.base.config.FilterConfig;
import pkit.core.base.nif.CaptureNetworkInterface;

import java.io.EOFException;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class Get {

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
        captureNetworkInterface.getNetworkInterfaceConfig().setCount(25);
        FilterConfig filterConfig = new FilterConfig();
        filterConfig.Initial();
        filterConfig.setFilter("icmp and ip src 192.168.2.114");
        captureNetworkInterface.setFilterConfig(filterConfig);


        PcapHandle handle = Pcaps.openOffline(captureNetworkInterface.getTpsPath());
        handle.setFilter(filterConfig.getFilter(), BpfProgram.BpfCompileMode.OPTIMIZE);

        PacketListener listener = System.out::println;
        handle.loop(-1, listener);

//        captureNetworkInterface.Stop();
//        handle.close();
//        pool.shutdown();

    }
}
