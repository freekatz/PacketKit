package pkit.gui;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.stage.Stage;

public class PlatformTools extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
//        Platform.runLater(new Runnable() {
//            // 这是一个队列，在空闲的时候运行一次更新界面，多个 run() 将以队列的形式依次运行
//            // 可以在 run 中修改主键的属性来更新界面，但是不要做繁重的任务
//            // 否则会卡死
//            @Override
//            public void run() {
//                System.out.println(Thread.currentThread().getName());
//            }
//        });
//        System.out.println(Thread.currentThread().getName());

//        // 关闭舞台之后，程序不会退出
//        Platform.setImplicitExit(false);
//        primaryStage.show();
//        // 必须再调用这个才可完全退出
//        Platform.exit();

        // 检查系统支持情况
        System.out.println(Platform.isSupported(ConditionalFeature.SCENE3D));
        System.out.println(Platform.isSupported(ConditionalFeature.FXML));

    }

    public static void main(String[] args) {
        launch(args);
    }
}


