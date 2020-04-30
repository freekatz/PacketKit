package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import gui.ctrl.bar.CaptureFilterBar;
import gui.ctrl.bar.CaptureMenuBar;
import gui.ctrl.bar.CaptureToolBar;
import gui.ctrl.View;
import gui.model.Property;
import gui.model.SettingProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.Pcaps;

import java.io.*;
import java.net.URL;
import java.util.List;

public class ViewHandle {

    public static FXMLLoader GetLoader(String path) {
        FXMLLoader loader = new FXMLLoader();
        URL url = loader.getClassLoader().getResource(path);
        loader.setLocation(url);

        return loader;
    }

    public static void InitializeCaptureTopBox(VBox box, View view) {
        try {
            FXMLLoader menuBarLoader = ViewHandle.GetLoader("gui/view/bar/CaptureMenuBar.fxml");
            AnchorPane menuBarPane = menuBarLoader.load();
            menuBarPane.setMaxWidth(Double.MAX_VALUE);
            CaptureMenuBar captureMenuBar = menuBarLoader.getController();
            captureMenuBar.setView(view);
            FXMLLoader toolBarLoader = ViewHandle.GetLoader("gui/view/bar/CaptureToolBar.fxml");
            AnchorPane toolBarPane = toolBarLoader.load();
            toolBarPane.setMaxWidth(Double.MAX_VALUE);
            CaptureToolBar captureToolBar = toolBarLoader.getController();
            captureToolBar.setView(view);
            FXMLLoader filterBarLoader = ViewHandle.GetLoader("gui/view/bar/CaptureFilterBar.fxml");
            AnchorPane filterBarPane = filterBarLoader.load();
            filterBarPane.setMaxWidth(Double.MAX_VALUE);
            CaptureFilterBar captureFilterBar = filterBarLoader.getController();
            captureFilterBar.setView(view);
            box.getChildren().addAll(menuBarPane, toolBarPane, filterBarPane);
            box.setSpacing(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void InitializeList(String path, ListView<String> list) {
        File file = new File(path);
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            ObservableList<String> ob = FXCollections.observableArrayList();
            String str = null;
            int num = 0;
            while((str = br.readLine()) != null && num < SettingProperty.maxPcapFileHistory)
            {
                File file1 = new File(str);
                FileInputStream fis1 = new FileInputStream(file1);
                if (!ob.contains(str + "(" + fis1.available()/1024 + "KB)")) {
                    ob.add(str + "(" + fis1.available()/1024 + "KB)");
                    num ++;
                }

            }
            list.setItems(ob);
            fis.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void InitializeComboBox(String path, ComboBox<String> box) {
        File file = new File(path);
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            ObservableList<String> ob = FXCollections.observableArrayList();
            String str = null;
            int num = 0;
            while((str = br.readLine()) != null && num < SettingProperty.maxFilterHistory)
            {
                if (!ob.contains(str)) {
                    ob.add(str);
                    num ++;
                }

            }
            box.setItems(ob);
            fis.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void UpdateConfigTable(String configPath, Property property, TableView<Property> table) {
        List<Property> list = FileHandle.ReadJson(configPath, property.getClass());

        assert list != null;
        list.forEach(p -> {
                table.getItems().add(p);
            });
    }
}
