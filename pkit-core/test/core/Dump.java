package pkit.core;

import org.pcap4j.core.*;
import org.pcap4j.util.NifSelector;
import pkit.core.base.config.CaptureFilterConfig;
import pkit.core.base.nif.CaptureNetworkInterface;
import pkit.core.base.group.CapturePacketGroup;
import pkit.core.base.group.PacketGroup;

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

//        // 启动作业
//        ExecutorService pool = Executors.newSingleThreadExecutor();
//        Task task = new Task(captureNetworkInterface);
//
//        pool.execute(task);
//        pool.shutdown();

        CaptureFilterConfig captureFilterConfig = new CaptureFilterConfig();
        captureFilterConfig.Initial();
        captureFilterConfig.setFilter("ip src 192.168.2.113");
        captureNetworkInterface.setCaptureFilterConfig(captureFilterConfig);

        captureNetworkInterface.Start();
        ArrayList<PacketGroup> packetGroupArrayList = captureNetworkInterface.getPacketGroupArrayList();
        CapturePacketGroup packetGroup = (CapturePacketGroup) packetGroupArrayList.get(0);
        LinkedHashMap<Integer, PcapPacket> group = packetGroup.getPacketGroup();
        group.forEach(((index, packet) -> System.out.println(packet)));

        String path = "tmp/group.tps";
        PcapDumper dumper = captureNetworkInterface.handle.dumpOpen(path);
        packetGroup.Dump(dumper);

        CaptureFilterConfig captureFilterConfig1 = new CaptureFilterConfig();
        captureFilterConfig1.Initial();
        captureFilterConfig1.setFilter("ip src 192.168.2.113");
        captureNetworkInterface.setCaptureFilterConfig(captureFilterConfig1);
        BpfProgram bpfProgram = captureNetworkInterface.handle
                .compileFilter(
                        captureFilterConfig1.getFilter(),
                        BpfProgram.BpfCompileMode.OPTIMIZE,
                        (Inet4Address) captureNetworkInterface.getIPv4Address().getNetmask()
                );

        // 更新过滤器之后的结果
        packetGroup.Clear();
        packetGroup.Add(captureNetworkInterface.getTpsPath(), bpfProgram);

        captureNetworkInterface.Start();

        LinkedHashMap<Integer, PcapPacket> group1 = packetGroup.getPacketGroup();
        group1.forEach(((index, packet) -> System.out.println(packet)));

        String path1 = "tmp/group1.tps";
        PcapDumper dumper1 = captureNetworkInterface.handle.dumpOpen(path1);
        packetGroup.Dump(dumper1);

        captureNetworkInterface.Stop();

        // 结果 tmp.tps 40 个包，group.tps 只有 tcp 包，group1.tps 只有 icmp 包
        // 结果完全正确


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
