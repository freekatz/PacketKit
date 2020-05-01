package gui.ctrl;

import gui.ctrl.bar.MenuBar;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class ChartView implements View{
    MenuBar menuBar;
    String path;

    WebView webView = new WebView();
    WebEngine webEngine = webView.getEngine();

    @FXML
    AnchorPane pane;

    public ChartView() {}

    public void initialize() {
        pane.getChildren().add(webView);
        webView.setPrefHeight(600);
        webView.setPrefWidth(900);
    }

    public void setPath(String path) {
        this.path = path;
        try {
            File file = new File(path);
            URL url = file.toURI().toURL();
            webEngine.load(url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setMenuBar(MenuBar menuBar) {
        this.menuBar = menuBar;
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