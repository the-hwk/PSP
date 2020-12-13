package sample;

import containers.GsonContainer;
import controllers.Controller;
import data.AppConfig;
import data.Data;
import data.UDPMessage;
import enums.Action;
import handlers.ConnectionHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        AppConfig.getInstance().init();

        WebView webView = new WebView();

        WebEngine engine = webView.getEngine();
        engine.load(this.getClass().getResource("views/main.html").toExternalForm());

        engine.setJavaScriptEnabled(true);

        JSObject windowObject = (JSObject) engine.executeScript("window");
        Controller controller = new Controller(windowObject);
        windowObject.setMember("app", controller);

        engine.setOnAlert((WebEvent<String> wEvent) -> {
            System.out.println("JS alert() message: " + wEvent.getData() );
        });

        VBox root = new VBox();
        root.getChildren().addAll(webView);

        VBox.setVgrow(webView, Priority.ALWAYS);

        primaryStage.setTitle(AppConfig.getInstance().getProperty("app_name"));
        primaryStage.setMinWidth(Double.parseDouble(AppConfig.getInstance().getProperty("min_width")));
        primaryStage.setMinHeight(Double.parseDouble(AppConfig.getInstance().getProperty("min_height")));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        UDPMessage request = new UDPMessage(Action.CONNECTION_CLOSE,
                GsonContainer.getGson().toJson(Data.getUser()));
        ConnectionHandler.createRequest(request);

        Data.getNotifierThread().join();
    }
}
