package util;

import javafx.scene.control.TableView;
import model.*;
import org.pcap4j.core.*;

public class CNIF implements NIF {
    private PcapHandle.Builder builder;  // 网卡构建对象, 通过 builder 设置网卡操作相关的字段
    private String pcapPath;
    private TableView<Property> table;
    public PcapHandle handle;  // handle, 默认捕获全部数据包
    public PcapDumper dumper;  // 用于存储到默认缓冲区

    private CaptureProperty captureProperty;


    public CNIF(NIFProperty nifProperty, CaptureProperty captureProperty) {
        this.builder = new PcapHandle.Builder(nifProperty.getName());
        this.pcapPath = null;

        this.captureProperty = captureProperty;

        try {
            this.load();
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }
    }

    public CNIF(String pcapPath) {
        this.builder = null;
        this.pcapPath = pcapPath;

        try {
            this.load();
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() throws PcapNativeException {
        if (this.builder==null) {
            this.handle = Pcaps.openOffline(this.pcapPath);
            return;
        }
        this.builder.snaplen(this.captureProperty.getLength())
                .timeoutMillis(this.captureProperty.getTimeout())
                .bufferSize(this.captureProperty.getBuffer())
                .promiscuousMode(this.captureProperty.getPromiscuous());

        this.builder.immediateMode(this.captureProperty.getImmediate() == NIFMode.ImmediateMode.ImmediateMode);

        try {
            this.handle = this.builder.build();
            this.dumper = this.handle.dumpOpen(SettingProperty.tempPcapFolder + "/tmp.pcapng");
        } catch (NotOpenException e) {
            e.printStackTrace();
        }
    }
}
