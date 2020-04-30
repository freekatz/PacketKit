package gui.ctrl.list;

import gui.ctrl.CaptureView;
import gui.ctrl.IndexView;
import gui.ctrl.View;
import gui.model.SettingProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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


public class IndexFileList {
    View view;

    @FXML
    Button openButton;

    @FXML
    ListView<String> fileList;

    public IndexFileList() {}

    public void initialize() {
        ViewHandle.InitializeList(SettingProperty.pcapFileHistory, fileList);

        fileList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    String item = fileList.getSelectionModel().getSelectedItem();
                    String pattern = "\\(.*?\\)";
                    item = item.replaceAll(pattern, "");
                    File file = new File(item);
                    FileHandle.AddLine(SettingProperty.pcapFileHistory, file.getAbsolutePath());
                    ViewHandle.InitializeList(SettingProperty.pcapFileHistory, fileList);

                    StartCapture(item);
                    view.close(event);
                }
            }
        });
    }

    public void setView(View view) {
        this.view = view;
        if (view.getNifName()==null && fileList.getItems().size()>0)
            view.setPcapFile(fileList.getItems().get(0));

    }

    private void StartCapture(String pcapFile) {
        try {
            FXMLLoader loader = ViewHandle.GetLoader("gui/view/CaptureView.fxml");
            AnchorPane capturePane = loader.load();
            Stage stage = new Stage();

            Scene scene = new Scene(capturePane);
            stage.setScene(scene);

            CaptureView captureView = loader.getController();
            IndexView indexView = (IndexView) view;
            captureView.setFilterExpression(indexView.filterProperty.getExpression());
            captureView.setCaptureProperty(indexView.captureProperty);
            captureView.setPcapFile(pcapFile);
            captureView.setNifName(null);
            captureView.setIndexView((IndexView) view);

            System.out.println(indexView.filterProperty.getExpression());
            System.out.println(indexView.captureProperty.getName());
            System.out.println(pcapFile);

            stage.show();

            captureView.CaptureControl();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void OpenButtonOnClicked(Event event) {
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
        FileHandle.AddLine(SettingProperty.pcapFileHistory, file.getAbsolutePath());
        StartCapture(file.getAbsolutePath());
        view.close(event);
    }
}
