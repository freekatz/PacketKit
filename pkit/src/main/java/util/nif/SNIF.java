package util.nif;

import gui.model.NIFProperty;
import gui.model.config.SendProperty;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapPacket;

public class SNIF implements NIF {
    public PcapHandle.Builder builder;
    public PcapHandle handle;

    public SNIF(String nifName) {
        this.builder = new PcapHandle.Builder(nifName);
    }

    @Override
    public void load() throws PcapNativeException {
        handle = builder.build();
    }
}
