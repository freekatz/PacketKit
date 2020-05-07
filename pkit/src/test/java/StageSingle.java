//
//
//import javafx.application.Application;
//import javafx.scene.image.Image;
//
//public class StageSingle extends Application {
//
//    @Override
//    public void start(javafx.stage.Stage primaryStage) throws Exception{
//
//        primaryStage.setTitle("Index");
//
//        Image image = new Image("file:E:\\GitHub\\PKit\\pkit-gui\\src\\test\\gui.res\\logo.png");
//        // set title icon
//        primaryStage.getIcons().add(image);
//
//        // 设置透明度
//        primaryStage.setOpacity(0.6);
//
//        // 设置舞台初始位置，原点为左上角，x y 范围为显示器分辨率大小
//        primaryStage.setX(500);
//        primaryStage.setY(200);
//        primaryStage.setWidth(600);
//        primaryStage.setHeight(400);
//
//        // 设置舞台在最顶层
//        primaryStage.setAlwaysOnTop(true);  // 与设置全屏冲突
//
//        // 设置全屏，注意，这个与最大化不一样
////        primaryStage.setFullScreen(true);
////        primaryStage.setScene(
////                new Scene(new Group())
////        ); // 这句必须设置，否则会有黑快
//
//        // set size
////        primaryStage.setHeight(500);
////        primaryStage.setWidth(1000);
////        primaryStage.setMaxHeight(600);
////        primaryStage.setMinHeight(400);
////        primaryStage.setMaxWidth(1200);
////        primaryStage.setMinWidth(800);
////        primaryStage.setResizable(false);  // set the right of modify size
//
////        primaryStage.setIconified(true);  // set min size auto
////        primaryStage.setMaximized(true);  // set max size auto
//
//        primaryStage.show();
//
//        // 必须先设置或者在 show 之后获取，否则为 NaN
////        double h = primaryStage.getHeight();
////        double w = primaryStage.getWidth();
////        System.out.println(h + " " + w);
//
//        // 监听高和宽
////        primaryStage.heightProperty().addListener(new ChangeListener<Number>() {
////            @Override
////            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
////                // 观察者模式 (visitor mode) 监听各种东西，可用于做响应式客户端应用，NB
////                System.out.println("height: " + observableValue.getValue());
////            }
////        });
////
////        primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
////            @Override
////            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
////                System.out.println("width: " + observableValue.getValue());
////            }
////        });
//
//
//        // 监听坐标
////        primaryStage.xProperty().addListener(new ChangeListener<Number>() {
////            @Override
////            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
////                System.out.println("X: " + observableValue.getValue());
////            }
////        });
////
////        primaryStage.yProperty().addListener(new ChangeListener<Number>() {
////            @Override
////            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
////                System.out.println("Y: " + observableValue.getValue());
////            }
////        });
//
////        primaryStage.close();  // close the stage
//    }
//
//    // 还可以添加各种监听器
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
