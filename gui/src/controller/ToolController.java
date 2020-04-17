package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class Tool {
    @FXML
    Label label;

    public Tool() {

    }

    public void initialize() {
        System.out.println(label.getText());
    }
}
