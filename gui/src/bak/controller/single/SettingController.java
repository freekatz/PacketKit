package bak.controller.single;

import bak.controller.component.configController.FilterConfigController;
import bak.controller.component.configController.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;

// TODO: 2020/4/16 系统界面文字国际化
public class SettingController {
    private final FXMLLoader captureLoader = new FXMLLoader();
    private final FXMLLoader filterLoader = new FXMLLoader();
    private final FXMLLoader sendLoader = new FXMLLoader();
    private final FXMLLoader folderLoader = new FXMLLoader();
    private final FXMLLoader dbLoader = new FXMLLoader();
    private final FXMLLoader systemLoader = new FXMLLoader();
    private final FXMLLoader otherLoader = new FXMLLoader();
    private final FXMLLoader blankLoader = new FXMLLoader();

    private final JsonHandle jsonHandle = new JsonHandle();

    @FXML
    Button applyButton;

    @FXML
    Button okButton;

    @FXML
    Button cancelButton;

    @FXML
    ScrollPane rightScrollPane;

    @FXML
    ListView<Label> nifListView;

    @FXML
    ListView<Label> dumpListView;

    @FXML
    ListView<Label> systemListView;

    public SettingController() {

    }

    public void initialize() {
        // 初始化操作
        // 在此处添加界面组件
        this.InitializeNifListView();
        this.InitializeDumpListView();
        this.InitializeSystemListView();

    }

    private void InitializeNifListView() {
        ObservableList<Label> ob = FXCollections.observableArrayList();
        Label captureLabel = new Label("捕获默认配置");
        captureLabel.setId("captureLabel");
        Label filterLabel = new Label("过滤器默认设置");
        filterLabel.setId("filterLabel");
        Label sendLabel= new Label("发送默认设置");
        sendLabel.setId("sendLabel");
        ob.addAll(captureLabel, filterLabel, sendLabel);
        nifListView.setItems(ob);
    }

    private void InitializeDumpListView() {
        ObservableList<Label> ob = FXCollections.observableArrayList();
        Label folderLabel = new Label("默认目录设置");
        folderLabel.setId("folderLabel");
        Label dbLabel = new Label("数据库设置");
        dbLabel.setId("dbLabel");
        ob.addAll(folderLabel, dbLabel);
        dumpListView.setItems(ob);
    }

    private void InitializeSystemListView() {
        ObservableList<Label> ob = FXCollections.observableArrayList();
        Label systemLabel = new Label("系统设置");
        systemLabel.setId("systemLabel");
        Label otherLabel = new Label("其他设置");
        otherLabel.setId("otherLabel");
        ob.addAll(systemLabel, otherLabel);
        systemListView.setItems(ob);
    }

    private void SettingHandle() throws IOException {
//        CaptureConfigController captureConfigController = this.captureLoader.getController();
//        captureConfigController.parse();
//        this.jsonHandle.Object2Json(new File("config/nif/capture.json"), captureConfigController.config());

        FilterConfigController filterConfigController = this.filterLoader.getController();
        filterConfigController.parse();
        this.jsonHandle.Object2Json(new File("./gui/res/config/filter/filter.json"), filterConfigController.config());
//
//        SendConfigController sendConfigController = this.sendLoader.getController();
//        sendConfigController.parse();
//        this.jsonHandle.Object2Json(new File("config/nif/send.json"), sendConfigController.config());
//
//        FolderConfigController folderConfigController = this.folderLoader.getController();
//        folderConfigController.parse();
//        this.jsonHandle.Object2Json(new File("config/dump/folder.json"), folderConfigController.config());
//
//        DBConfigController dbConfigController = this.dbLoader.getController();
//        dbConfigController.parse();
//        this.jsonHandle.Object2Json(new File("config/dump/db.json"), dbConfigController.config());
//
//        SystemConfigController systemConfigController = this.systemLoader.getController();
//        systemConfigController.parse();
//        this.jsonHandle.Object2Json(new File("config/sys/system.json"), systemConfigController.config());
//
//        OtherConfigController otherConfigController = this.otherLoader.getController();
//        otherConfigController.parse();
//        this.jsonHandle.Object2Json(new File("config/sys/other.json"), otherConfigController.config());

    }

    @FXML
    private void ApplyButtonOnClicked() {
        // TODO: 2020/4/16 利用临时配置文件来实现
        System.out.println(applyButton.getText());
    }

    @FXML
    private void OkButtonOnClicked() throws IOException {
//        // todo 以下改为全部配置的逻辑
        System.out.println(okButton.getText());
        this.SettingHandle();
    }

    @FXML
    private void CancelButtonOnClicked() {
        System.out.println(cancelButton.getText());
    }

    @FXML
    private void NifListViewClicked() throws IOException {
        String id = nifListView.getSelectionModel().getSelectedItem().getId();
        String fxmlPath;
        switch (id) {
            case "captureLabel":
                System.out.println("capture");
                fxmlPath = "bak/controller/view/component/configView/CaptureConfigView.fxml";
                this.UpdateContent(this.captureLoader, fxmlPath);
                break;
            case "filterLabel":
                System.out.println("filter");
                fxmlPath = "bak/controller/view/component/configView/FilterConfigView.fxml";
                this.UpdateContent(this.filterLoader, fxmlPath);
                break;
            case "sendLabel":
                System.out.println("send");
                fxmlPath = "bak/controller/view/component/configView/SendConfigView.fxml";
                this.UpdateContent(this.sendLoader, fxmlPath);
                break;
            default:
                System.out.println("blank");
                fxmlPath = "bak/controller/view/single/BlankView.fxml";
                this.UpdateContent(this.blankLoader, fxmlPath);
                break;
        }
    }

    @FXML
    private void DumpListViewClicked() throws IOException {
        String id = dumpListView.getSelectionModel().getSelectedItem().getId();
        String fxmlPath;
        switch (id) {
            case "folderLabel":
                System.out.println("folder");
                fxmlPath = "bak/controller/view/component/configView/FolderConfigView.fxml";
                this.UpdateContent(this.folderLoader, fxmlPath);
                break;
            case "dbLabel":
                System.out.println("db");
                fxmlPath = "bak/controller/view/component/configView/DBConfigView.fxml";
                this.UpdateContent(this.dbLoader, fxmlPath);
                break;
            default:
                System.out.println("blank");
                fxmlPath = "bak/controller/view/single/BlankView.fxml";
                this.UpdateContent(this.blankLoader, fxmlPath);
                break;
        }
    }

    @FXML
    private void SystemListViewClicked() throws IOException {
        String id = systemListView.getSelectionModel().getSelectedItem().getId();
        String fxmlPath;
        switch (id) {
            case "systemLabel":
                System.out.println("system");
                fxmlPath = "bak/controller/view/component/configView/SystemConfigView.fxml";
                this.UpdateContent(this.systemLoader, fxmlPath);
                break;
            case "otherLabel":
                System.out.println("other");
                fxmlPath = "bak/controller/view/component/configView/OtherConfigView.fxml";
                this.UpdateContent(this.otherLoader, fxmlPath);
                break;
            default:
                System.out.println("blank");
                fxmlPath = "bak/controller/view/single/BlankView.fxml";
                this.UpdateContent(this.blankLoader, fxmlPath);
                break;
        }
    }

    private void UpdateContent(FXMLLoader loader, @NotNull String fxmlPath) throws IOException {
        if (loader.getLocation()!=null)
            return;
        URL url = this.filterLoader.getClassLoader().getResource(fxmlPath);
        loader.setLocation(url);
        AnchorPane anchorPane = loader.load();
        rightScrollPane.setContent(anchorPane);
    }
}
