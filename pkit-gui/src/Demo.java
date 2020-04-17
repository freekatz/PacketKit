package pkit.gui;

import cn.edu.scau.biubiusuisui.annotation.FXScan;
import cn.edu.scau.biubiusuisui.config.FXPlusApplication;
import javafx.application.Application;
import javafx.stage.Stage;

@FXScan(base = {"pkit.gui.controller.module.tool"})
public class Demo extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXPlusApplication.start(Demo.class);
//        stage.show();
    }
}
