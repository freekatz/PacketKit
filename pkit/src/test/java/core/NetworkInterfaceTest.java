package core;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetworkInterfaceTest {

    public static void main(String[] args) throws Exception {
        // 获得本机的所有网络接口
        Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
        while (nifs.hasMoreElements()) {
            NetworkInterface nif = nifs.nextElement();
            // 获得与该网络接口绑定的 IP 地址，一般只有一个
            Enumeration<InetAddress> addresses = nif.getInetAddresses();
            System.out.println(nif.getName());
//            while (addresses.hasMoreElements()) {
//                InetAddress addr = addresses.nextElement();
////                if (addr instanceof Inet4Address) { // 只关心 IPv4 地址
//                    System.out.println("网卡接口名称：" + nif.getIndex());
//                    System.out.println(nif.getInetAddresses().nextElement());
//                    System.out.println(NetworkInterface.getByInetAddress(nif.getInetAddresses().nextElement()).getIndex());
//                    System.out.println("网卡接口地址：" + addr.getHostAddress());
//                    System.out.println();
////                }
//            }
        }
    }
}