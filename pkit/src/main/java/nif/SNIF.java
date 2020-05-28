package nif;

import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;

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
