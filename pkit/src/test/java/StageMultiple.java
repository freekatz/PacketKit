package pkit.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StageMultiple extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // 五种类型的舞台
        Stage stage1 = new Stage();
        stage1.initStyle(StageStyle.DECORATED);
        stage1.setTitle("S1");

        stage1.show();

        // 透明的
        Stage stage2 = new Stage();
        stage2.initStyle(StageStyle.TRANSPARENT);
        stage2.setTitle("S2");

        stage2.show();

        // 纯白背景不带任何装饰
        Stage stage3 = new Stage();
        stage3.initStyle(StageStyle.UNDECORATED);
        stage3.setTitle("S3");

        stage3.show();

        // 统一的，不带标题，其他和第一个一样
        Stage stage4 = new Stage();
        stage4.initStyle(StageStyle.UNIFIED);
        stage4.setTitle("S4");

        stage4.show();

        // 实用程序，只带一个关闭舞台图标
        Stage stage5 = new Stage();
        stage5.initStyle(StageStyle.UTILITY);
        stage5.setTitle("S5");

        stage5.show();

        // 平台根据，非常多的工具
//        Platform.exit();  // 退出全部
    }

    public static void main(String[] args) {
        launch(args);
    }
}
