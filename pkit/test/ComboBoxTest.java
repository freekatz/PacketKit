import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ComboBoxTest extends Application
{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        AnchorPane pane = new AnchorPane();

        Image image = new Image(getClass().getResourceAsStream("icon/24x24/x-capture-start.png"));
        Button btn = new Button();
        btn.setGraphic(new ImageView(image));

        pane.getChildren().add(btn);

        Scene scene = new Scene(pane);



        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
