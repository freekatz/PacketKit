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

public class DumpGet {

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

        // 启动作业
        ExecutorService pool = Executors.newSingleThreadExecutor();
        Task task = new Task(captureNetworkInterface);

        pool.execute(task);

        PcapHandle handle = Pcaps.openOffline(captureNetworkInterface.getTpsPath());
        handle.setFilter(filterConfig.getFilter(), BpfProgram.BpfCompileMode.OPTIMIZE);

        Task task2 = new Task(handle);

        pool.execute(task2);

//        captureNetworkInterface.Stop();
//        handle.close();
//        pool.shutdown();

    }


    private static class Task implements Runnable {
        private CaptureNetworkInterface captureNetworkInterface;
        private PcapHandle handle;

        public Task(CaptureNetworkInterface captureNetworkInterface) {
            this.captureNetworkInterface=captureNetworkInterface;
        }

        public Task(PcapHandle handle) {
            this.handle = handle;
        }

        @Override
        public void run() {
            try {
                this.captureNetworkInterface.Start();
            } catch (PcapNativeException | NotOpenException | EOFException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
