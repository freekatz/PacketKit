package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PacketProperty implements Property{
    private final SimpleIntegerProperty no = new SimpleIntegerProperty(this, "no"); // 程序中自增
    private final SimpleStringProperty time = new SimpleStringProperty(this, "time"); // 程序中自增
    private final SimpleStringProperty src = new SimpleStringProperty(this, "src"); // 自适应，无ip时使用mac
    private final SimpleStringProperty dst = new SimpleStringProperty(this, "dst");
    private final SimpleStringProperty protocol = new SimpleStringProperty(this, "protocol");
    private final SimpleIntegerProperty length = new SimpleIntegerProperty(this, "length");
    private final SimpleStringProperty info = new SimpleStringProperty(this, "info");

    public int getNo() {
        return no.get();
    }

    public SimpleIntegerProperty noProperty() {
        return no;
    }

    public void setNo(int no) {
        this.no.set(no);
    }

    public String getTime() {
        return time.get();
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getInfo() {
        return info.get();
    }

    public SimpleStringProperty infoProperty() {
        return info;
    }

    public void setInfo(String info) {
        this.info.set(info);
    }

    public int getLength() {
        return length.get();
    }

    public SimpleIntegerProperty lengthProperty() {
        return length;
    }

    public void setLength(int length) {
        this.length.set(length);
    }

    public String getProtocol() {
        return protocol.get();
    }

    public SimpleStringProperty protocolProperty() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol.set(protocol);
    }

    public String getDst() {
        return dst.get();
    }

    public SimpleStringProperty dstProperty() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst.set(dst);
    }

    public String getSrc() {
        return src.get();
    }

    public SimpleStringProperty srcProperty() {
        return src;
    }

    public void setSrc(String src) {
        this.src.set(src);
    }

    @Override
    public String toString() {
        return "PacketProperty{" +
                "no=" + getNo() +
                ", time=" + getTime() +
                ", src=" + getSrc() +
                ", dst=" + getDst() +
                ", protocol=" + getProtocol() +
                ", length=" + getLength() +
                ", info=" + getInfo() +
                '}';
    }
}
