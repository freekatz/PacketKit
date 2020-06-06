package gui.ctrl.bar;

import gui.ctrl.IndexView;
import gui.ctrl.View;
import gui.ctrl.config.FilterConfigView;
import gui.model.JobMode;
import gui.model.SettingProperty;
import gui.model.ViewType;
import gui.model.config.FilterProperty;
import gui.model.history.FilterHistoryProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.FileHandle;
import util.ViewHandle;

import java.io.IOException;
import java.util.List;

public class FilterBar {

    IndexView view;
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

    public FilterBar() {}

    public void initialize() {

        configButton.setTooltip(new Tooltip("select or manage the filter config"));
        clearButton.setTooltip(new Tooltip("clear current filter expression"));
        applyButton.setTooltip(new Tooltip("apply the filter expression"));
        filterBox.setTooltip(new Tooltip("view the filter history"));

        ViewHandle.InitializeFilterComboBox(SettingProperty.filterHistory, filterBox);
        Image clearImage = new Image(getClass().getResourceAsStream(SettingProperty.filterIconFolder + "/clear.png"));
        clearButton.setGraphic(new ImageView(clearImage));
        Image applyImage = new Image(getClass().getResourceAsStream(SettingProperty.filterIconFolder + "/apply.png"));
        applyButton.setGraphic(new ImageView(applyImage));
        Image configImage = new Image(getClass().getResourceAsStream(SettingProperty.filterIconFolder + "/config.png"));
        configButton.setGraphic(new ImageView(configImage));

        configMenu = new ContextMenu();
        managerItem = new MenuItem();
        this.InitialContextMenu();

        filterBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode()==KeyCode.ENTER) {
                    // 添加历史记录
                    if (filterBox.getValue()==null)
                        filterBox.setValue("");
                    if (filterBox.getValue().equals(view.getFilterProperty().getExpression()))
                        return;
                    if (!filterBox.getValue().equals(""))
                        FileHandle.AddHistory(SettingProperty.filterHistory, filterBox.getValue(), FilterHistoryProperty.class);
                    ViewHandle.InitializeFilterComboBox(SettingProperty.filterHistory, filterBox);
                    view.getFilterProperty().setExpression(filterBox.getValue());
                    if (view.getType().equals(ViewType.CaptureView)) {
                        view.clearBrowser();
                        view.JobScheduler(JobMode.ApplyJob);
                    }

                    filterBox.getEditor().requestFocus();
                    filterBox.getEditor().deselect();
                    filterBox.getEditor().end();
                }
            }
        });

    }

    private void InitialContextMenu() {
        managerItem.setText("Manager");
        managerItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    AnchorPane managerPane = loader.load(loader.getClassLoader().getResourceAsStream(SettingProperty.filterConfigView));
                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.DECORATED);
                    stage.initModality(Modality.APPLICATION_MODAL);

                    Scene scene = new Scene(managerPane);
                    stage.setScene(scene);

                    FilterConfigView filterConfigView = loader.getController();
                    filterConfigView.setFilterBar(self());

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

    private FilterBar self() {
        return this;
    }


    public void UpdateContextMenu() {
        configMenu.getItems().remove(2, configMenu.getItems().size());
        ToggleGroup group = new ToggleGroup();
        List<FilterProperty> list = FileHandle.ReadConfig(SettingProperty.filterConfig, FilterProperty.class);
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
                            if (item.getText().equals(property.getName())) {
                                filterBox.setValue(property.getExpression());
                                view.setFilterProperty(property);
                                // TODO: 2020/6/1 点击选择配置，应用过滤器
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

    public void setView(IndexView view) {
        this.view = view;
        if (view.getFilterProperty()==null && configMenu.getItems().size()>2){
            selectIndex = 2;
            RadioMenuItem item = (RadioMenuItem) configMenu.getItems().get(selectIndex);
            item.setSelected(true);
            List<FilterProperty> list = FileHandle.ReadConfig(SettingProperty.filterConfig, FilterProperty.class);
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
        if (filterBox.getValue()==null)
            filterBox.setValue("");
        if (filterBox.getValue().equals(""))
            return;
        if (filterBox.getValue().equals(view.getFilterProperty().getExpression()))
            view.getFilterProperty().setExpression("");
        filterBox.setValue("");
        if (view.getType().equals(ViewType.CaptureView)) {
            view.getPacketListCtrl().getPacketTable().getItems().clear();
            view.getFilterProperty().setExpression("");
            view.JobScheduler(JobMode.ApplyJob);
        }
    }

    @FXML
    private void ApplyButtonOnClicked() {
        // 添加历史记录
        if (filterBox.getValue()==null)
            filterBox.setValue("");
        if (filterBox.getValue().equals(view.getFilterProperty().getExpression()))
            return;
        if (!filterBox.getValue().equals(""))
            FileHandle.AddHistory(SettingProperty.filterHistory, filterBox.getValue(), FilterHistoryProperty.class);
        ViewHandle.InitializeFilterComboBox(SettingProperty.filterHistory, filterBox);
        view.getFilterProperty().setExpression(filterBox.getValue());
        if (view.getType().equals(ViewType.CaptureView)) {
            view.clearBrowser();
            view.JobScheduler(JobMode.ApplyJob);
        }
    }
}

