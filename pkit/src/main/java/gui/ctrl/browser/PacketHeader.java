package gui.ctrl.browser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gui.ctrl.SendView;
import gui.ctrl.View;
import gui.model.browser.FieldProperty;
import gui.model.browser.PacketHeaderProperty;
import gui.model.packet.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.pcap4j.packet.Packet;
import util.PacketHandle;

import java.io.IOException;

public class PacketHeader {
    View view;

    int index = 0;

    @FXML
    TreeTableView<FieldProperty> headerTreeTable;

    TreeItem<FieldProperty> root;

    public PacketHeader() {
        root = new TreeItem<>();
    }

    public void initialize() {

        headerTreeTable.setEditable(true);


        TreeTableColumn<FieldProperty, String> fieldColumn = new TreeTableColumn<>();
        fieldColumn.setText("Field");
        fieldColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        fieldColumn.setPrefWidth(200);

        TreeTableColumn<FieldProperty, String> valueColumn = new TreeTableColumn<>();
        valueColumn.setText("Value");
        valueColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("value"));
        valueColumn.setPrefWidth(400);

        valueColumn.setCellFactory(new Callback<TreeTableColumn<FieldProperty, String>, TreeTableCell<FieldProperty, String>>() {
            @Override
            public TreeTableCell<FieldProperty, String> call(TreeTableColumn<FieldProperty, String> column) {

                TreeTableCell<FieldProperty, String> cell = new TreeTableCell<>() {


                    // TODO: 2020/5/3 提交编辑时的逻辑
                    @Override
                    public void startEdit() {
                        super.startEdit();

                        TextField textField = new TextField(this.getTreeTableRow().getItem().getValue());

                        this.setGraphic(textField);

                        textField.setAlignment(Pos.CENTER);

                        textField.requestFocus();

                        textField.deselect();

                        textField.end();

                        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                            @Override
                            public void handle(KeyEvent keyEvent) {
                                if (keyEvent.getCode()== KeyCode.ENTER){
                                    String newValue = textField.getText();
                                    commitEdit(newValue);
                                    textField.setFocusTraversable(false);
                                }

                            }
                        });

                    }

                    @Override
                    public void cancelEdit() {
                        super.cancelEdit();
                        updateItem(this.getTreeTableRow().getItem().getValue(), false);
                    }

                    @Override
                    public void commitEdit(String s) {
                        super.commitEdit(s);
                        SendView sendView = (SendView) view;
                        if (sendView.getPacketListCtrl().getPacketTable().getSelectionModel().getSelectedItem()!=null)
                            index = sendView.getPacketListCtrl().getPacketTable().getSelectionModel().getSelectedIndex();
                        PacketHeaderProperty packetHeaderProperty = sendView.packetPropertyArrayList.get(index).getHeader();

                        TreeItem<FieldProperty> item = headerTreeTable.getSelectionModel().getSelectedItem();
                        TreeItem<FieldProperty> header = item.getParent();
                        FieldProperty itemFieldProperty = item.getValue();
                        FieldProperty headerFieldProperty = header.getValue();

                        String headerName = headerFieldProperty.getName().replaceAll(" Header", "");
                        String itemName = itemFieldProperty.getName();


                        JsonMapper mapper = new JsonMapper();

                        String js = null;
                        JsonNode node = null;
                        Class clazz = null;
                        int i = 0;
                        try {
                            for (PPacket pPacket : packetHeaderProperty.getHeader()) {
                                i = packetHeaderProperty.getHeader().indexOf(pPacket);
                                if (pPacket.name().equals(headerName)) {
                                    switch (headerName) {
                                        case "Ethernet": {
                                            EthernetPPacket ethernetPPacket = (EthernetPPacket) pPacket;
                                            js = mapper.writeValueAsString(ethernetPPacket);
                                            node = mapper.readTree(js);
                                            ((ObjectNode) node).put(itemName, s);
                                            clazz = EthernetPPacket.class;
                                            break;
                                        }
                                        case "ARP": {
                                            ArpPPacket arpPPacket = (ArpPPacket) pPacket;
                                            js = mapper.writeValueAsString(arpPPacket);
                                            node = mapper.readTree(js);
                                            ((ObjectNode) node).put(itemName, s);
                                            clazz = ArpPPacket.class;
                                            break;
                                        }
                                        case "IPv4": {
                                            Ipv4PPacket ipv4PPacket = (Ipv4PPacket) pPacket;
                                            js = mapper.writeValueAsString(ipv4PPacket);
                                            node = mapper.readTree(js);
                                            ((ObjectNode) node).put(itemName, s);
                                            clazz = Ipv4PPacket.class;
                                            break;
                                        }
                                        case "IPv6": {
                                            Ipv6PPacket ipv6PPacket = (Ipv6PPacket) pPacket;
                                            js = mapper.writeValueAsString(ipv6PPacket);
                                            node = mapper.readTree(js);
                                            ((ObjectNode) node).put(itemName, s);
                                            clazz = Ipv6PPacket.class;
                                            break;
                                        }
                                        case "TCP": {
                                            TcpPPacket tcpPPacket = (TcpPPacket) pPacket;
                                            js = mapper.writeValueAsString(tcpPPacket);
                                            node = mapper.readTree(js);
                                            ((ObjectNode) node).put(itemName, s);
                                            clazz = TcpPPacket.class;
                                            break;
                                        }
                                        case "UDP": {
                                            UdpPPacket udpPPacket = (UdpPPacket) pPacket;
                                            js = mapper.writeValueAsString(udpPPacket);
                                            node = mapper.readTree(js);
                                            ((ObjectNode) node).put(itemName, s);
                                            clazz = UdpPPacket.class;
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }

                            js = mapper.writeValueAsString(node);
                            packetHeaderProperty.getHeader().set(i, (PPacket) mapper.readValue(js, clazz));

                            // // TODO: 2020/5/5  eth bug fix + packet to pcappacket


                            Packet packet = PacketHandle.Restore(sendView.packetPropertyArrayList.get(index));


                            PacketHandle.Parse(packet, sendView.packetPropertyArrayList.get(index));

                            sendView.getPacketListCtrl().getPacketTable().getItems().set(index, sendView.packetPropertyArrayList.get(index).getInfo());

                        } catch (IOException ignored) {
                        }

                        updateItem(s, false);
                    }

                    @Override
                    protected void updateItem(String s, boolean b) {
                        super.updateItem(s, b);

                        HBox hBox = new HBox();

                        Label label = new Label(s);

                        hBox.getChildren().add(label);

                        hBox.setAlignment(Pos.CENTER);
                        if (!b) {
                            this.setGraphic(hBox);
                        } else {
                            this.setGraphic(null);
                        }
                    }
                };

                return cell;
            }
        });

        headerTreeTable.getColumns().add(fieldColumn);
        headerTreeTable.getColumns().add(valueColumn);

    }

    public void setEdit(boolean editable) {
        headerTreeTable.setEditable(editable);
    }

    public TreeItem<FieldProperty> getRoot() {
        return root;
    }

    public void setView(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public TreeTableView<FieldProperty> getHeaderTreeTable() {
        return headerTreeTable;
    }

    public void setHeaderTreeTable(TreeTableView<FieldProperty> headerTreeTable) {
        this.headerTreeTable = headerTreeTable;
    }
}
