//import java.io.File;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import javafx.application.Application;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.VBox;
//import javafx.scene.web.WebEngine;
//import javafx.scene.web.WebView;
//import javafx.stage.Stage;
//
//public class WebTest extends Application {
//
//    @Override
//    public void start(final Stage stage) {
//
//        Button buttonHtmlFile = new Button("加载本地文件");
//
//        final WebView browser = new WebView();
//        final WebEngine webEngine = browser.getEngine();
//
//        buttonHtmlFile.setOnAction(new EventHandler<ActionEvent>() {
//
//            @Override
//            public void handle(ActionEvent event) {
//                try {
//                    File file = new File("pkit/src/main/java/web/app/protocolPieChart.html");
//                    URL url = file.toURI().toURL();
//                    // file:/D:/test/a.html
//                    System.out.println("Local URL: " + url.toString());
//                    webEngine.load(url.toString());
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//
//        VBox root = new VBox();
//        root.setPadding(new Insets(5));
//        root.setSpacing(5);
//        root.getChildren().addAll(buttonHtmlFile, browser);
//
//        Scene scene = new Scene(root);
//
//        stage.setScene(scene);
//        stage.setWidth(450);
//        stage.setHeight(300);
//
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//}