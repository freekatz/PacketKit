package controller;

import controller.component.configController.ConfigController;
import controller.component.configController.FilterConfigController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

// TODO: 2020/4/16 系统界面文字国际化
public class SettingController {
    private FXMLLoader loader;

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
        this.InitializeSettingFile();

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

    private void InitializeSettingFile() {

    }

    @FXML
    private void ApplyButtonOnClicked() {
        // TODO: 2020/4/16 利用临时配置文件来实现
        System.out.println(applyButton.getText());
    }

    @FXML
    private void OkButtonOnClicked() {
        System.out.println(okButton.getText());
        FilterConfigController filterConfigController = this.loader.getController();
        filterConfigController.dump();
        System.out.println(filterConfigController.getConfig().toString());
    }

    @FXML
    private void CancelButtonOnClicked() {
        System.out.println(cancelButton.getText());
    }

    @FXML
    private void NifListViewClicked() throws IOException {
        String id = nifListView.getSelectionModel().getSelectedItem().getId();
        switch (id) {
            case "captureLabel":
                System.out.println("capture");
                this.UpdateContent("view/component/configView/CaptureConfigView.fxml");
                break;
            case "filterLabel":
                this.UpdateContent("view/component/configView/FilterConfigView.fxml");
                System.out.println("filter");
                break;
            case "sendLabel":
                this.UpdateContent("view/component/configView/SendConfigView.fxml");
                System.out.println("send");
                break;
            default:
                this.UpdateContent("view/BlankView.fxml");
                System.out.println("blank");
                break;
        }
    }

    @FXML
    private void DumpListViewClicked() throws IOException {
        String id = dumpListView.getSelectionModel().getSelectedItem().getId();
        switch (id) {
            case "folderLabel":
                System.out.println("folder");
                this.UpdateContent("view/component/configView/FolderConfigView.fxml");
                break;
            case "dbLabel":
                this.UpdateContent("view/component/configView/DBConfigView.fxml");
                System.out.println("db");
                break;
            default:
                this.UpdateContent("view/BlankView.fxml");
                System.out.println("blank");
                break;
        }
    }

    @FXML
    private void SystemListViewClicked() throws IOException {
        String id = systemListView.getSelectionModel().getSelectedItem().getId();
        switch (id) {
            case "systemLabel":
                System.out.println("system");
                this.UpdateContent("view/component/configView/SystemConfigView.fxml");
                break;
            case "otherLabel":
                this.UpdateContent("view/component/configView/OtherConfigView.fxml");
                System.out.println("other");
                break;
            default:
                this.UpdateContent("view/BlankView.fxml");
                System.out.println("blank");
                break;
        }
    }

    private void UpdateContent(@NotNull String fxmlPath) throws IOException {
        if (this.loader!=null && this.loader.getLocation().getPath().contains(fxmlPath)) {
            System.out.println(this.loader.getLocation());
            return;
        }
        this.loader = new FXMLLoader();

        URL url = this.loader.getClassLoader().getResource(fxmlPath);

        this.loader.setLocation(url);
        AnchorPane anchorPane = this.loader.load();

        rightScrollPane.setContent(anchorPane);
    }


}
