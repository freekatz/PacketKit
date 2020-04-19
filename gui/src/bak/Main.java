import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader();

        URL url = loader.getClassLoader().getResource("bak/controller/view/single/SettingView.fxml");

        loader.setLocation(url);
        AnchorPane anchorPane = loader.load();

        Scene scene = new Scene(anchorPane);

        stage.setScene(scene);

        stage.show();
    }
}
