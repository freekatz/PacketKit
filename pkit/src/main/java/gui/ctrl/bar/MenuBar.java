package gui.ctrl.bar;

import gui.ctrl.AnalysisView;
import gui.ctrl.View;
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

public class MenuBar {
    View view;

    public MenuBar() {}

    public void initialize() {

    }

    public void setView(View view) {
        this.view = view;
    }


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

}
