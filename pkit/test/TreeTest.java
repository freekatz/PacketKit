import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TreeTest extends Application {

    @Override
    public void start(Stage primaryStage) {

        String catRoot = "ROOT";
        // Java
        String catJava = "JAVA-00";
        String catJSP = "JAVA-01";
        String catSpring = "JAVA-02";
        // C#
        String catCSharp = "C#-00";
        String catWinForm = "C#-01";

        // Root Item
        TreeItem<String> rootItem = new TreeItem<String>(catRoot);
        rootItem.setExpanded(true);

        // Java
        TreeItem<String> itemJava = new TreeItem<String>(catJava);
        TreeItem<String> itemJSP = new TreeItem<String>(catJSP);
        TreeItem<String> itemSpring = new TreeItem<String>(catSpring);
        itemJava.getChildren().addAll(itemJSP, itemSpring);

        // CSharp
        TreeItem<String> itemCSharp = new TreeItem<String>(catCSharp);
        TreeItem<String> itemWinForm = new TreeItem<String>(catWinForm);
        itemCSharp.getChildren().addAll(itemWinForm);

        // Add to Root
        rootItem.getChildren().addAll(itemJava, itemCSharp);

        TreeView<String> tree = new TreeView<String>(rootItem);

        // Hide the root Item.
        tree.setShowRoot(false);

        StackPane root = new StackPane();
        root.setPadding(new Insets(5));
        root.getChildren().add(tree);

        primaryStage.setTitle("JavaFX TreeView示例(xntutor.com)");
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}