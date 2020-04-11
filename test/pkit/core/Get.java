package pkit.core;

import org.pcap4j.core.*;

import java.io.EOFException;
import java.util.concurrent.TimeoutException;

public class Get {

    public static void main(String[] args) throws PcapNativeException, NotOpenException, EOFException, TimeoutException{

        String filePath = "tmp/tmp.tps";
        PcapHandle handle = Pcaps.openOffline(filePath);
        handle.setFilter("icmp and ip src 192.168.43.12", BpfProgram.BpfCompileMode.OPTIMIZE);

        PcapPacket packet;
        while (true) {
            try {
                packet = handle.getNextPacketEx();
                PcapPacket.Builder builders = packet.getBuilder();
                System.out.println("Get: \n" + packet);
            } catch (EOFException e) {
                System.out.println("End of file");
                break;
            } catch (TimeoutException e) {
                System.out.println("Timed out");
                break;
            }
        }

    }
}
