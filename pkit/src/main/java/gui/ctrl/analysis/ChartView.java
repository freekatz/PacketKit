package gui.ctrl.analysis;

import gui.ctrl.bar.CaptureMenuBar;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class ChartView {
    CaptureMenuBar captureMenuBar;
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
            System.out.println("Local URL: " + url.toString());
            webEngine.load(url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setCaptureMenuBar(CaptureMenuBar captureMenuBar) {
        this.captureMenuBar = captureMenuBar;
    }
}