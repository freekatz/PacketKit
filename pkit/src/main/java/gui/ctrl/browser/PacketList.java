package gui.ctrl.browser;

import gui.ctrl.IndexView;
import gui.ctrl.SendView;
import gui.ctrl.View;
import gui.model.browser.PacketInfoProperty;
import gui.model.Property;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import util.ViewHandle;
import util.job.BrowserJob;

public class PacketList {
    View view;

    @FXML
    TableView<Property> packetTable;

    public PacketList() {}

    public void initialize() {
        ViewHandle.InitializeTable(new PacketInfoProperty(), packetTable);
        packetTable.getColumns().get(0).setVisible(false);

        packetTable.getColumns().get(1).setPrefWidth(50);
        packetTable.getColumns().get(2).setPrefWidth(200);
        packetTable.getColumns().get(3).setPrefWidth(200);
        packetTable.getColumns().get(4).setPrefWidth(150);
        packetTable.getColumns().get(5).setPrefWidth(100);
        packetTable.getColumns().get(6).setPrefWidth(50);
        packetTable.getColumns().get(7).setPrefWidth(600);

        packetTable.addEventHandler(MouseEvent.MOUSE_CLICKED, (event)-> {
            String type = view.getType();
            if (packetTable.getSelectionModel().getSelectedItem()!=null) {
                int index = packetTable.getSelectionModel().getSelectedIndex();
                if (type.equals("capture")) {
                    IndexView indexView = (IndexView) view;
                    BrowserJob job = new BrowserJob(indexView.packetPropertyArrayList.get(index), indexView);
                    Thread thread = new Thread(job);
                    thread.start();
                } else if (type.equals("send")) {
                    SendView sendView = (SendView) view;

                    sendView.setPacketProperty(sendView.packetPropertyArrayList.get(index));
                    BrowserJob job = new BrowserJob(sendView.packetPropertyArrayList.get(index), sendView);
                    Thread thread = new Thread(job);
                    thread.start();
                }
            }
        });

    }

    public void setView(View view) {
        this.view = view;
        if (view.getType().equals("send")) packetTable.getColumns().get(1).setVisible(false);
    }

    public TableView<Property> getPacketTable() {
        return packetTable;
    }
}
