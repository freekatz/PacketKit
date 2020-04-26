package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.NIFProperty;
import model.Property;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TableHandle {

    public static void InitializeTable(Property property, TableView<Property> table) throws IOException {
        JsonMapper mapper = new JsonMapper();
        String json = mapper.writeValueAsString(property);
        JsonNode node = mapper.readTree(json);
        ObservableList<Property> observableList = FXCollections.observableArrayList();
        if (table.getColumns().size()==0) {
            node.fields().forEachRemaining(field -> {
                TableColumn<Property, Object> column = new TableColumn<>();
                column.setText(field.getKey());
                column.setId(field.getKey());
                column.setCellValueFactory(new PropertyValueFactory<>(field.getKey()));
                table.getColumns().add(column);
            });
        }
        table.setItems(observableList);
        table.mouseTransparentProperty().setValue(false);
        table.fixedCellSizeProperty().setValue(50);
    }

    public static void UpdateConfigTable(String configPath, Property property, TableView<Property> table) throws IOException {
        File[] configList = ConfigHandle.ConfigScan(configPath);
        JsonMapper mapper = new JsonMapper();
        for (File config : configList)
            table.getItems().add(mapper.readValue(config, property.getClass()));
    }

    public static void UpdateNIFTable(TableView<Property> table) {
        List nifList = null;
        try {
            nifList = Pcaps.findAllDevs();
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }
        PcapNetworkInterface nif;
        if (nifList != null && nifList.size() != 0) {
            for (Object o : nifList) {
                nif = (PcapNetworkInterface) o;
                NIFProperty nifProperty = new NIFProperty(nif);
                table.getItems().add(nifProperty);
            }
        }
    }

}
