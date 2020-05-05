package gui.model;

public class SettingProperty {

    public String captureConfig = "res/config/capture.json";

    public String sendConfig = "res/config/send.json";

    public String filterConfig = "res/config/filter.json";

    public String tempPcapFolder = "res/temp";

    public String capturePcapFileHistory = "res/history/capturePcap.json";

    public String sendPcapFileHistory = "res/history/sendPcap.json";

    public String filterHistory = "res/history/filter.json";

    public String captureIconFolder = "/icon/capture";

    public String filterIconFolder = "/icon/filter";

    public String sendIconFolder = "/icon/send";

    public String packetTemplatePath = "res/packet/template.pcap";

    public int maxFilterHistory = 20;

    public int maxPcapFileHistory = 40;

    // json 为每个文件定制
    public String ioLineChartJson = "res/web/api/ioLineChart.json";
    public String ioLineChartPath = "res/web/app/ioLineChart.html";

    public String protocolPieChartJson = "res/web/api/protocolPieChart.json";
    public String protocolPieChartPath = "res/web/app/protocolPieChart.html";

    public String ipv4StatBarChartJson = "res/web/api/ipv4StatBarChart.json";
    public String ipv4StatBarChartPath = "res/web/app/ipv4StatBarChart.html";

    public String ipv6StatBarChartJson = "res/web/api/ipv6StatBarChart.json";
    public String ipv6StatBarChartPath = "res/web/app/ipv6StatBarChart.html";

    public String analysisWelcomePath = "res/web/app/analysisWelcome.html";

}
