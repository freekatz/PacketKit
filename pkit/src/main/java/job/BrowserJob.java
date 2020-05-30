package job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import gui.ctrl.IndexView;
import gui.ctrl.SendView;
import gui.ctrl.View;
import gui.model.ViewType;
import gui.model.browser.FieldProperty;
import gui.model.browser.PacketDataProperty;
import gui.model.browser.PacketHeaderProperty;
import gui.model.browser.PacketProperty;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import util.TypeHandle;

public class BrowserJob extends Task<PacketProperty> {

    View view;
    PacketProperty packetProperty;
    ListView<HBox> dataArea;
    TreeItem<FieldProperty> header;

    public BrowserJob(PacketProperty packetProperty, View view) {
        this.view = view;
        this.packetProperty = packetProperty;

        if (view.getType().equals(ViewType.CaptureView)) {
            IndexView indexView = (IndexView) view;
            this.dataArea = indexView.getPacketDataCtrl().getDataArea();
            this.header = indexView.getPacketHeaderCtrl().getRoot();
        } else if (view.getType().equals(ViewType.SendView)) {
            SendView sendView = (SendView) view;
            this.header = sendView.getPacketHeaderCtrl().getRoot();
        }
        // else send

    }

    @Override
    protected void updateValue(PacketProperty packetProperty) {
        super.updateValue(packetProperty);
        if (view.getType().equals(ViewType.CaptureView)) {
            this.InitializeHeaderTreeTable();
            this.InitializeDataArea();
        } else if (view.getType().equals(ViewType.SendView)) {
            this.InitializeHeaderTreeTable();
        }
        // else send

    }

    @Override
    protected PacketProperty call() throws Exception {
        return packetProperty;
    }


    private void InitializeDataArea() {

        PacketDataProperty property = this.packetProperty.getData();


        dataArea.getItems().clear();

        char[] txt = property.getTxt();
        String[] hex = property.getHex();

        byte lineLimit = 16;
        byte batchLimit = 8;
        HBox hBox = new HBox();
        HBox hexBox = new HBox();
        HBox txtBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hexBox.setAlignment(Pos.CENTER_LEFT);
        txtBox.setAlignment(Pos.CENTER_LEFT);
        for (int i = 0; i < hex.length; i++) {
            if (i!=0 && i%batchLimit==0) {
                Label l1 = new Label(" ");
                l1.setMinWidth(20.0);
                Label l2 = new Label(" ");
                l2.setMinWidth(10.0);
                hexBox.getChildren().add(l1);
                txtBox.getChildren().add(l2);
            }
            if (i!=0 && i%lineLimit==0) {
                Label l = new Label(TypeHandle.IntToHex(i-16));
                l.setMinWidth(70);
                hBox.getChildren().add(l);
                hBox.getChildren().add(hexBox);
                hBox.getChildren().add(txtBox);
                dataArea.getItems().add(hBox);
                hBox = new HBox();
                hexBox = new HBox();
                txtBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                hexBox.setAlignment(Pos.CENTER_LEFT);
                txtBox.setAlignment(Pos.CENTER_LEFT);
            }

            Label h = new Label(hex[i] + " ");
            h.setMinWidth(20.0);
            Label t = new Label(String.valueOf(txt[i]));
            t.setMinWidth(10.0);
            hexBox.getChildren().add(h);
            txtBox.getChildren().add(t);
        }
        Label l = new Label(TypeHandle.IntToHex((hex.length/16)*16));
        l.setMinWidth(70);
        hBox.getChildren().add(l);

        while (hexBox.getChildren().size()<18){
            Label l1 = new Label(" ");
            l1.setMinWidth(20.0);
            Label l2 = new Label(" ");
            l2.setMinWidth(10.0);
            hexBox.getChildren().add(l1);
            txtBox.getChildren().add(l2);
        }

        hBox.getChildren().add(hexBox);
        hBox.getChildren().add(txtBox);
        dataArea.getItems().add(hBox);

    }

    private void InitializeHeaderTreeTable() {
        PacketHeaderProperty property = this.packetProperty.getHeader();

        JsonMapper mapper = new JsonMapper();
        header.getChildren().clear();
        try {
            String js = mapper.writeValueAsString(property);
            JsonNode root = mapper.readTree(js).path("header");
            root.iterator().forEachRemaining(pPacket->{
                FieldProperty headItemField = new  FieldProperty(pPacket.path("name").toString().replaceAll("\"", "") + " Header", "");
                TreeItem<FieldProperty> headerItem = new TreeItem<>(headItemField);

                if (view.getType().equals("send"))
                    headerItem.setExpanded(true);

                pPacket.fieldNames().forEachRemaining(field->{
                    if (!field.equals("name")) {
                        FieldProperty itemField = new FieldProperty(field, pPacket.path(field).toString().replaceAll("\"", ""));
                        TreeItem<FieldProperty> item = new TreeItem<>(itemField);
                        headerItem.getChildren().add(item);
                    }
                });
                header.getChildren().add(headerItem);
            });


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
