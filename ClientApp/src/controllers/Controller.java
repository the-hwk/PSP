package controllers;

import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

public class Controller {
    private final WebEngine engine;

    public Controller(WebEngine engine) {
        this.engine = engine;
    }

    public void createConnection() {
        System.out.println("Connection");
        engine.load(this.getClass().getResource("../views/main.html").toExternalForm());
        JSObject windowObject = (JSObject) engine.executeScript("window");
        windowObject.setMember("app", this);
        /*
        if (Connection.createConnection(host)) {
            engine.load(this.getClass().getResource("views/auth.html").toExternalForm());
        } else {
            // TODO: JS Error
        }*/
    }

    public void check() {
        System.out.println("Check");
    }
}
