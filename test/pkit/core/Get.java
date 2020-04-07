package pkit.core;

import org.pcap4j.core.*;

import java.io.EOFException;
import java.util.concurrent.TimeoutException;

public class Get {

    public static void main(String[] args) throws PcapNativeException, NotOpenException, EOFException, TimeoutException{

        String filePath = "tmp/tmp.tps";
        PcapHandle handle = Pcaps.openOffline(filePath);
        handle.setFilter("icmp and ip src 192.168.43.12", BpfProgram.BpfCompileMode.OPTIMIZE);

        PcapPacket packet = null;
        int i=-1;
        while ((packet = handle.getNextPacket()) != null) {   // todo 将此判断过程得到的 packet 送入离线数据包组
            System.out.println("Get: \n" + packet);  // todo 由于只可以将某一时刻的全部读入，因此要判别是否丢包，使用 ping host -t 来测试
        }
//        captureNetworkInterface.Stop();
//        handle.close();
//        pool.shutdown();

    }
}
