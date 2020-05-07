//package core;
//
//import org.pcap4j.core.PcapNativeException;
//import org.pcap4j.core.PcapNetworkInterface;
//import org.pcap4j.core.Pcaps;
//
//import java.io.IOException;
//import java.util.List;
//
//public class NIFTest {
//
//    private NIFTest() {}
//
//    public static void main(String[] args) throws IOException {
//
//        PcapNetworkInterface nif;
//
//        List allDevs = null;
//
//        try {
//            allDevs = Pcaps.findAllDevs();
//        } catch (PcapNativeException var3) {
//            throw new IOException(var3.getMessage());
//        }
//
//        if (allDevs != null && !allDevs.isEmpty()) {
//            int deviceNum = 5;
//            nif = (PcapNetworkInterface) allDevs.get(deviceNum);
//        } else {
//            throw new IOException("No NIF to capture.");
//        }
//
//        System.out.println("isLocal: " + nif.isLocal());
//        System.out.println("isLoopBack: " + nif.isLoopBack());
//        System.out.println("isRuning: " + nif.isRunning());
//        System.out.println("isUp: " + nif.isUp());
//        System.out.println("Description: " + nif.getDescription());
//        System.out.println("Name: " + nif.getName());
//        System.out.println("IPv6 Addresses: " + nif.getAddresses().get(0));
//        System.out.println("IPv4 Addresses: " + nif.getAddresses().get(1).getDestinationAddress());
//        System.out.println("LinkLayerAddresses: " + nif.getLinkLayerAddresses());
//        System.out.println("hashCode: " + nif.hashCode());
//        System.out.println("String: " + nif.toString());
//        System.out.println("Class: " + nif.getClass());
//
//
//    }
//
//}
