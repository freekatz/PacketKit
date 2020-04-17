package pkit.gui.controller.module.tool;

// 捕获时顶部的工具栏

import cn.edu.scau.biubiusuisui.annotation.FXController;
import cn.edu.scau.biubiusuisui.annotation.FXWindow;
import cn.edu.scau.biubiusuisui.entity.FXBaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


@FXController(path = "pkit/gui/view/module/tool/CaptureTool.fxml")
public class CaptureTool extends FXBaseController {

    @FXML
    Button button;

    @FXML
    Label label;

    @Override
    public void initialize() {

    }

}

