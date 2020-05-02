package util.job;

import gui.ctrl.IndexView;
import gui.model.browser.PacketInfoProperty;
import gui.model.browser.PacketProperty;
import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapPacket;
import util.PacketHandle;
import util.nif.CNIF;

import java.io.EOFException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

public class SaveJob implements Runnable{

    CNIF cnif;

    IndexView indexView;
    String pcapFile;
    String savePath;
    String filterExpression;

    public SaveJob(IndexView indexView, String path, String savePath, String filterExpression) {
        this.indexView = indexView;
        this.pcapFile = path;
        this.savePath = savePath;
        this.filterExpression = filterExpression;

        cnif = new CNIF(indexView.getPcapFile());

        try {
            cnif.dumper = cnif.handle.dumpOpen(savePath);
        } catch (PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {

        while (true) {
            try {
                BpfProgram bpfProgram = cnif.handle.compileFilter(filterExpression, BpfProgram.BpfCompileMode.OPTIMIZE, (Inet4Address) InetAddress.getByName("255.255.255.255"));
                PcapPacket packet = cnif.handle.getNextPacketEx();
                if (bpfProgram.applyFilter(packet)) {
                    // 对符合过滤器的包进行处理
                    cnif.dumper.dump(packet);
                }
            } catch (EOFException e) {
                break;
            } catch (TimeoutException | PcapNativeException | NotOpenException | UnknownHostException ignored) {
            }
        }
    }
}
