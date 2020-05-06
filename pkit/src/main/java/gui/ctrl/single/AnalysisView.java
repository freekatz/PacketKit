package gui.ctrl;

import gui.model.AnalysisMenuProperty;
import gui.model.SettingProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import util.ViewHandle;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;

public class AnalysisView implements View{
    SettingProperty settingProperty = new SettingProperty();

    View view;
    String path;

    WebView webView = new WebView();
    WebEngine webEngine = webView.getEngine();
    TreeView<String> menuTree;

    @FXML
    AnchorPane menuPane;

    @FXML
    AnchorPane chartPane;

    public AnalysisView() {}

    public void initialize() {

        /*
        create menu
        */
        AnalysisMenuProperty menuProperty = new AnalysisMenuProperty();
        LinkedHashMap<String, String> traffic = new LinkedHashMap<>();
        traffic.put("NIF Statistic", "NIF Statistic1");
        traffic.put("IO LineChart", settingProperty.ioLineChartPath);
        traffic.put("Protocol PieChart", settingProperty.protocolPieChartPath);
        traffic.put("IPv4 Statistic", settingProperty.ipv4StatBarChartPath);
        traffic.put("IPv6 Statistic", settingProperty.ipv6StatBarChartPath);

        LinkedHashMap<String, String> communication = new LinkedHashMap<>();
        LinkedHashMap<String, String> relation = new LinkedHashMap<>();
        LinkedHashMap<String, String> description = new LinkedHashMap<>();

        menuProperty.setTraffic(traffic);
        menuProperty.setCommunication(communication);
        menuProperty.setRelation(relation);
        menuProperty.setDescription(description);

        menuTree = ViewHandle.InitializeMenuTree(menuProperty);
        assert menuTree != null;
        menuTree.setShowRoot(false);

        menuPane.getChildren().add(menuTree);
        menuPane.setPrefWidth(200);
        AnchorPane.setTopAnchor(menuTree, 0.0);
        AnchorPane.setLeftAnchor(menuTree, 0.0);
        AnchorPane.setRightAnchor(menuTree, 0.0);
        AnchorPane.setBottomAnchor(menuTree, 0.0);

        chartPane.getChildren().add(webView);
        webView.setPrefHeight(600);
        webView.setPrefWidth(800);

        AnchorPane.setTopAnchor(webView, 0.0);
        AnchorPane.setLeftAnchor(webView, 0.0);
        AnchorPane.setRightAnchor(webView, 0.0);
        AnchorPane.setBottomAnchor(webView, 0.0);

        this.setPath(settingProperty.analysisWelcomePath);

        menuTree.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (menuTree.getSelectionModel().getSelectedItem()==null)
                    return;
                String name = (String) ((TreeItem)menuTree.getSelectionModel().getSelectedItem()).getValue();
                String p;
                if (traffic.containsKey(name))
                    p = traffic.get(name);
                else if (communication.containsKey(name))
                    p = communication.get(name);
                else if (relation.containsKey(name))
                    p = relation.get(name);
                else if (description.containsKey(name))
                    p = description.get(name);
                else return;
                if (p.equals(path))
                    return;
                setPath(p);

            }
        });


    }

    private void setPath(String path) {
        this.path = path;
        try {
            File file = new File(path);
            URL url = file.toURI().toURL();
            webEngine.load(url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
        IndexView indexView = (IndexView) view;
        indexView.StartCapture("analysis");
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public void setType(String type) {

    }

    @Override
    public void close(Event event) {

    }
}
