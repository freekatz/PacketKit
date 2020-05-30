package gui.ctrl.bar;

import com.fasterxml.jackson.databind.json.JsonMapper;
import gui.ctrl.IndexView;
import gui.ctrl.SendView;
import gui.ctrl.View;
import gui.ctrl.single.AnalysisView;
import gui.model.JobMode;
import gui.model.SettingProperty;
import gui.model.ViewType;
import gui.model.browser.PacketProperty;
import gui.model.history.CapturePcapFileHistoryProperty;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import job.BrowserJob;
import org.pcap4j.core.PcapNetworkInterface;
import util.FileHandle;
import util.ViewHandle;

import java.io.File;
import java.io.IOException;

public class CaptureMenuBar {

    IndexView view;

    @FXML
    Menu fileMenu;

    @FXML
    Menu toolMenu;

    @FXML
    Menu helpMenu;

    @FXML
    MenuItem openItem;

    @FXML
    Menu recentMenu;

    @FXML
    MenuItem clearItem;

    @FXML
    MenuItem closeItem;

    @FXML
    MenuItem saveItem;

    @FXML
    MenuItem settingItem;

    @FXML
    MenuItem quitItem;

    /*
    Tool
     */

    @FXML
    MenuItem sendItem;

    @FXML
    MenuItem analysisItem;

    @FXML
    MenuItem websiteItem;

    @FXML
    MenuItem qaItem;

    @FXML
    MenuItem sampleItem;

    @FXML
    MenuItem downloadItem;

    @FXML
    MenuItem cfuItem;

    @FXML
    MenuItem aboutItem;

    public CaptureMenuBar() {}

