package pkit.core;

import org.pcap4j.core.*;
import org.pcap4j.util.NifSelector;
import pkit.core.base.config.CaptureFilterConfig;
import pkit.core.base.nif.CaptureNetworkInterface;

import java.io.EOFException;
import java.io.IOException;
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
        captureNetworkInterface.getCaptureNetworkInterfaceConfig().setCount(-1);
        CaptureFilterConfig captureFilterConfig = new CaptureFilterConfig();
        captureFilterConfig.Initial();
        captureFilterConfig.setFilter("icmp and ip src 192.168.2.114");
        captureNetworkInterface.setCaptureFilterConfig(captureFilterConfig);

//        // 启动作业
//        ExecutorService pool = Executors.newSingleThreadExecutor();
//        Task task = new Task(captureNetworkInterface);
//
//        pool.execute(task);

        captureNetworkInterface.Start();
        captureNetworkInterface.Stop();
//        pool.shutdown();

    }


    private static class Task implements Runnable {
        private CaptureNetworkInterface captureNetworkInterface;

        public Task(CaptureNetworkInterface captureNetworkInterface) {
            this.captureNetworkInterface=captureNetworkInterface;
        }

        @Override
        public void run() {
            try {
                this.captureNetworkInterface.Start();
            } catch (PcapNativeException | NotOpenException | EOFException | TimeoutException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
