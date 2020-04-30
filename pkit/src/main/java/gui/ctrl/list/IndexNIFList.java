package gui.ctrl.list;

import gui.bak.model.NIFProperty;
import gui.ctrl.CaptureView;
import gui.ctrl.IndexView;
import gui.ctrl.View;
import gui.model.SettingProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import util.FileHandle;
import util.ViewHandle;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class IndexNIFList {
    View view;
    List<PcapNetworkInterface> pcapNifList;

    @FXML
    Button captureButton;

    @FXML
    MenuButton displayMenu;

    @FXML
    CheckMenuItem allCheckItem;

    @FXML
    CheckMenuItem loopbackCheckItem;

    @FXML
    CheckMenuItem upCheckItem;

    @FXML
    CheckMenuItem runningCheckItem;

    @FXML
    CheckMenuItem localCheckItem;

//    @FXML
//    CheckMenuItem wireCheckItem;
//
//    @FXML
//    CheckMenuItem virtualCheckItem;
//
//    @FXML
//    CheckMenuItem ipv4CheckItem;
//
//    @FXML
//    CheckMenuItem ipv6CheckItem;

    @FXML
    ListView<Node> nifList;

    public IndexNIFList() {}

    // add filter tf
    public void initialize() {
        allCheckItem.setSelected(true);
        localCheckItem.setSelected(false);
        upCheckItem.setSelected(false);
        loopbackCheckItem.setSelected(false);
        runningCheckItem.setSelected(false);
        pcapNifList = ViewHandle.GetPcapNIFList();
        this.UpdateNIFList();
        displayMenu.getItems().forEach(menuItem -> {
            menuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (menuItem.getText().equals("All") && ((CheckMenuItem)menuItem).isSelected()) {
                        displayMenu.getItems().forEach(menuItem1 -> {
                            if (!menuItem1.getText().equals("All"))
                                ((CheckMenuItem)menuItem1).setSelected(false);
                        });
                    } else {
                        displayMenu.getItems().forEach(menuItem1 -> {
                            if (menuItem1.getText().equals("All"))
                                ((CheckMenuItem)menuItem1).setSelected(false);
                        });
                    }

                    UpdateNIFList();
                }
            });
        });
        nifList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    Label item = (Label) nifList.getSelectionModel().getSelectedItem();
                    view.setNifName(item.getId());
                }
                if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Label item = (Label) nifList.getSelectionModel().getSelectedItem();

                    StartCapture(item.getText());
                    view.close(event);
                }
            }
        });

        nifList.getSelectionModel().select(0);
    }

    public void setView(View view) {
        this.view = view;
        if (view.getNifName()==null && nifList.getItems().size()>0)
            view.setNifName(nifList.getItems().get(0).getId());
    }

    private void StartCapture(String name) {
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
            captureView.setPcapFile(null);
            PcapNetworkInterface nif=null;
            for (int i = 0; i < nifList.getItems().size(); i++) {
                Label label = (Label) nifList.getItems().get(i);
                if (label.getText().equals(name))
                    captureView.setNifName(label.getId());
            }

            captureView.setIndexView((IndexView) view);

            System.out.println(indexView.filterProperty.getExpression());
            System.out.println(indexView.captureProperty.getName());

            stage.show();

            captureView.CaptureControl();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void UpdateNIFList() {
        nifList.getItems().clear();
        ArrayList<PcapNetworkInterface> t = new ArrayList<>(pcapNifList.size());
        t.addAll(pcapNifList);
        if (t.size()>0) {
            for (int i = 0; i < t.size(); i++) {
                if (allCheckItem.isSelected())
                    break;
                PcapNetworkInterface pcapNif = t.get(i);
                if (loopbackCheckItem.isSelected())
                    if (!pcapNif.isLoopBack())
                        t.remove(pcapNif);
                if (!loopbackCheckItem.isSelected())
                    if (pcapNif.isLoopBack())
                        t.remove(pcapNif);
                if (upCheckItem.isSelected())
                    if (!pcapNif.isUp())
                        t.remove(pcapNif);
                if (!upCheckItem.isSelected())
                    if (pcapNif.isUp())
                        t.remove(pcapNif);
                if (runningCheckItem.isSelected())
                    if (!pcapNif.isRunning())
                        t.remove(pcapNif);
                if (!runningCheckItem.isSelected())
                    if (pcapNif.isRunning())
                        t.remove(pcapNif);
                if (localCheckItem.isSelected())
                    if (!pcapNif.isLocal())
                        t.remove(pcapNif);
                if (!localCheckItem.isSelected())
                    if (pcapNif.isLocal())
                        t.remove(pcapNif);
            }


            for (PcapNetworkInterface pcapNif : t) {
                Label label = new Label(pcapNif.getName());
                label.setId(pcapNif.getName());
                nifList.getItems().add(label);
            }
        }

    }

    @FXML
    private void CaptureButtonOnClicked() {
        this.StartCapture(((Label)nifList.getSelectionModel().getSelectedItem()).getText());
    }
}
