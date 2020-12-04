package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        WebView webView = new WebView();

        WebEngine engine = webView.getEngine();
        engine.load(this.getClass().getResource("views/main.html").toExternalForm());

        engine.setJavaScriptEnabled(true);

        JSObject windowObject = (JSObject) engine.executeScript("window");
        Controller controller = new Controller(engine, windowObject);
        windowObject.setMember("app", controller);

        VBox root = new VBox();
        root.getChildren().addAll(webView);

        VBox.setVgrow(webView, Priority.ALWAYS);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
