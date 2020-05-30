package gui.ctrl.single;

import gui.ctrl.IndexView;
import gui.ctrl.View;
import gui.model.AnalysisMenuProperty;
import gui.model.JobMode;
import gui.model.SettingProperty;
import gui.model.ViewType;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import util.ViewHandle;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;

public class AnalysisView implements View {

    IndexView view;
    String path;

    WebView webView = new WebView();
    WebEngine webEngine = webView.getEngine();
    TreeView<String> menuTree;

    @FXML
    AnchorPane menuPane;

    @FXML
    AnchorPane chartPane;
    private ViewType type;

    public AnalysisView() {
        this.type = ViewType.AnalysisView;
    }

    public void initialize() {

        /*
        create menu
        */
        AnalysisMenuProperty menuProperty = new AnalysisMenuProperty();
        LinkedHashMap<String, String> traffic = new LinkedHashMap<>();
//        traffic.put("NIF Statistic", settingProperty.nifStatTableChartPath);
        traffic.put("IO LineChart", SettingProperty.ioLineChartPath);
        traffic.put("Protocol PieChart", SettingProperty.protocolPieChartPath);
        traffic.put("IPv4 Statistic", SettingProperty.ipv4StatBarChartPath);
        traffic.put("IPv6 Statistic", SettingProperty.ipv6StatBarChartPath);

        LinkedHashMap<String, String> communication = new LinkedHashMap<>();
        communication.put("S2OSankeyChart", SettingProperty.s2oSankeyChartPath);
        communication.put("S2SankeyChart", SettingProperty.s2sSankeyChartPath);
        communication.put("O2SankeyChart", SettingProperty.o2sSankeyChartPath);

        LinkedHashMap<String, String> relation = new LinkedHashMap<>();

        relation.put("Network", SettingProperty.networkChartPath);

//        LinkedHashMap<String, String> description = new LinkedHashMap<>();

        menuProperty.setTraffic(traffic);
        menuProperty.setCommunication(communication);
        menuProperty.setRelation(relation);
//        menuProperty.setDescription(description);

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

        this.setPath(SettingProperty.analysisWelcomePath);

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
//                else if (description.containsKey(name))
//                    p = description.get(name);
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

    public void setView(IndexView view) {
        this.view = view;
        view.JobScheduler(JobMode.AnalysisJob);
    }

    @Override
    public ViewType getType() {
        return this.type;
    }

    @Override
    public void setType(ViewType type) {
        this.type = type;
    }

    @Override
    public void JobScheduler(JobMode jobMode) {

    }

    @Override
    public void JobStop() {

    }

    @Override
    public void close(Event event) {

    }
}
