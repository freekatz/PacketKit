package util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import config.CaptureFilterConfig;
import config.Config;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import nif.CaptureNetworkInterface;
import nif.NetworkInterface;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TableHandle {

    public static void UpdateTable(String configPath, Config configObject, TableView<Config> table) throws IOException {
        File[] configList = ConfigHandle.ConfigScan(configPath);
        JsonMapper mapper = new JsonMapper();
        ObservableList<Config> observableList = FXCollections.observableArrayList();
        for (File config : configList) {
            JsonNode node;
            node = mapper.readTree(config);
            if (table.getColumns().size() == 0) {
                node.fields().forEachRemaining(field -> {
                    if (!field.getKey().equals("filterConfig")) {
                        TableColumn<Config, Object> column = new TableColumn<>();
                        column.setText(field.getKey());
                        column.setId(field.getKey());
                        column.setCellValueFactory(new PropertyValueFactory<>(field.getKey()));
                        table.getColumns().add(column);
                    }
                });
            }
            observableList.add(mapper.readValue(config, configObject.getClass()));
        }

        table.setItems(observableList);
        table.mouseTransparentProperty().setValue(false);
        table.fixedCellSizeProperty().setValue(50);
    }

    // TODO: 2020/4/19 nif stat class
    public static void UpdateTable(CaptureNetworkInterface networkInterface, TableView<HashMap<String, String>> table) throws IOException {

        JsonMapper mapper = new JsonMapper();
        ObservableList<HashMap<String, String>> observableList = FXCollections.observableArrayList();
        TableColumn<String, String> keyColumn = new TableColumn<>();
        keyColumn.setText("statKey");
        keyColumn.setText("statKey");
        TableColumn<String, String> valueColumn = new TableColumn<>();
        valueColumn.setText("statValue");
        valueColumn.setText("statValue");
        JsonNode node;
        node = mapper.readTree(String.valueOf(networkInterface));
        HashMap<String, String> hashMap = new HashMap<>();
        node.fields().forEachRemaining(field -> {
            hashMap.put(field.getKey(), field.getValue().textValue());
        });
        observableList.add(hashMap);
        table.setItems(observableList);
        table.mouseTransparentProperty().setValue(false);
        table.fixedCellSizeProperty().setValue(50);
    }
}
