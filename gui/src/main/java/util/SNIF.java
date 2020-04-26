package util;

import model.NIFProperty;
import model.SendProperty;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapPacket;

public class SNIF implements NIF{
    private PcapHandle.Builder builder;
    private PcapHandle handle;
    private PcapPacket packet;

    private final SendProperty sendProperty;

    public SNIF(NIFProperty nifProperty, SendProperty sendProperty) {
        this.builder = new PcapHandle.Builder(nifProperty.getName());

        this.sendProperty = sendProperty;
    }

    @Override
    public void load() throws PcapNativeException {
        this.handle = this.builder.build();
    }

    public void start() {
        try {
            for (int i=0; i<this.sendProperty.getCount(); ++i) {
                Thread.sleep(this.sendProperty.getTimeout());
                this.handle.sendPacket(this.packet);
            } // todo 发送失败重试次数实现
        } catch (NotOpenException | PcapNativeException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.handle.close();
    }

    public void setPacket(PcapPacket packet) {
        this.packet = packet;
    }
}
