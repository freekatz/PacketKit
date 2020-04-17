package pkit.gui;

import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StageModality extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        Stage stage1 = new Stage();
        stage1.setTitle("S1");
        stage1.show();

        /*
        APPLICATION 和 WINDOW 两种模态
        APPLICATION：同一程序中必须关闭模态舞台才可以使用其他舞台，如 stage3
        WINDOW：将阻止所有与之关联拥有者的所有舞台， 如 stage2
         */

        Stage stage2 = new Stage();
        stage2.setTitle("S2");
//        stage2.initOwner(stage1);
        stage2.initModality(Modality.WINDOW_MODAL);
        stage2.show();

        Stage stage3 = new Stage();
        stage3.setTitle("S3");
        stage3.initModality(Modality.APPLICATION_MODAL);
        stage3.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
