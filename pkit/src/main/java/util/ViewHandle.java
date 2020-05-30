package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import gui.ctrl.IndexView;
import gui.ctrl.SendView;
import gui.ctrl.View;
import gui.ctrl.bar.*;
import gui.ctrl.browser.PacketData;
import gui.ctrl.browser.PacketHeader;
import gui.ctrl.browser.PacketList;
import gui.ctrl.list.FileList;
import gui.ctrl.list.NIFList;
import gui.model.Property;
import gui.model.SettingProperty;
import gui.model.ViewType;
import gui.model.browser.FieldProperty;
import gui.model.history.CapturePcapFileHistoryProperty;
import gui.model.history.FilterHistoryProperty;
import gui.model.history.SendPcapFileHistoryProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

public class ViewHandle {

    public static void InitializeCaptureTop(IndexView indexView) {
        VBox box = indexView.getTopBox();
        try {
            FXMLLoader menuBarLoader = new FXMLLoader();
            AnchorPane menuBarPane = menuBarLoader.load(menuBarLoader.getClassLoader().getResourceAsStream(SettingProperty.captureMenuBar));
            menuBarPane.setMaxWidth(Double.MAX_VALUE);
            CaptureMenuBar captureMenuBar = menuBarLoader.getController();
            captureMenuBar.setView(indexView);
            indexView.setCaptureMenuBarCtrl(captureMenuBar);
            FXMLLoader toolBarLoader = new FXMLLoader();
            AnchorPane toolBarPane = toolBarLoader.load(toolBarLoader.getClassLoader().getResourceAsStream(SettingProperty.captureToolBar));
            toolBarPane.setMaxWidth(Double.MAX_VALUE);
            CaptureToolBar captureToolBar = toolBarLoader.getController();
            captureToolBar.setView(indexView);
            indexView.setCaptureToolBarCtrl(captureToolBar);
            FXMLLoader filterBarLoader = new FXMLLoader();
            AnchorPane filterBarPane = filterBarLoader.load(filterBarLoader.getClassLoader().getResourceAsStream(SettingProperty.filterBar));
            filterBarPane.setMaxWidth(Double.MAX_VALUE);
            FilterBar filterBar = filterBarLoader.getController();
            filterBar.setView(indexView);
            indexView.setFilterBarCtrl(filterBar);
            box.getChildren().addAll(menuBarPane, toolBarPane, filterBarPane);
            box.setSpacing(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void InitializeCaptureCenter(IndexView view) {
        if (view.getType().equals(ViewType.IndexView)) {
            try {
                VBox centerBox = view.getCenterBox();
                view.getPane().setCenter(centerBox);
                if (centerBox.getChildren().size()>0)
                    return;
                FXMLLoader nifListLoader = new FXMLLoader();
                AnchorPane nifListPane = nifListLoader.load(nifListLoader.getClassLoader().getResourceAsStream(SettingProperty.nifList));
                nifListPane.setMaxWidth(Double.MAX_VALUE);
                NIFList nifList = nifListLoader.getController();
                nifList.setView(view);
                view.setNifListCtrl(nifList);
                FXMLLoader fileListLoader = new FXMLLoader();
                AnchorPane fileListPane = fileListLoader.load(fileListLoader.getClassLoader().getResourceAsStream(SettingProperty.fileList));
                fileListPane.setMaxWidth(Double.MAX_VALUE);
                FileList fileList = fileListLoader.getController();
                fileList.setView(view);
                view.setFileListCtrl(fileList);
                centerBox.getChildren().addAll(fileListPane, nifListPane);
                centerBox.setSpacing(10);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                SplitPane browserPane = view.getBrowserPane();
                view.getPane().setCenter(browserPane);
                if (browserPane.getItems().size()>0)
                    return;

                SplitPane detailPane = new SplitPane();
                detailPane.setOrientation(Orientation.HORIZONTAL);

                FXMLLoader packetListLoader = new FXMLLoader();
                AnchorPane packetListPane = packetListLoader.load(packetListLoader.getClassLoader().getResourceAsStream(SettingProperty.packetList));
                packetListPane.setMaxWidth(Double.MAX_VALUE);
                PacketList packetList = packetListLoader.getController();
                packetList.setView(view);
                view.setPacketListCtrl(packetList);
                FXMLLoader packetHeaderLoader = new FXMLLoader();
                AnchorPane packetHeaderPane = packetHeaderLoader.load(packetHeaderLoader.getClassLoader().getResourceAsStream(SettingProperty.packetHeader));
                packetHeaderPane.setMaxWidth(Double.MAX_VALUE);
                PacketHeader packetHeader = packetHeaderLoader.getController();
                packetHeader.setView(view);
                view.setPacketHeaderCtrl(packetHeader);

                TreeItem<FieldProperty> root = packetHeader.getRoot();
                packetHeader.getHeaderTreeTable().setRoot(root);
                packetHeader.getHeaderTreeTable().setShowRoot(false);

                FXMLLoader packetDataLoader = new FXMLLoader();
                AnchorPane packetDataPane = packetDataLoader.load(packetDataLoader.getClassLoader().getResourceAsStream(SettingProperty.packetData));
                packetDataPane.setMaxWidth(Double.MAX_VALUE);
                PacketData packetData = packetDataLoader.getController();
                packetData.setView(view);
                view.setPacketDataCtrl(packetData);

                detailPane.getItems().addAll(packetHeaderPane, packetDataPane);
                detailPane.setDividerPositions(0.45, 0.55);

                browserPane.getItems().addAll(packetListPane, detailPane);


                for (int i = 0; i < browserPane.getDividers().size(); i++) {
                    browserPane.getDividers().get(i).setPosition((i + 1.0) / browserPane.getItems().size());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void InitializeCaptureBottom(IndexView view) {
        BorderPane pane = view.getPane();
        try {
            FXMLLoader statusBarLoader = new FXMLLoader();
            AnchorPane statusBatPane = statusBarLoader.load(statusBarLoader.getClassLoader().getResourceAsStream(SettingProperty.captureStatusBar));
            statusBatPane.setMaxWidth(Double.MAX_VALUE);
            CaptureStatusBar captureStatusBar = statusBarLoader.getController();
            captureStatusBar.setView(view);
            pane.setBottom(statusBatPane);
            pane.getBottom().setLayoutX(0);
            view.setCaptureStatusBarCtrl(captureStatusBar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void InitializeSendTop(SendView view) {
        VBox box = view.getTopBox();
        try {
            FXMLLoader menuBarLoader = new FXMLLoader();
            AnchorPane menuBarPane = menuBarLoader.load(menuBarLoader.getClassLoader().getResourceAsStream(SettingProperty.sendMenuBar));
            menuBarPane.setMaxWidth(Double.MAX_VALUE);
            SendMenuBar sendMenuBar = menuBarLoader.getController();
            sendMenuBar.setView(view);
            view.setSendMenuBarCtrl(sendMenuBar);
            FXMLLoader toolBarLoader = new FXMLLoader();
            AnchorPane toolBarPane = toolBarLoader.load(toolBarLoader.getClassLoader().getResourceAsStream(SettingProperty.sendToolBar));
            toolBarPane.setMaxWidth(Double.MAX_VALUE);
            SendToolBar sendToolBar = toolBarLoader.getController();
            sendToolBar.setView(view);
            view.setSendToolBarCtrl(sendToolBar);
            box.getChildren().addAll(menuBarPane, toolBarPane);
            box.setSpacing(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void InitializeSendCenter(SendView view) {
        try {
            SplitPane browserPane = view.getBrowserPane();
            if (browserPane.getItems().size()>0)
                return;

            FXMLLoader packetListLoader = new FXMLLoader();
            AnchorPane packetListPane = packetListLoader.load(packetListLoader.getClassLoader().getResourceAsStream(SettingProperty.packetList));
            packetListPane.setMaxWidth(Double.MAX_VALUE);
            PacketList packetList = packetListLoader.getController();
            packetList.setView(view);
            view.setPacketListCtrl(packetList);
            FXMLLoader packetHeaderLoader = new FXMLLoader();
            AnchorPane packetHeaderPane = packetHeaderLoader.load(packetHeaderLoader.getClassLoader().getResourceAsStream(SettingProperty.packetHeader));
            packetHeaderPane.setMaxWidth(Double.MAX_VALUE);
            PacketHeader packetHeader = packetHeaderLoader.getController();
            packetHeader.setView(view);
            view.setPacketHeaderCtrl(packetHeader);

            TreeItem<FieldProperty> root = packetHeader.getRoot();
            packetHeader.getHeaderTreeTable().setRoot(root);
            packetHeader.getHeaderTreeTable().setShowRoot(false);


            browserPane.getItems().addAll(packetHeaderPane, packetListPane);
            browserPane.setDividerPositions(0.45, 0.55);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void InitializeSendBottom(SendView view) {
        BorderPane pane = view.getPane();
        try {
            FXMLLoader statusBarLoader = new FXMLLoader();
            AnchorPane statusBatPane = statusBarLoader.load(statusBarLoader.getClassLoader().getResourceAsStream(SettingProperty.sendStatusBar));
            statusBatPane.setMaxWidth(Double.MAX_VALUE);
            SendStatusBar sendStatusBar = statusBarLoader.getController();
            sendStatusBar.setView(view);
            pane.setBottom(statusBatPane);
            pane.getBottom().setLayoutX(0);
            view.setSendStatusBarCtrl(sendStatusBar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void InitializePcapFileList(String path, ListView<String> list) {

        SettingProperty settingProperty = new SettingProperty();
        JsonMapper mapper = new JsonMapper();
        try {
            CapturePcapFileHistoryProperty property = mapper.readValue(new File(path), CapturePcapFileHistoryProperty.class);
            HashSet<String> hashSet;
            hashSet = property.getHistory();

            ObservableList<String> ob = FXCollections.observableArrayList();
            int num = 0;
            for (String f : hashSet) {
                if (num < settingProperty.maxPcapFileHistory) {
                    File file1 = new File(f);
                    FileInputStream fis1 = new FileInputStream(file1);
                    String item;
                    if (fis1.available()/1024==0)
                        item = f + "(" + ((double)fis1.available())/1024 + " KB)";
                    else item = f + "(" + fis1.available()/1024 + " KB)";
                    ob.add(item);
                    num ++;
                }
            }
            list.setItems(ob);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void InitializeCapturePcapFileMenu(String path, Menu menu) {

        SettingProperty settingProperty = new SettingProperty();
        menu.getItems().remove(2,menu.getItems().size());
        JsonMapper mapper = new JsonMapper();
        try {
            CapturePcapFileHistoryProperty property = mapper.readValue(new File(path), CapturePcapFileHistoryProperty.class);
            HashSet<String> hashSet;
            hashSet = property.getHistory();

            int num = 0;
            ToggleGroup group = new ToggleGroup();
            for (String f : hashSet) {
                if (num < settingProperty.maxPcapFileHistory) {
                    File file1 = new File(f);
                    if (!file1.exists()) FileHandle.RemoveHistory(path, f, CapturePcapFileHistoryProperty.class);
                    FileInputStream fis1 = new FileInputStream(file1);
                    String item;
                    if (fis1.available()/1024==0)
                        item = f + "(" + ((double)fis1.available())/1024 + " KB)";
                    else item = f + "(" + fis1.available()/1024 + " KB)";
                    RadioMenuItem menuItem = new RadioMenuItem(item);
                    menuItem.setToggleGroup(group);
                    menuItem.setSelected(false);
                    menu.getItems().add(menuItem);
                    num ++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void InitializeSendPcapFileMenu(String path, Menu menu) {

        SettingProperty settingProperty = new SettingProperty();
        menu.getItems().remove(2,menu.getItems().size());
        JsonMapper mapper = new JsonMapper();
        try {
            SendPcapFileHistoryProperty property = mapper.readValue(new File(path), SendPcapFileHistoryProperty.class);
            HashSet<String> hashSet;
            hashSet = property.getHistory();

            int num = 0;
            ToggleGroup group = new ToggleGroup();
            for (String f : hashSet) {
                if (num < settingProperty.maxPcapFileHistory) {
                    File file1 = new File(f);
                    if (!file1.exists()) FileHandle.RemoveHistory(path, f, SendPcapFileHistoryProperty.class);
                    FileInputStream fis1 = new FileInputStream(file1);
                    String item;
                    if (fis1.available()/1024==0)
                        item = f + "(" + ((double)fis1.available())/1024 + " KB)";
                    else item = f + "(" + fis1.available()/1024 + " KB)";
                    RadioMenuItem menuItem = new RadioMenuItem(item);
                    menuItem.setSelected(false);
                    menuItem.setToggleGroup(group);
                    menu.getItems().add(menuItem);
                    num ++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void InitializeNifComboBox(ComboBox<String> box) {
        List pcapNifList = ViewHandle.GetPcapNIFList();

        pcapNifList.forEach(p->{
            PcapNetworkInterface nif = (PcapNetworkInterface) p;
            box.getItems().add(nif.getName());
        });

        box.setValue("");
    }

    public static void InitializeFilterComboBox(String path, ComboBox<String> box) {

        String s = box.getValue();
        if (box.getItems().size()==0) {
            SettingProperty settingProperty = new SettingProperty();
            JsonMapper mapper = new JsonMapper();
            try {
                FilterHistoryProperty property = mapper.readValue(new File(path), FilterHistoryProperty.class);
                HashSet<String> hashSet;
                hashSet = property.getHistory();

                ObservableList<String> ob = FXCollections.observableArrayList();
                int num = 0;
                for (String f : hashSet) {
                    if (num < settingProperty.maxFilterHistory) {
                        ob.add(f);
                        num++;
                    }
                }
                box.setItems(ob);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (!box.getItems().contains(s))
                box.getItems().add(s);
        }

        box.setValue(s);
    }

    public static List GetPcapNIFList() {
        List pcapNifList = null;
        try {
            pcapNifList = Pcaps.findAllDevs();
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }

        return pcapNifList;
    }

    public static void InitializeTable(Property property, TableView<Property> table) {
        JsonMapper mapper = new JsonMapper();
        try {
            String json = mapper.writeValueAsString(property);
            JsonNode node = mapper.readTree(json);
            if (table.getColumns().size() == 0) {
                node.fields().forEachRemaining(field -> {
                    TableColumn<Property, Object> column = new TableColumn<>();
                    column.setText(field.getKey());
                    column.setId(field.getKey());
                    column.setCellValueFactory(new PropertyValueFactory<>(field.getKey()));
                    table.getColumns().add(column);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void UpdateConfigTable(String configPath, Property property, TableView<Property> table) {
        List<Property> list = FileHandle.ReadConfig(configPath, property.getClass());

        assert list != null;
        list.forEach(p -> {
                table.getItems().add(p);
            });
    }

    public static TreeView<String> InitializeMenuTree(Property property) {
        JsonMapper mapper = new JsonMapper();
        try {
            String js = mapper.writeValueAsString(property);
            JsonNode root = mapper.readTree(js);
            TreeItem<String> rootItem = new TreeItem<>(root.asText());
            root.fieldNames().forEachRemaining(field -> {
                if (!field.equals("name")) {
                    TreeItem<String> item = new TreeItem<>(field);
                    JsonNode node = root.path(field);
                    rootItem.getChildren().add(item);
                    node.fieldNames().forEachRemaining(f -> {
                        TreeItem<String> it = new TreeItem<>(f);
                        item.getChildren().add(it);

                    });
                }
            });

            return new TreeView<>(rootItem);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
