package util;

import gui.model.analysis.SankeyProperty;
import gui.model.browser.PacketInfoProperty;

public class ChartHandle {

    public static void SankeyHandle(PacketInfoProperty packetInfoProperty, SankeyProperty sankeyProperty, String[] opt) {
        String srcIp = packetInfoProperty.getSrc().split(":")[0];
        String dstIp = packetInfoProperty.getDst().split(":")[0];
        String port;
        boolean ctrl;
        ctrl = !packetInfoProperty.getProtocol().contains("IP") && PacketHandle.SameSubNet(srcIp, opt[0], opt[1]) && PacketHandle.SameSubNet(opt[2], dstIp, opt[3]);
        if (ctrl) {

            // 4. sankey 1
            port = packetInfoProperty.getSrc().split(":")[1] + "(" + packetInfoProperty.getProtocol() + ")";

            if (sankeyProperty.getData().containsKey(srcIp+":"+port))
                sankeyProperty.getData().put(srcIp+":"+port, sankeyProperty.getData().get(srcIp+":"+port)+packetInfoProperty.getLength());
            else sankeyProperty.getData().put(srcIp+":"+port, packetInfoProperty.getLength());

            if (sankeyProperty.getData().containsKey(port+":"+dstIp))
                sankeyProperty.getData().put(port+":"+dstIp, sankeyProperty.getData().get(port+":"+dstIp)+packetInfoProperty.getLength());
            else sankeyProperty.getData().put(port+":"+dstIp, packetInfoProperty.getLength());
        }
    }
}
