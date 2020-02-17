package cn.zjh567;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.net.URL;

public class SceneSingle extends Application {
    @Override
    public void start(Stage primarysStage) throws Exception {

        // 启动应用时自动打开 url
//        HostServices host = getHostServices();
//        host.showDocument("www.zjh567.cn");

        // 注意这里必须加上 / ，表示资源根目录
        URL url = SceneSingle.class.getResource("/logo.png");
        assert url != null;
        String path = url.toExternalForm();

        Button button = new Button();
        button.setPrefHeight(200);
        button.setPrefWidth(200);

        button.setCursor(Cursor.MOVE);

        Group group = new Group();
        group.getChildren().add(button);

        //scene 需要绑定 root 结点
        Scene scene = new Scene(group);

        // 设置鼠标风格
//        scene.setCursor(Cursor.cursor(path));
        scene.setCursor(Cursor.CLOSED_HAND);

        // 为舞台添加场景
        primarysStage.setScene(scene);

        primarysStage.setTitle("Scene");
        primarysStage.getIcons().add(new Image(path));
        primarysStage.setHeight(600);
        primarysStage.setWidth(800);

        primarysStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
