//package core;
//
//import org.pcap4j.core.*;
//import org.pcap4j.util.NifSelector;
//
//import java.io.IOException;
//
//public class Send {
//
//    public static void main(String[] args) throws PcapNativeException {
//        PcapNetworkInterface nif = null;
//        try {
//            nif = new NifSelector().selectNetworkInterface();
//        } catch (
//                IOException e) {
//            e.printStackTrace();
//        }
//        if (nif == null)
//            return;
////
////        SendNetworkInterface sendNetworkInterface = new SendNetworkInterface(nif);
////        sendNetworkInterface.Initial();
////        sendNetworkInterface.Activate();
////        sendNetworkInterface.Load();
////
////        // 修改配置
////        SendNetworkInterfaceConfig sendNetworkInterfaceConfig = new SendNetworkInterfaceConfig();
////        sendNetworkInterfaceConfig.Initial();
////        sendNetworkInterfaceConfig.setCount(5);
////        sendNetworkInterfaceConfig.setTimeoutMillis(500);
////        sendNetworkInterface.setSendNetworkInterfaceConfig(sendNetworkInterfaceConfig);
////
////        sendNetworkInterface.Start();
//    }
//}
