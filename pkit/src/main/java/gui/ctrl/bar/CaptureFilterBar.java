package gui.ctrl.bar;

import gui.bak.controller.StartCapture;
import gui.bak.util.DirHandle;
import gui.ctrl.CaptureView;
import gui.ctrl.FilterConfigView;
import gui.ctrl.View;
import gui.model.CaptureProperty;
import gui.model.FilterProperty;
import gui.model.SettingProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.FileHandle;
import util.Job;
import util.ViewHandle;
import util.nif.CNIF;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CaptureFilterBar {
    View view;
    private int selectIndex;

    @FXML
    Button configButton;

    ContextMenu configMenu;
    MenuItem managerItem;

    @FXML
    Button clearButton;

    @FXML
    Button applyButton;

    @FXML
    ComboBox<String> filterBox;

    public CaptureFilterBar() {}

    public void initialize() {

        ViewHandle.InitializeComboBox(SettingProperty.filterHistory, filterBox);
        Image clearImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder3 + "/x-filter-clear.active.png"));
        clearButton.setGraphic(new ImageView(clearImage));
        Image applyImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder2 + "/x-filter-apply.png"));
        applyButton.setGraphic(new ImageView(applyImage));
        Image configImage = new Image(getClass().getResourceAsStream(SettingProperty.iconFolder3 + "/x-display-filter-bookmark.png"));
        configButton.setGraphic(new ImageView(configImage));

        configMenu = new ContextMenu();
        managerItem = new MenuItem();
        this.InitialContextMenu();

    }

    private void InitialContextMenu() {
        managerItem.setText("Manager");
        managerItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    FXMLLoader loader = ViewHandle.GetLoader("gui/view/FilterConfigView.fxml");
                    AnchorPane managerPane = loader.load();
                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.DECORATED);
                    stage.initModality(Modality.APPLICATION_MODAL);

                    Scene scene = new Scene(managerPane);
                    stage.setScene(scene);

                    FilterConfigView filterConfigView = loader.getController();
                    filterConfigView.setCaptureFilterBar(self());

                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        configMenu.getItems().addAll(managerItem, new SeparatorMenuItem());
        configButton.setContextMenu(configMenu);

        this.UpdateContextMenu();
    }

    private CaptureFilterBar self() {
        return this;
    }


    public void UpdateContextMenu() {
        configMenu.getItems().remove(2, configMenu.getItems().size());
        ToggleGroup group = new ToggleGroup();
        List<FilterProperty> list = FileHandle.ReadJson(SettingProperty.filterConfig, FilterProperty.class);
        assert list != null;
        list.forEach(p -> {
            RadioMenuItem item = new RadioMenuItem(p.getName()
                    + ":" + p.getExpression());
            item.setToggleGroup(group);
            configMenu.getItems().add(item);
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (item.isSelected()) {
                        for (FilterProperty property : list) {
                            if (item.getText().contains(property.getName())) {
                                filterBox.setValue(property.getExpression());
                                view.setFilterProperty(property);
                                break;
                            }
                        }
                    }
                }
            });
        });

        if (selectIndex>1)
            ((RadioMenuItem)configMenu.getItems().get(selectIndex)).setSelected(true);

    }

    public void setView(View view) {
        this.view = view;
        if (view.getFilterProperty()==null && configMenu.getItems().size()>2){
            selectIndex = 2;
            RadioMenuItem item = (RadioMenuItem) configMenu.getItems().get(selectIndex);
            item.setSelected(true);
            System.out.println(item.getText());
            List<FilterProperty> list = FileHandle.ReadJson(SettingProperty.filterConfig, FilterProperty.class);
            assert list != null;
            for (FilterProperty property : list) {
                if (item.getText().contains(property.getName())) {
                    view.setFilterProperty(property);
                    break;
                }
            }
        }
    }

    @FXML
    private void ConfigButtonOnClicked() {
        configMenu.show(configButton, Side.BOTTOM, 0, 0);
    }

    @FXML
    private void ClearButtonOnClicked() {
        filterBox.setValue("");
        if (view.getClass().equals(CaptureView.class)) {
            CaptureView captureView = (CaptureView) view;
            captureView.getPacketListCtrl().getPacketTable().getItems().clear();
            captureView.setFilterExpression("");
            String path;
            if (captureView.getPcapFile()==null)
                path = SettingProperty.tempPcapFolder + "/tmp.pcapng";
            else
                path = captureView.getPcapFile();
            File file = new File(path);
            if (!file.exists())
                return;
            CNIF cnif = new CNIF(path);
            Job.OfflineJob job = new Job.OfflineJob(captureView, cnif);
            Thread thread = new Thread(job);
            thread.start();
        }
    }

    @FXML
    private void ApplyButtonOnClicked() {
        // 添加历史记录
        if (!filterBox.getValue().equals(""))
            FileHandle.AddLine(SettingProperty.filterHistory, filterBox.getValue());
        ViewHandle.InitializeComboBox(SettingProperty.filterHistory, filterBox);
        FilterProperty property = new FilterProperty();
        property.setName("apply-tmp");
        property.setExpression(filterBox.getValue());
        view.setFilterProperty(property);

        if (view.getClass().equals(CaptureView.class)) {
            CaptureView captureView = (CaptureView) view;
            captureView.getPacketListCtrl().getPacketTable().getItems().clear();
            String path;
            if (captureView.getPcapFile()==null)
                path = SettingProperty.tempPcapFolder + "/tmp.pcapng";
            else
                path = captureView.getPcapFile();
            File file = new File(path);
            if (!file.exists())
                return;
            CNIF cnif = new CNIF(path);
            Job.OfflineJob job = new Job.OfflineJob(captureView, cnif);
            Thread thread = new Thread(job);
            thread.start();

            if (captureView.getPcapFile()==null)
                captureView.CaptureControl();
        }
    }
}

