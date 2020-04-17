package util;

import org.pcap4j.core.PcapPacket;

public interface Job {

    //  作业接口，便于其它方法调用函数，使用 lambda 表达式
    void Entry(PcapPacket packet);
}
