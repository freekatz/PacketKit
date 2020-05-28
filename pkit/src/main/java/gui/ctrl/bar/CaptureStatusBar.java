package gui.ctrl.bar;

import gui.ctrl.IndexView;
import gui.ctrl.View;
import gui.ctrl.config.CaptureConfigView;
import gui.model.SettingProperty;
import gui.model.config.CaptureProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.FileHandle;

import java.io.IOException;
import java.util.List;

public class CaptureStatusBar {
    SettingProperty settingProperty = new SettingProperty();

    View view;
    int selectIndex;

    @FXML
    public Label statusLabel;

    @FXML
    public Button configButton;

    ContextMenu configMenu;
    MenuItem managerItem;


    public CaptureStatusBar() {}

    public void initialize() {
        configButton.setTooltip(new Tooltip("select or manage the capture config"));
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
                    FXMLLoader loader = new FXMLLoader();
                    AnchorPane managerPane = loader.load(loader.getClassLoader().getResourceAsStream("view/config/CaptureConfigView.fxml"));
                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.DECORATED);
                    stage.initModality(Modality.APPLICATION_MODAL);

                    Scene scene = new Scene(managerPane);
                    stage.setScene(scene);

                    CaptureConfigView captureConfigView = loader.getController();
                    captureConfigView.setCaptureStatusBar(self());

                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        configMenu.getItems().addAll(managerItem, new SeparatorMenuItem());
        configButton.setContextMenu(configMenu);

        this.UpdateContextMenu();

        configButton.setText("None Config");

    }

    private CaptureStatusBar self() {
        return this;
    }

    public void UpdateContextMenu() {
        // 为每个菜单行为添加行为：读取配置
        configMenu.getItems().remove(2, configMenu.getItems().size());
        ToggleGroup group = new ToggleGroup();
        List<CaptureProperty> list = FileHandle.ReadConfig(settingProperty.captureConfig, CaptureProperty.class);
        assert list!=null;
        list.forEach(p -> {
            RadioMenuItem item = new RadioMenuItem(p.getName());
            item.setToggleGroup(group);
            configMenu.getItems().add(item);
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    IndexView indexView = (IndexView) view;
                    if (item.isSelected()) {
                        for (CaptureProperty property : list) {
                            if (item.getText().equals(property.getName())) {
                                indexView.setCaptureProperty(property);
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
        IndexView indexView = (IndexView) view;
        if (indexView.getCaptureProperty() == null && configMenu.getItems().size() > 2) {
            selectIndex = 2;
            RadioMenuItem item = (RadioMenuItem) configMenu.getItems().get(selectIndex);
            item.setSelected(true);
            configButton.setText(item.getText());
            List<CaptureProperty> list = FileHandle.ReadConfig(settingProperty.captureConfig, CaptureProperty.class);
            assert list != null;
            for (CaptureProperty property : list) {
                if (item.getText().contains(property.getName())) {
                    indexView.setCaptureProperty(property);
                    break;
                }
            }
        }
    }


    @FXML
    private void ConfigButtonOnClicked() {
        configMenu.show(configButton, Side.BOTTOM, 0, 0);
    }

}
