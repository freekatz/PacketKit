import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;

public class Launch extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        URL url = loader.getClassLoader().getResource("view/Manager.fxml");
        loader.setLocation(url);
        AnchorPane pane = loader.load();
        // TODO: 2020/4/18 WebView load failed
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

}
