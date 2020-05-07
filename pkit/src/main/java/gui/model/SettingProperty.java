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

    public String analysisWelcomePath = "res/web/app/analysisWelcome.html";

    // todo: json 为每个文件定制

    public String nifTableChartJson = "res/web/api/ipv6StatBarChart.json";
    public String nifTableChartPath = "res/web/app/ipv6StatBarChart.html";

    public String ioLineChartJson = "res/web/api/ioLineChart.json";
    public String ioLineChartPath = "res/web/app/ioLineChart.html";

    public String protocolPieChartJson = "res/web/api/protocolPieChart.json";
    public String protocolPieChartPath = "res/web/app/protocolPieChart.html";

    public String ipv4StatBarChartJson = "res/web/api/ipv4StatBarChart.json";
    public String ipv4StatBarChartPath = "res/web/app/ipv4StatBarChart.html";

    public String ipv6StatBarChartJson = "res/web/api/ipv6StatBarChart.json";
    public String ipv6StatBarChartPath = "res/web/app/ipv6StatBarChart.html";

    public String s2oSankeyChartJson = "res/web/api/s2oSankeyChart.json";
    public String s2oSankeyChartPath = "res/web/app/s2oSankeyChart.html";

    public String s2sSankeyChartJson = "res/web/api/s2sSankeyChart.json";
    public String s2sSankeyChartPath = "res/web/app/s2sSankeyChart.html";

    public String o2sSankeyChartJson = "res/web/api/o2sSankeyChart.json";
    public String o2sSankeyChartPath = "res/web/app/o2sSankeyChart.html";


    public String networkChartJson = "res/web/api/networkChart.json";
    public String networkChartPath = "res/web/app/networkChart.html";

}
