package util.nif;

import org.pcap4j.core.PcapNativeException;

public interface NIF {

    void load() throws PcapNativeException;

}
