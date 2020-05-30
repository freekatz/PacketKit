package gui.ctrl.list;

import gui.ctrl.IndexView;
import gui.ctrl.View;
import gui.model.JobMode;
import gui.model.ViewType;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.pcap4j.core.PcapNetworkInterface;
import util.ViewHandle;

import java.util.ArrayList;
import java.util.List;

public class NIFList {
    IndexView view;
    List<PcapNetworkInterface> pcapNifList;

    @FXML
    Button captureButton;

    @FXML
    MenuButton displayMenu;

    @FXML
    RadioMenuItem allItem;

    @FXML
    RadioMenuItem loopbackItem;

    @FXML
    RadioMenuItem upItem;

    @FXML
    RadioMenuItem runningItem;

    @FXML
    RadioMenuItem localItem;

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

    public NIFList() {}

    // add filter tf
    public void initialize() {

        captureButton.setTooltip(new Tooltip("start capture"));

        allItem.setSelected(true);
        localItem.setSelected(false);
        upItem.setSelected(false);
        loopbackItem.setSelected(false);
        runningItem.setSelected(false);
        pcapNifList = ViewHandle.GetPcapNIFList();
        this.UpdateNIFList();
        ToggleGroup group = new ToggleGroup();
        displayMenu.getItems().forEach(menuItem -> {
            ((RadioMenuItem) menuItem).setToggleGroup(group);
            menuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
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
                }
            }
        });

        nifList.getSelectionModel().select(0);
    }

    public void setView(IndexView view) {
        this.view = view;
        if (view.getType().equals(ViewType.IndexView))
            if (view.getNifName()==null && nifList.getItems().size()>0)
                view.setNifName(nifList.getItems().get(0).getId());
    }

    private void StartCapture(String name) {
        if (view.getType().equals(ViewType.IndexView)) {
            view.setType(ViewType.CaptureView);
            view.setPcapFile(null);
            PcapNetworkInterface nif=null;
            for (int i = 0; i < nifList.getItems().size(); i++) {
                Label label = (Label) nifList.getItems().get(i);
                if (label.getText().equals(name))
                    view.setNifName(label.getId());
            }
            ViewHandle.InitializeCaptureCenter(view);
        }
        view.JobScheduler(JobMode.OnlineJob);
    }

    private void UpdateNIFList() {
        nifList.getItems().clear();
        ArrayList<PcapNetworkInterface> t = new ArrayList<>(pcapNifList.size());
        t.addAll(pcapNifList);
        if (t.size()>0) {
            for (int i = 0; i < t.size(); i++) {
                if (allItem.isSelected())
                    break;
                PcapNetworkInterface pcapNif = t.get(i);
                if (loopbackItem.isSelected())
                    if (!pcapNif.isLoopBack())
                        t.remove(pcapNif);
                if (upItem.isSelected())
                    if (!pcapNif.isUp())
                        t.remove(pcapNif);
                if (runningItem.isSelected())
                    if (!pcapNif.isRunning())
                        t.remove(pcapNif);
                if (localItem.isSelected())
                    if (!pcapNif.isLocal())
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
    private void CaptureButtonOnClicked(Event event) {
        this.StartCapture(((Label)nifList.getSelectionModel().getSelectedItem()).getText());
    }
}
