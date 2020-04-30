package gui.ctrl.bar;

import gui.ctrl.View;
import gui.ctrl.analysis.ChartView;
import gui.model.SettingProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.ViewHandle;

import java.io.IOException;

public class CaptureMenuBar {
    View view;

    @FXML
    MenuItem nifStatItem;

    @FXML
    MenuItem ioItem;

    @FXML
    MenuItem protocolItem;

    @FXML
    MenuItem ipv4StatItem;

    @FXML
    MenuItem ipv6StatItem;

    public CaptureMenuBar() {}

    public void initialize() {

    }

    public void setView(View view) {
        this.view = view;
    }

    private void OpenChart(String chartPath) {
        try {
            FXMLLoader loader = ViewHandle.GetLoader("gui/view/analysis/ChartView.fxml");
            AnchorPane managerPane = loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(managerPane);
            stage.setScene(scene);
//
            ChartView chartView = loader.getController();
            chartView.setCaptureMenuBar(this);
            chartView.setPath(chartPath);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void NifStatItemOnClicked() {

    }

    @FXML
    private void IoItemOnClicked() {
        this.OpenChart(SettingProperty.ioLineChartPath);
    }

    @FXML
    private void ProtocolItemOnClicked() {
        this.OpenChart(SettingProperty.protocolPieChartPath);

    }

    @FXML
    private void Ipv4StatItemOnClicked() {
        this.OpenChart(SettingProperty.ipv4StatBarChartPath);

    }

    @FXML
    private void Ipv6StatItemOnClicked() {
        this.OpenChart(SettingProperty.ipv6StatBarChartPath);

    }
}