    public void initialize() {

        recentMenu.getItems().add(new SeparatorMenuItem());
        ViewHandle.InitializeCapturePcapFileMenu(SettingProperty.capturePcapFileHistory, recentMenu);

        for (int i = 2; i < recentMenu.getItems().size(); i++) {
            RadioMenuItem item = (RadioMenuItem) recentMenu.getItems().get(i);
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (item.isSelected()) {
                        view.setNifName(null);
                        String pattern = "\\(.*?\\)";
                        view.setPcapFile(item.getText().replaceAll(pattern, ""));
                        if (view.getType().equals(ViewType.IndexView)) {
                            view.setType(ViewType.CaptureView);
                            ViewHandle.InitializeCaptureCenter(view);
                        }
                        view.clearBrowser();
                        view.JobScheduler(JobMode.OfflineJob);
                    }
                }
            });
        }

        analysisItem.setDisable(true);

    }

    public void setView(IndexView view) {
        this.view = view;
    }


    @FXML
    private void OpenItemOnClicked() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FileChooser fileChooser = new FileChooser();
        // setting
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PCAP files", "*.pcap"),
                new FileChooser.ExtensionFilter("PCAPNG files", "*.pcapng"),
                new FileChooser.ExtensionFilter("CAP files", "*.cap")
        );
        File file = fileChooser.showOpenDialog(stage);
        if (file==null) return;
        FileHandle.AddHistory(SettingProperty.capturePcapFileHistory, file.getAbsolutePath(), CapturePcapFileHistoryProperty.class);
        view.setPcapFile(file.getAbsolutePath());
        view.setNifName(null);
        if (view.getType().equals(ViewType.IndexView)) {
            view.setType(ViewType.CaptureView);
            ViewHandle.InitializeCaptureCenter(view);
        }

        view.clearBrowser();
        view.JobScheduler(JobMode.OfflineJob);
    }

    @FXML
    private void ClearItemOnClicked() {
        JsonMapper mapper = new JsonMapper();
        CapturePcapFileHistoryProperty property = new CapturePcapFileHistoryProperty();
        try {
            mapper.writeValue(new File(SettingProperty.capturePcapFileHistory), property);
        } catch (IOException e) {
            e.printStackTrace();
        }

        view.getFileListCtrl().getFileList().getItems().clear();
        recentMenu.getItems().clear();
    }

    @FXML
    private void CloseItemOnClicked() {
       if (view.getType().equals(ViewType.IndexView))
            return;
        view.clearBrowser();
        view.setPcapFile(null);
        view.setType(ViewType.IndexView);
        ViewHandle.InitializeCaptureCenter(view);

        view.getCaptureToolBarCtrl().InitializeButtonStatus();
    }

    @FXML
    private void SaveItemOnClicked() {
        if (view.getType().equals(ViewType.IndexView))
            return;
        // new task and new stage to save
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FileChooser fileChooser = new FileChooser();
        // setting
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PCAP files", "*.pcap"),
                new FileChooser.ExtensionFilter("PCAPNG files", "*.pcapng"),
                new FileChooser.ExtensionFilter("CAP files", "*.cap")
        );
        IndexView indexView = (IndexView) view;
        fileChooser.setInitialFileName(indexView.getPcapFile().split("/")[indexView.getPcapFile().split("/").length-1]);
        File file = fileChooser.showSaveDialog(stage);
        if (file==null) return;
        indexView.setSavePath(file.getAbsolutePath());
        indexView.JobScheduler(JobMode.SaveJob);
    }

    @FXML
    private void SettingItemOnClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane managerPane = loader.load(loader.getClassLoader().getResourceAsStream(SettingProperty.settingView));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(managerPane);
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void QuitItemOnClicked() {
        Platform.exit();
    }

    @FXML
    private void AnalysisItemOnClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane managerPane = loader.load(loader.getClassLoader().getResourceAsStream(SettingProperty.analysisView));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(managerPane);
            stage.setScene(scene);

            AnalysisView analysisView = loader.getController();
            analysisView.setView(view);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void SendItemOnClicked() {
        String nifName;
        if (view.getNifName()==null)
            nifName = ((PcapNetworkInterface)ViewHandle.GetPcapNIFList().get(0)).getName();
        else nifName = view.getNifName();

        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane managerPane = loader.load(loader.getClassLoader().getResourceAsStream(SettingProperty.sendView));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(managerPane);
            stage.setScene(scene);

            SendView sendView = loader.getController();
            if (view.getType().equals(ViewType.CaptureView)) {
                if (view.getPacketListCtrl().getPacketTable().getSelectionModel().getSelectedItem() != null) {
                    PacketProperty packetProperty = view.packetPropertyArrayList.get(view.getPacketListCtrl().getPacketTable().getSelectionModel().getSelectedIndex());
                    sendView.setPacketProperty(packetProperty);
                    sendView.packetPropertyArrayList.add(packetProperty);
                    sendView.getPacketListCtrl().getPacketTable().getItems().add(packetProperty.getInfo());
                    // 第一个包初始化
                    BrowserJob job = new BrowserJob(packetProperty, sendView);
                    Thread thread = new Thread(job);
                    thread.start();
                }
            }

            sendView.setNifName(nifName);

            sendView.setIndexView(view);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void WebsiteItemOnClicked() {

    }
    @FXML
    private void QAItemOnClicked() {

    }

    @FXML
    private void SampleItemOnClicked() {

    }

    @FXML
    private void DownloadItemOnClicked() {

    }

    @FXML
    private void CfUItemOnClicked() {

    }

    @FXML
    private void AboutItemOnClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane managerPane = loader.load(loader.getClassLoader().getResourceAsStream(SettingProperty.aboutView));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(managerPane);
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MenuItem getAboutItem() {
        return aboutItem;
    }

    public void setAboutItem(MenuItem aboutItem) {
        this.aboutItem = aboutItem;
    }

    public MenuItem getCfuItem() {
        return cfuItem;
    }

    public void setCfuItem(MenuItem cfuItem) {
        this.cfuItem = cfuItem;
    }

    public MenuItem getDownloadItem() {
        return downloadItem;
    }

    public void setDownloadItem(MenuItem downloadItem) {
        this.downloadItem = downloadItem;
    }

    public MenuItem getSampleItem() {
        return sampleItem;
    }

    public void setSampleItem(MenuItem sampleItem) {
        this.sampleItem = sampleItem;
    }

    public MenuItem getQaItem() {
        return qaItem;
    }

    public void setQaItem(MenuItem qaItem) {
        this.qaItem = qaItem;
    }

    public MenuItem getWebsiteItem() {
        return websiteItem;
    }

    public void setWebsiteItem(MenuItem websiteItem) {
        this.websiteItem = websiteItem;
    }

    public MenuItem getAnalysisItem() {
        return analysisItem;
    }

    public void setAnalysisItem(MenuItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    public MenuItem getSendItem() {
        return sendItem;
    }

    public void setSendItem(MenuItem sendItem) {
        this.sendItem = sendItem;
    }

    public MenuItem getSettingItem() {
        return settingItem;
    }

    public void setSettingItem(MenuItem settingItem) {
        this.settingItem = settingItem;
    }

    public MenuItem getQuitItem() {
        return quitItem;
    }

    public void setQuitItem(MenuItem quitItem) {
        this.quitItem = quitItem;
    }

    public MenuItem getSaveItem() {
        return saveItem;
    }

    public void setSaveItem(MenuItem saveItem) {
        this.saveItem = saveItem;
    }

    public MenuItem getCloseItem() {
        return closeItem;
    }

    public void setCloseItem(MenuItem closeItem) {
        this.closeItem = closeItem;
    }

    public MenuItem getClearItem() {
        return clearItem;
    }

    public void setClearItem(MenuItem clearItem) {
        this.clearItem = clearItem;
    }

    public IndexView getView() {
        return view;
    }

    public Menu getRecentMenu() {
        return recentMenu;
    }

    public void setRecentMenu(Menu recentMenu) {
        this.recentMenu = recentMenu;
    }

    public MenuItem getOpenItem() {
        return openItem;
    }

    public void setOpenItem(MenuItem openItem) {
        this.openItem = openItem;
    }

    public Menu getHelpMenu() {
        return helpMenu;
    }

    public void setHelpMenu(Menu helpMenu) {
        this.helpMenu = helpMenu;
    }

    public Menu getToolMenu() {
        return toolMenu;
    }

    public void setToolMenu(Menu toolMenu) {
        this.toolMenu = toolMenu;
    }

    public Menu getFileMenu() {
        return fileMenu;
    }

    public void setFileMenu(Menu fileMenu) {
        this.fileMenu = fileMenu;
    }
}
