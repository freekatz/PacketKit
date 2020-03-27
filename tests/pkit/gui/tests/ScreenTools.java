package pkit.gui.tests;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ScreenTools extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        Screen screen = Screen.getPrimary();

        Rectangle2D rect1 = screen.getBounds();
//        Rectangle2D rect2 = screen.getVisualBounds();

        System.out.println("左上角X: " + rect1.getMinX() + " " + "左上角Y: " + rect1.getMinY());
        System.out.println("右下角X: " + rect1.getMaxX() + " " + "右上角Y: " + rect1.getMaxY());
        System.out.println("宽度=" + rect1.getWidth() + " " + "高度=" + rect1.getHeight());
        System.out.println("DPI: " + screen.getDpi());

        Platform.exit();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
