package gui.ctrl.bar;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import gui.ctrl.AnalysisView;
import gui.ctrl.IndexView;
import gui.ctrl.View;
import gui.model.SettingProperty;
import gui.model.history.PcapFileHistoryProperty;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.FileHandle;
import util.ViewHandle;

import java.io.File;
import java.io.IOException;

public class MenuBar {
    View view;

    @FXML
    Menu fileMenu;

    @FXML
    Menu viewMenu;

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
    MenuItem preferenceItem;

    @FXML
    MenuItem quitItem;

    /*
    View
     */

    @FXML
    MenuItem sendItem;

    @FXML
    MenuItem analysisItem;

    @FXML
    MenuItem contentItem;

    @FXML
    MenuItem websiteItem;

    @FXML
    MenuItem qaItem;

    @FXML
    MenuItem bbsItem;

    @FXML
    MenuItem sampleItem;

    @FXML
    MenuItem downloadItem;

    @FXML
    MenuItem cfuItem;

    @FXML
    MenuItem aboutItem;

    public MenuBar() {}

    public void initialize() {
        recentMenu.getItems().add(new SeparatorMenuItem());
        ViewHandle.InitializePcapFileMenu(SettingProperty.pcapFileHistory, recentMenu);

        for (int i = 2; i < recentMenu.getItems().size(); i++) {
            RadioMenuItem item = (RadioMenuItem) recentMenu.getItems().get(i);
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    IndexView indexView = (IndexView) view;
                    if (item.isSelected()) {
                        indexView.setNifName(null);
                        String pattern = "\\(.*?\\)";
                        indexView.setPcapFile(item.getText().replaceAll(pattern, ""));
                        indexView.StartCapture("offline");
                    }
                }
            });
        }

    }

    public void setView(View view) {
        this.view = view;
    }


    private void StartCaptureOffline(String pcapFile) {
        String type = view.getType();
        IndexView indexView = (IndexView) view;
        if (type.equals("index")) {
            indexView.setType("capture");
            indexView.setNifName(null);
            indexView.setPcapFile(pcapFile);
            ViewHandle.InitializeCenter(indexView);
        }
        indexView.StartCapture("offline");

    }

    @FXML
    private void OpenItemOnClicked() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        // setting
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PCAP files", "*.pcap"),
                new FileChooser.ExtensionFilter("PCAPNG files", "*.pcapng"),
                new FileChooser.ExtensionFilter("CAP files", "*.cap")
        );
        File file = fileChooser.showOpenDialog(stage);
        if (file==null) return;
        FileHandle.AddHistory(SettingProperty.pcapFileHistory, file.getAbsolutePath(), PcapFileHistoryProperty.class);
        String type = view.getType();
        IndexView indexView = (IndexView) view;
        indexView.setPcapFile(file.getAbsolutePath());
        indexView.setNifName(null);
        if (type.equals("index")) {
            indexView.setType("capture");
            ViewHandle.InitializeCenter(indexView);
        }

        indexView.clearBrowser();
        indexView.StartCapture("offline");
    }

    @FXML
    private void ClearItemOnClicked() {
        JsonMapper mapper = new JsonMapper();
        PcapFileHistoryProperty property = new PcapFileHistoryProperty();
        try {
            mapper.writeValue(new File(SettingProperty.pcapFileHistory), property);
        } catch (IOException e) {
            e.printStackTrace();
        }

        IndexView indexView = (IndexView) view;
        indexView.getFileListCtrl().getFileList().getItems().clear();
        recentMenu.getItems().clear();
    }

    @FXML
    private void MergeItemOnClicked() {
        IndexView indexView = (IndexView) view;
        indexView.clearBrowser();
        indexView.setPcapFile(null);
        indexView.setType("index");
        ViewHandle.InitializeCenter(indexView);
    }

    @FXML
    private void CloseItemOnClicked() {

    }

    @FXML
    private void SaveItemOnClicked() {

    }

    @FXML
    private void PreferenceItemOnClicked() {

    }

    @FXML
    private void QuitItemOnClicked() {
        Platform.exit();
    }

    /*
    View
     */


    @FXML
    private void AnalysisItemOnClicked() {
        try {
            FXMLLoader loader = ViewHandle.GetLoader("gui/view/AnalysisView.fxml");
            AnchorPane managerPane = loader.load();
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

    }

    @FXML
    private void ContentItemOnClicked() {

    }

    @FXML
    private void WebsiteItemOnClicked() {

    }
    @FXML
    private void QAItemOnClicked() {

    }

    @FXML
    private void BBSItemOnClicked() {

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

    public MenuItem getBbsItem() {
        return bbsItem;
    }

    public void setBbsItem(MenuItem bbsItem) {
        this.bbsItem = bbsItem;
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

    public MenuItem getContentItem() {
        return contentItem;
    }

    public void setContentItem(MenuItem contentItem) {
        this.contentItem = contentItem;
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

    public MenuItem getPreferenceItem() {
        return preferenceItem;
    }

    public void setPreferenceItem(MenuItem preferenceItem) {
        this.preferenceItem = preferenceItem;
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

    public View getView() {
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

    public Menu getViewMenu() {
        return viewMenu;
    }

    public void setViewMenu(Menu viewMenu) {
        this.viewMenu = viewMenu;
    }

    public Menu getFileMenu() {
        return fileMenu;
    }

    public void setFileMenu(Menu fileMenu) {
        this.fileMenu = fileMenu;
    }
}
