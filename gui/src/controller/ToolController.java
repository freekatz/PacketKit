package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class ToolController {
    @FXML
    Label label;

    public ToolController() {

    }

    public void initialize() {
        System.out.println(label.getText());
    }
}
