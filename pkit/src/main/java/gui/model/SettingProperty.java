package gui.model;

public class SettingProperty {

    //  view fxml path
    private static final String viewFolder = "view/";

    //  single view
    public static String indexView = viewFolder + "IndexView.fxml";
    public static String sendView = viewFolder + "SendView.fxml";
    public static String aboutView = viewFolder + "single/AboutView.fxml";
    public static String analysisView = viewFolder + "single/AnalysisView.fxml";
    public static String loadView = viewFolder + "single/LoadView.fxml";
    public static String packetView = viewFolder + "single/PacketView.fxml";
    public static String settingView = viewFolder + "single/SettingView.fxml";
    public static String captureConfigView = viewFolder + "config/CaptureConfigView.fxml";
    public static String sendConfigView = viewFolder + "config/SendConfigView.fxml";
    public static String filterConfigView = viewFolder + "config/FilterConfigView.fxml";

    //  component view
    public static String fileList = viewFolder + "list/FileList.fxml";
    public static String nifList = viewFolder + "list/NifList.fxml";

    public static String packetData = viewFolder + "browser/PacketData.fxml";
    public static String packetHeader = viewFolder + "browser/PacketHeader.fxml";
    public static String packetList = viewFolder + "browser/PacketList.fxml";

    public static String captureMenuBar = viewFolder + "bar/CaptureMenuBar.fxml";
    public static String captureStatusBar = viewFolder + "bar/CaptureStatusBar.fxml";
    public static String captureToolBar = viewFolder + "bar/CaptureToolBar.fxml";
    public static String filterBar = viewFolder + "bar/FilterBar.fxml";
    public static String sendMenuBar = viewFolder + "bar/SendMenuBar.fxml";
    public static String sendStatusBar = viewFolder + "bar/SendStatusBar.fxml";
    public static String sendToolBar = viewFolder + "bar/SendToolBar.fxml";

    //  配置路径
    public static String captureConfig = "res/config/capture.json";
    public static String sendConfig = "res/config/send.json";
    public static String filterConfig = "res/config/filter.json";

    //  临时文件夹
    public static String tempPcapFolder = "res/temp";

    //  数据包模板文件
    public static String packetTemplatePath = "res/packet/template.pcap";

    //  历史记录文件
    public static String capturePcapFileHistory = "res/history/capturePcap.json";
    public static String sendPcapFileHistory = "res/history/sendPcap.json";
    public static String filterHistory = "res/history/filter.json";

    //  最大记录数
    public static int maxFilterHistory = 20;
    public static int maxPcapFileHistory = 40;

    //  图标文件夹
    public static String captureIconFolder = "/icon/capture";
    public static String filterIconFolder = "/icon/filter";
    public static String sendIconFolder = "/icon/send";

    //  分析界面 html 及对应 api 路径
    public static String analysisWelcomePath = "res/web/app/analysisWelcome.html";

    public static String nifStatTableChartJson = "res/web/api/nifStatTableChart.json";
    public static String nifStatTableChartPath = "res/web/app/nifStatTableChart.html";

    public static String ioLineChartJson = "res/web/api/ioLineChart.json";
    public static String ioLineChartPath = "res/web/app/ioLineChart.html";

    public static String protocolPieChartJson = "res/web/api/protocolPieChart.json";
    public static String protocolPieChartPath = "res/web/app/protocolPieChart.html";

    public static String ipv4StatBarChartJson = "res/web/api/ipv4StatBarChart.json";
    public static String ipv4StatBarChartPath = "res/web/app/ipv4StatBarChart.html";

    public static String ipv6StatBarChartJson = "res/web/api/ipv6StatBarChart.json";
    public static String ipv6StatBarChartPath = "res/web/app/ipv6StatBarChart.html";

    public static String s2oSankeyChartJson = "res/web/api/s2oSankeyChart.json";
    public static String s2oSankeyChartPath = "res/web/app/s2oSankeyChart.html";

    public static String s2sSankeyChartJson = "res/web/api/s2sSankeyChart.json";
    public static String s2sSankeyChartPath = "res/web/app/s2sSankeyChart.html";

    public static String o2sSankeyChartJson = "res/web/api/o2sSankeyChart.json";
    public static String o2sSankeyChartPath = "res/web/app/o2sSankeyChart.html";


    public static String networkChartJson = "res/web/api/networkChart.json";
    public static String networkChartPath = "res/web/app/networkChart.html";

}
